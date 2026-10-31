package Enemy;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import Entity.Entity;
import Main.GamePanel;
import Object.ObjectHealthDrop;

public class BossRock extends Entity {
    GamePanel gamePanel;

    // UI & Status
    boolean hpBarOn = false;
    int hpBarCounter = 0;

    // Combat Stats
    int attackDamage = 5; // Damage Boss
    int attackCooldown = 120; // 2 Detik (60 frame x 2)
    int currentCooldown = 0;
    boolean attacking = false;
    int attackFrameCounter = 0;

    // Logic Detection (Mirip Orc)
    int detectRadius;
    boolean playerPreviouslyInRadius = false;

    // Ukuran Serangan (Base)
    int baseAttackWidth = 80;
    int baseAttackHeight = 48;

    // Skala Gambar Boss
    int scaleBoss = 5;

    public BossRock(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        // Config Entity
        name = "BossRock";
        kecepatanBergerak = 2; // Jalan santai saat patroli
        maxLife = 10;
        life = maxLife;
        arah = "bawah";
        type = 2;

        // PENTING: Matikan setAction bawaan biar kita bisa custom logic sendiri di
        // update()
        useBaseSetAction = false;

        // Hitbox Badan (Disesuaikan dengan Scale x5)
        solidArea.x = 40;
        solidArea.y = 40;
        solidArea.width = (gamePanel.ukuranKotak * scaleBoss) - 80;
        solidArea.height = (gamePanel.ukuranKotak * scaleBoss) - 80;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Hitbox Serangan Awal
        attackArea.width = baseAttackWidth;
        attackArea.height = baseAttackHeight;

        // Radius Deteksi (Lebih besar dari Orc karena Boss besar)
        detectRadius = gamePanel.ukuranKotak * 5;

        getImage();
        getAttackImage();
    }

    public void getImage() {
        int jumlahFrame = 9;
        try {
            for (int i = 0; i < jumlahFrame; i++) {
                String num = String.format("%02d", i);
                setupImage("/Gambar/Enemy/BossRock/Jalan/Atas/Boss_Jalan_Atas_" + num + ".png", jalanAtas);
                setupImage("/Gambar/Enemy/BossRock/Jalan/Bawah/Boss_Jalan_Bawah_" + num + ".png", jalanBawah);
                setupImage("/Gambar/Enemy/BossRock/Jalan/Kiri/Boss_Jalan_Kiri_" + num + ".png", jalanKiri);
                setupImage("/Gambar/Enemy/BossRock/Jalan/Kanan/Boss_Jalan_Kanan_" + num + ".png", jalanKanan);
            }
        } catch (Exception e) {
        }
    }

    public void getAttackImage() {
        int jumlahFrame = 9;
        try {
            for (int i = 0; i < jumlahFrame; i++) {
                String num = String.format("%02d", i);
                setupImage("/Gambar/Enemy/BossRock/Attack/Atas/Boss_Attack1_Atas_" + num + ".png", attackAtas);
                setupImage("/Gambar/Enemy/BossRock/Attack/Bawah/Boss_Attack1_Bawah_" + num + ".png", attackBawah);
                setupImage("/Gambar/Enemy/BossRock/Attack/Kiri/Boss_Attack1_Kiri_" + num + ".png", attackKiri);
                setupImage("/Gambar/Enemy/BossRock/Attack/Kanan/Boss_Attack1_Kanan_" + num + ".png", attackKanan);
            }
        } catch (Exception e) {
        }
    }

