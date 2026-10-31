package Enemy;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Entity.Entity;
import Main.GamePanel;
import Object.ObjectHealthDrop;

public class Orc extends Entity {
    GamePanel gamePanel;
    boolean hpBarOn = false;

    boolean canAttack = true;
    int hpBarCounter = 0;
    int attackCooldown = 120; // 1 detik cooldown (60 frame)
    int currentCooldown = 0;
    int attackDamage = 2; // besarnya damage ke player
    // Tambahkan di variabel class
    int detectRadius;
    int attackHitboxWidth = 39;
    int attackHitboxHeight = 14;
    // Flag untuk mendeteksi apakah player sebelumnya sudah berada di dalam radius
    boolean playerPreviouslyInRadius = false;// ukuran standar hitbox serangan (ubah sekali saja)
    int standardAttackWidth = 60;
    int standardAttackHeight = 36;

    public Orc(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        detectRadius = gamePanel.ukuranKotak * 2; // radius untuk mendeteksi player
        super(gamePanel);
        // Orc akan mengatur sendiri kapan memanggil setAction() supaya bisa
        // menentukan attack sebelum movement di base class dijalankan.
        useBaseSetAction = false;
        name = "Orc";
        kecepatanBergerak = 100;
        maxLife = 5;
        arah = "bawah";
        life = maxLife;
        type = 2;
        attackArea.width = standardAttackWidth;
        attackArea.height = standardAttackHeight;

        solidArea.x = 12 * 2;
        solidArea.y = 14 * 2;
        solidArea.width = 39 * 2;
        solidArea.height = 36 * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        if (arah.equals("atas") || arah.equals("bawah")) {
            attackArea.width = 39;
            attackArea.height = 14;
        } else {
            attackArea.width = 39;
            attackArea.height = 14;
        }

        getImage();
        getAttackImage();
    }