    public void setupImage(String path, ArrayList<BufferedImage> list) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null)
                list.add(ImageIO.read(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        // 1. Tentukan Aksi (Jalan Random / Kejar / Serang)
        setAction();

        // 2. Update Fisik (Gerak & Collision)
        super.update();

        // 3. Handle Cooldown
        if (currentCooldown > 0)
            currentCooldown--;

        // 4. Handle Serangan
        if (attacking) {
            kecepatanBergerak = 0; // Boss berhenti saat nyerang
            performAttack();
        }
        if (life <= 0) {
            gamePanel.stopMusic(); // Hentikan musik battle
            // gamePanel.playSE(X); // (Opsional) Tambahkan efek suara menang
            gamePanel.gameState = gamePanel.gameWinState; // Pindah ke layar The End
        }
    }

    @Override
    public void checkDrop() {
        int chance = new Random().nextInt(100); // 0–99
        int threshold = 50;
        lastDropChance = "Drop Roll: " + chance + " / " + threshold + " -> ";
        if (chance < threshold) { // 50% kemungkinan drop
            ObjectHealthDrop heart = new ObjectHealthDrop(gamePanel);
            heart.worldX = this.worldX; // posisi mati
            heart.worldY = this.worldY;

            for (int i = 0; i < gamePanel.obj.length; i++) {
                if (gamePanel.obj[i] == null) {
                    gamePanel.obj[i] = heart;
                    lastDropChance += "Health Potion DROPPED";
                    gamePanel.debugMode.logDropChance(name, chance, threshold, "Health Potion DROPPED");
                    System.out.println(name + ": " + lastDropChance);
                    break;
                }
            }
        } else {
            lastDropChance += "NO DROP";
            gamePanel.debugMode.logDropChance(name, chance, threshold, "NO DROP");
            System.out.println(name + ": " + lastDropChance);
        }
    }

    @Override
    public void setAction() {
        // Hitung jarak ke center hitbox player vs center hitbox boss
        int bossCenterX = worldX + solidArea.x + solidArea.width / 2;
        int bossCenterY = worldY + solidArea.y + solidArea.height / 2;
        int playerCenterX = gamePanel.player.worldX + gamePanel.player.solidArea.x
                + gamePanel.player.solidArea.width / 2;
        int playerCenterY = gamePanel.player.worldY + gamePanel.player.solidArea.y
                + gamePanel.player.solidArea.height / 2;

        int distance = (int) Math
                .sqrt(Math.pow(bossCenterX - playerCenterX, 2) + Math.pow(bossCenterY - playerCenterY, 2));

        boolean nowInRadius = distance < detectRadius;

        if (nowInRadius) {
            // --- MODE KEJAR & SERANG ---
            arah = getDirectionToPlayer();
            kecepatanBergerak = 10; // Lebih cepat saat mengejar

            // Sesuaikan Area Serangan Berdasarkan Arah
            if (arah.equals("atas") || arah.equals("bawah")) {
                attackArea.width = 150; // Lebar ke samping
                attackArea.height = 100; // Jangkauan ke depan
            } else {
                attackArea.width = 100; // Jangkauan ke samping
                attackArea.height = 150; // Lebar vertikal
            }

            // Trigger Serangan jika cooldown ready & belum nyerang
            if (currentCooldown == 0 && !attacking) {
                attacking = true;
                spriteNumber = 0;
                currentCooldown = attackCooldown;
                kecepatanBergerak = 0;
            }
            playerPreviouslyInRadius = true;

        } else {
            // --- MODE PATROLI (Jalan Random) ---
            playerPreviouslyInRadius = false;
            kecepatanBergerak = 2; // Jalan santai
            attacking = false;
            currentCooldown = 0;

            actionLockCounter++;
            if (actionLockCounter >= 120) { // Ganti arah tiap 2 detik
                Random random = new Random();
                int i = random.nextInt(100) + 1;
                if (i <= 25)
                    arah = "atas";
                else if (i <= 50)
                    arah = "bawah";
                else if (i <= 75)
                    arah = "kiri";
                else
                    arah = "kanan";
                actionLockCounter = 0;
            }
        }
    }

    public void performAttack() {
        attackFrameCounter++;

        // Kecepatan animasi (Semakin kecil angka 8, semakin cepat pukulannya)
        if (attackFrameCounter > 8) {
            spriteNumber++;

            // 🔥 PERBAIKAN: Cek hitbox di BEBERAPA frame (Frame 3, 4, dan 5)
            // Ini membuat serangan lebih "menggigit" dan tidak mudah miss.
            if (spriteNumber >= 3 && spriteNumber <= 5) {
                checkHitbox();
            }

            // Animasi Selesai
            if (spriteNumber >= 8) {
                attacking = false;
                spriteNumber = 0;
            }
            attackFrameCounter = 0;
        }
    }

    public void checkHitbox() {
        // Simpan posisi Boss saat ini
        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        // Geser hitbox SolidArea boss ke depan sesuai arah serangan
        // Ini trik agar method checkPlayer() bisa dipakai untuk attack hitbox
        switch (arah) {
            case "atas":
                worldY -= attackArea.height;
                break;
            case "bawah":
                worldY += solidArea.height;
                break;
            case "kiri":
                worldX -= attackArea.width;
                break;
            case "kanan":
                worldX += solidArea.width;
                break;
        }

        // Ubah ukuran solidArea jadi ukuran AttackArea
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        // Cek Tabrakan dengan Player
        boolean hitPlayer = gamePanel.collisioinCheck.checkPlayer(this);

        // Jika kena player DAN player belum invincible
        if (hitPlayer && !gamePanel.player.invincible && !gamePanel.player.invincible2) {
            gamePanel.player.life -= attackDamage; // Kurangi darah
            gamePanel.player.invincible = true; // Beri waktu kebal (biar ga kena damage berkali-kali dlm 1 serangan)
            gamePanel.playSE(6); // Suara hit
            System.out.println("😱 BOSS HIT PLAYER! Sisa Darah: " + gamePanel.player.life);
        }

        // 🔥 PENTING: Kembalikan posisi dan ukuran hitbox badan Boss ke semula
        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }

    public String getDirectionToPlayer() {
        int dx = gamePanel.player.worldX - worldX;
        int dy = gamePanel.player.worldY - worldY;
        if (Math.abs(dx) > Math.abs(dy))
            return dx > 0 ? "kanan" : "kiri";
        else
            return dy > 0 ? "bawah" : "atas";
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        BufferedImage gambar = null;
        ArrayList<BufferedImage> animasiAktif = null;

        // Pilih Animasi
        if (attacking) {
            switch (arah) {
                case "atas":
                    animasiAktif = attackAtas;
                    break;
                case "bawah":
                    animasiAktif = attackBawah;
                    break;
                case "kiri":
                    animasiAktif = attackKiri;
                    break;
                case "kanan":
                    animasiAktif = attackKanan;
                    break;
            }
        } else {
            switch (arah) {
                case "atas":
                    animasiAktif = jalanAtas;
                    break;
                case "bawah":
                    animasiAktif = jalanBawah;
                    break;
                case "kiri":
                    animasiAktif = jalanKiri;
                    break;
                case "kanan":
                    animasiAktif = jalanKanan;
                    break;
            }
        }

        // Render Check (Jarak Jauh karena boss besar)
        if (worldX + gamePanel.ukuranKotak * 10 > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.ukuranKotak * 10 < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.ukuranKotak * 10 > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.ukuranKotak * 10 < gamePanel.player.worldY + gamePanel.player.screenY) {

            // Ambil Gambar
            if (animasiAktif != null && !animasiAktif.isEmpty()) {
                if (spriteNumber >= animasiAktif.size())
                    spriteNumber = 0;
                gambar = animasiAktif.get(spriteNumber);
            }

            // Draw Boss (Scale x5)
            if (gambar != null) {
                g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak * scaleBoss,
                        gamePanel.perbesarKotak * scaleBoss, null);
            } else {
                // Fallback Kotak Merah
                g2.setColor(Color.RED);
                g2.fillRect(screenX, screenY, gamePanel.ukuranKotak * scaleBoss, gamePanel.ukuranKotak * scaleBoss);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.drawString("BOSS", screenX + 50, screenY + 100);
            }

            // HP Bar
            if (hpBarOn || attacking || invincible) {
                double oneScale = (double) (gamePanel.ukuranKotak * scaleBoss) / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX, screenY - 30, gamePanel.ukuranKotak * scaleBoss, 20);
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 30, (int) hpBarValue, 20);

                if (!invincible && !attacking) {
                    hpBarCounter++;
                    if (hpBarCounter > 300) {
                        hpBarOn = false;
                        hpBarCounter = 0;
                    }
                }
            }

            // Efek Hit
            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                invincibleCounter++;
                if (invincibleCounter > 40) {
                    invincible = false;
                    invincibleCounter = 0;
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            // // --- DEBUG VISUAL ---
            // // 1. Radius Deteksi (Lingkaran Ungu)
            // g2.setColor(new Color(128, 0, 128, 100));
            // int centerX = screenX + (gamePanel.perbesarKotak * scaleBoss) / 2;
            // int centerY = screenY + (gamePanel.perbesarKotak * scaleBoss) / 2;
            // g2.drawOval(centerX - detectRadius, centerY - detectRadius, detectRadius * 2,
            // detectRadius * 2);

            // // 2. Hitbox Serangan (Kuning - Hanya saat nyerang)
            // if (attacking) {
            // g2.setColor(new Color(255, 255, 0, 150));
            // int hitX = screenX + solidArea.x;
            // int hitY = screenY + solidArea.y;
            // switch (arah) {
            // case "atas":
            // hitY -= attackArea.height;
            // break;
            // case "bawah":
            // hitY += solidArea.height;
            // break;
            // case "kiri":
            // hitX -= attackArea.width;
            // break;
            // case "kanan":
            // hitX += solidArea.width;
            // break;
            // }
            // g2.drawRect(hitX, hitY, attackArea.width, attackArea.height);
            // }

            // 3. Hitbox Badan (Biru)
            // g2.setColor(Color.BLUE);
            // g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width,
            // solidArea.height);
        }
    }
}