    public void getImage() {
        try {
            for (int i = 0; i < 5; i++) {
                jalanAtas.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Orc/Jalan/Atas/Orc_Jalan_Atas_" + i + ".png")));
                // src/Gambar/Enemy/Orc/Jalan/Atas/Orc_Jalan_Atas_0.png
                jalanBawah.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Orc/Jalan/Bawah/Orc_Jalan_Bawah_" + i + ".png")));
                jalanKiri.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Orc/Jalan/Kiri/Orc_Jalan_Kiri_" + i + ".png")));
                jalanKanan.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Orc/Jalan/Kanan/Orc_Jalan_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAttackImage() {
        try {
            for (int i = 0; i < 7; i++) {
                attackAtas.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Orc/Attack/Atas/Orc_Attack_Atas_" + i + ".png")));
                // src\Gambar\Enemy\Orc\Attack\Atas\Orc_Attack_Atas_0.png
                attackBawah.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Orc/Attack/Bawah/Orc_Attack_Bawah_" + i + ".png")));
                attackKiri.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Orc/Attack/Kiri/Orc_Attack_Kiri_" + i + ".png")));
                attackKanan.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Orc/Attack/Kanan/Orc_Attack_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Tentukan aksi (attack atau gerak) terlebih dahulu sehingga flag
        // `attack` dapat dipastikan sebelum base update melakukan movement.
        setAction();

        // Panggil update base yang akan melakukan pengecekan collision dan
        // movement (movement akan dilewati ketika attack==true karena perubahan
        // di Entity.update()).
        super.update();

        // Kurangi cooldown setelah update
        if (currentCooldown > 0)
            currentCooldown--;

        if (attack) {
            // Sedang menyerang → DIAM dan jalankan animasi
            kecepatanBergerak = 0;
            attack();
        }
    }

    public void move() {
        collisionOn = false;
        gamePanel.collisioinCheck.checkTile(this);
        gamePanel.collisioinCheck.checkPlayer(this);

        // Hanya bergerak jika tidak collision DAN kecepatanBergerak > 0
        if (!collisionOn && kecepatanBergerak > 0) {
            switch (arah) {
                case "atas":
                    worldY -= kecepatanBergerak;
                    break;
                case "bawah":
                    worldY += kecepatanBergerak;
                    break;
                case "kiri":
                    worldX -= kecepatanBergerak;
                    break;
                case "kanan":
                    worldX += kecepatanBergerak;
                    break;
            }
        }
    }

    int attackCounter = 0;
    int attackFrame = 0;

    public void attack() {
        attackCounter++;

        if (attackCounter > 8) { // kecepatan animasi
            attackFrame++;

            if (attackFrame == 3) {
                // gamePanel.playSE(4); // efek suara serangan

                // ===== CEK DAMAGE DI FRAME KE-3 =====
                // Hitung posisi attack hitbox
                int attackX = worldX + solidArea.x;
                int attackY = worldY + solidArea.y;
                int attackW = attackArea.width;
                int attackH = attackArea.height;

                switch (arah) {
                    case "atas":
                        attackY -= attackH;
                        break;
                    case "bawah":
                        attackY += solidArea.height;
                        break;
                    case "kiri":
                        attackX -= attackW;
                        break;
                    case "kanan":
                        attackX += solidArea.width;
                        break;
                }

                // Posisi player
                int playerX = gamePanel.player.worldX + gamePanel.player.solidArea.x;
                int playerY = gamePanel.player.worldY + gamePanel.player.solidArea.y;
                int playerW = gamePanel.player.solidArea.width;
                int playerH = gamePanel.player.solidArea.height;

                // Cek collision
                boolean hit = attackX < playerX + playerW &&
                        attackX + attackW > playerX &&
                        attackY < playerY + playerH &&
                        attackY + attackH > playerY;

                if (hit && !gamePanel.player.invincible && !gamePanel.player.invincible2) {
                    gamePanel.player.life -= attackDamage; // damage 2
                    gamePanel.player.invincible = true;
                    gamePanel.playSE(3);
                    System.out.println("💥 Player kena damage! HP: " + gamePanel.player.life);
                }
            }

            // Selesai animasi
            if (attackFrame >= 7) {
                attackFrame = 0;
                attack = false; // selesai menyerang, boleh gerak lagi
                canAttack = true;
                System.out.println("✅ Serangan selesai");
            }

            attackCounter = 0;
        }
    }

    public void setAction() {
        // Hitung jarak ke player
        int dx = gamePanel.player.worldX - worldX;
        int dy = gamePanel.player.worldY - worldY;
        int distance = Math.max(Math.abs(dx), Math.abs(dy));

        boolean nowInRadius = distance < detectRadius;

        if (nowInRadius) {
            // Player dalam radius ungu → kejar
            arah = getDirectionToPlayer();
            kecepatanBergerak = 4;

            // Update attackArea berdasarkan arah
            if (arah.equals("atas") || arah.equals("bawah")) {
                attackArea.width = 40;
                attackArea.height = 60;
            } else {
                attackArea.width = 60;
                attackArea.height = 40;
            }

            /*
             * Behavior change:
             * - Serang hanya pada momen PLAYER MASUK KE RADIUS (entry), bukan setiap frame
             * selama mengejar. Artinya jika pemain sudah berada di dalam radius, orc
             * tidak akan otomatis menyerang lagi sampai pemain keluar dan masuk lagi.
             * - Karena requirement: "tidak bisa menyerang kalau lagi jalan, hanya bisa
             * menyerang jika memasuki radius berwarna ungu" → kita trigger attack saat
             * nowInRadius && !playerPreviouslyInRadius.
             */
            // Jika player berada di dalam radius, orc akan mencoba menyerang setiap
            // `attackCooldown` frame (1 detik). Tidak menyerang saat sedang bergerak
            // karena `attack` akan menghentikan movement di base update.
            if (currentCooldown == 0 && !attack) {
                attack = true;
                currentCooldown = attackCooldown; // set cooldown supaya serang tiap 1 detik
                kecepatanBergerak = 0; // BERHENTI saat menyerang
                System.out.println("🗡️ ORC MENYERANG (periodic while in radius)!");
            }

            // tandai bahwa player sekarang berada di dalam radius agar serangan selanjutnya
            // tidak otomatis terjadi selama masih di dalam radius
            playerPreviouslyInRadius = true;

        } else {
            // Player di luar radius ungu → jalan acak
            // reset flag ketika player keluar radius
            playerPreviouslyInRadius = false;
            kecepatanBergerak = 3;
            attack = false; // pastikan tidak menyerang
            currentCooldown = 0; // reset cooldown jika player jauh

            actionLockCounter++;
            if (actionLockCounter >= 60) {
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

    public void draw(Graphics2D g2) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        BufferedImage gambar = null;

        if (worldX + gamePanel.ukuranKotak > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.ukuranKotak < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.ukuranKotak > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.ukuranKotak < gamePanel.player.worldY + gamePanel.player.screenY) {

            // Pilih sprite: jika sedang attack, gunakan frame dari attackAtas/..,
            // kalau tidak, gunakan sprite jalan (walking) seperti sebelumnya.
            if (attack) {
                int idx = attackFrame;
                // Pastikan index dalam rentang yang ada
                switch (arah) {
                    case "atas":
                        if (attackAtas.size() > 0) {
                            if (idx < 0)
                                idx = 0;
                            if (idx >= attackAtas.size())
                                idx = attackAtas.size() - 1;
                            gambar = attackAtas.get(idx);
                        }
                        break;
                    case "bawah":
                        if (attackBawah.size() > 0) {
                            if (idx < 0)
                                idx = 0;
                            if (idx >= attackBawah.size())
                                idx = attackBawah.size() - 1;
                            gambar = attackBawah.get(idx);
                        }
                        break;
                    case "kiri":
                        if (attackKiri.size() > 0) {
                            if (idx < 0)
                                idx = 0;
                            if (idx >= attackKiri.size())
                                idx = attackKiri.size() - 1;
                            gambar = attackKiri.get(idx);
                        }
                        break;
                    case "kanan":
                        if (attackKanan.size() > 0) {
                            if (idx < 0)
                                idx = 0;
                            if (idx >= attackKanan.size())
                                idx = attackKanan.size() - 1;
                            gambar = attackKanan.get(idx);
                        }
                        break;
                }
            } else {
                switch (arah) {
                    case "atas":
                        gambar = jalanAtas.get(spriteNumber);
                        break;
                    case "bawah":
                        gambar = jalanBawah.get(spriteNumber);
                        break;
                    case "kiri":
                        gambar = jalanKiri.get(spriteNumber);
                        break;
                    case "kanan":
                        gambar = jalanKanan.get(spriteNumber);
                        break;
                }
            }

            if (hpBarOn) {
                double oneScale = (double) (gamePanel.ukuranKotak / 3) / maxLife * 3;
                double hpBarValue = oneScale * life;

                // Posisi bar berdasarkan posisi hitbox (tengah atas)
                int collisionX = screenX + solidArea.x;
                int collisionY = screenY + solidArea.y;

                int barWidth = (int) (gamePanel.ukuranKotak); // panjang total bar
                int barX = collisionX + (solidArea.width / 2) - (barWidth / 2); // tengah
                int barY = collisionY - 15; // sedikit di atas hitbox

                // background hitam gelap
                g2.setColor(new java.awt.Color(35, 35, 35));
                g2.fillRect(barX - 1, barY - 1, barWidth + 2, 12);

                // bar darah merah
                g2.setColor(new java.awt.Color(255, 0, 30));
                g2.fillRect(barX, barY, (int) hpBarValue, 10);

                hpBarCounter++;
                if (hpBarCounter > 300) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }

            // gambar musuh
            g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak * 2, gamePanel.perbesarKotak * 2, null);

            // kembalikan transparansi normal
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // debug = hitbox
            // g2.setColor(java.awt.Color.RED);
            // int collisionX = screenX + solidArea.x;
            // int collisionY = screenY + solidArea.y;
            // g2.drawRect(collisionX, collisionY, solidArea.width, solidArea.height);

            // // debug = ukuran image
            // g2.setColor(java.awt.Color.GREEN);
            // g2.drawRect(screenX, screenY, gamePanel.perbesarKotak * 2,
            // gamePanel.perbesarKotak * 2);
        }

        // g2.setColor(new java.awt.Color(128, 0, 128, 100)); // ungu transparan
        // int radiusX = screenX + solidArea.x + solidArea.width / 2 - detectRadius / 2;
        // int radiusY = screenY + solidArea.y + solidArea.height / 2 - detectRadius /
        // 2;
        // g2.fillOval(radiusX, radiusY, detectRadius, detectRadius);

        // Gambar hitbox serangan (kuning) saat attack
        // Gambar hitbox serangan (kuning) saat attack
        if (attack) {
            int attackX = worldX + solidArea.x;
            int attackY = worldY + solidArea.y;

            // Gunakan ukuran attackArea sesuai arah
            int attackW = attackArea.width;
            int attackH = attackArea.height;

            switch (arah) {
                case "atas":
                    attackY -= attackH; // geser ke atas
                    break;
                case "bawah":
                    attackY += solidArea.height; // geser ke bawah
                    break;
                case "kiri":
                    attackX -= attackW; // geser ke kiri
                    break;
                case "kanan":
                    attackX += solidArea.width; // geser ke kanan
                    break;
            }

            // Koordinat layar
            screenX = attackX - gamePanel.player.worldX + gamePanel.player.screenX;
            screenY = attackY - gamePanel.player.worldY + gamePanel.player.screenY;

            // Gambar kotak kuning sesuai ukuran attackArea
            // g2.setColor(new java.awt.Color(255, 255, 0, 180)); // kuning transparan
            // g2.fillRect(screenX, screenY, attackW, attackH);
            // g2.setColor(java.awt.Color.YELLOW); // border
            // g2.drawRect(screenX, screenY, attackW, attackH);
        }

        // Hanya gambar hitbox kuning saat sedang menyerang
        if (attack) {
            int attackX = worldX + solidArea.x;
            int attackY = worldY + solidArea.y;

            switch (arah) {
                case "atas":
                    attackY -= attackArea.height;
                    break;
                case "bawah":
                    attackY += solidArea.height;
                    break;
                case "kiri":
                    attackX -= attackArea.width;
                    break;
                case "kanan":
                    attackX += solidArea.width;
                    break;
            }

            screenX = attackX - gamePanel.player.worldX + gamePanel.player.screenX;
            screenY = attackY - gamePanel.player.worldY + gamePanel.player.screenY;

            // 🔹 Gambar hitbox kuning semi-transparan
            // g2.setColor(new java.awt.Color(255, 255, 0, 200)); // kuning lebih terang
            // g2.fillRect(attackScreenX, attackScreenY, attackArea.width,
            // attackArea.height);

            // 🔹 border kuning untuk lebih jelas
            // g2.setColor(java.awt.Color.YELLOW);
            // g2.drawRect(screenX, screenY, attackArea.width, attackArea.height);
        }
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
    public void checkDrop() {
        int chance = new Random().nextInt(100); // 0–99
        int threshold = 50;
        lastDropChance = "Drop Roll: " + chance + " / " + threshold + " -> ";
        if (chance < threshold) { // 50% kemungkinan drop
            ObjectHealthDrop heart = new ObjectHealthDrop(gamePanel);
            heart.worldX = this.worldX; // posisi slime mati
            heart.worldY = this.worldY;

            // simpan di gamePanel.obj[] yang kosong
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
}
