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

public class Demon extends Entity {
    GamePanel gamePanel;

    public boolean attacking = false;
    public int attackCooldown = 0;
    public int damage = 4;

    boolean hpBarOn = false;
    int hpBarCounter = 0;

    int detectRadius;
    int chaseAttackCooldown = 120; // 1 detik
    int chaseCurrentCooldown = 0;
    boolean playerPreviouslyInRadius = false;

    public Demon(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        detectRadius = gamePanel.ukuranKotak * 2; // 2 tiles
        name = "Demon";
        arah = "bawah";
        kecepatanBergerak = 2;
        maxLife = 4;
        life = maxLife;
        type = 2;

        solidArea.x = 25 * 2;
        solidArea.y = 26 * 2;
        solidArea.width = 19 * 2;
        solidArea.height = 23 * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 48;
        attackArea.height = 48;

        getImage();
        getAttackImage();
    }

    public void getImage() {
        int jumlahFrame = 11; // jalan 11 frame
        try {
            for (int i = 0; i < jumlahFrame; i++) {
                String num = String.format("%02d", i);
                setupImage("/Gambar/Enemy/Demon/Jalan/Atas/Demon_Jalan_Atas_" + num + ".png", jalanAtas);
                setupImage("/Gambar/Enemy/Demon/Jalan/Bawah/Demon_Jalan_Bawah_" + num + ".png", jalanBawah);
                setupImage("/Gambar/Enemy/Demon/Jalan/Kiri/Demon_Jalan_Kiri_" + num + ".png", jalanKiri);
                setupImage("/Gambar/Enemy/Demon/Jalan/Kanan/Demon_Jalan_Kanan_" + num + ".png", jalanKanan);
            }
        } catch (Exception e) {
            System.out.println("❌ Error Loading Demon Walk");
        }
    }

    public void getAttackImage() {
        int jumlahFrame = 23; // attack 23 frame
        try {
            for (int i = 0; i < jumlahFrame; i++) {
                String num = String.format("%02d", i);
                setupImage("/Gambar/Enemy/Demon/Attack/Atas/Demon_Attack_Atas_" + num + ".png", attackAtas);
                setupImage("/Gambar/Enemy/Demon/Attack/Bawah/Demon_Attack_Bawah_" + num + ".png", attackBawah);
                setupImage("/Gambar/Enemy/Demon/Attack/Kiri/Demon_Attack_Kiri_" + num + ".png", attackKiri);
                setupImage("/Gambar/Enemy/Demon/Attack/Kanan/Demon_Attack_Kanan_" + num + ".png", attackKanan);
            }
        } catch (Exception e) {
            System.out.println("❌ Error Loading Demon Attack");
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

    private String getDirectionToPlayer() {
        int dx = gamePanel.player.worldX - worldX;
        int dy = gamePanel.player.worldY - worldY;

        if (Math.abs(dx) > Math.abs(dy))
            return dx > 0 ? "kanan" : "kiri";
        else
            return dy > 0 ? "bawah" : "atas";
    }

    @Override
    public void update() {
        setAction();
        super.update();

        if (chaseCurrentCooldown > 0)
            chaseCurrentCooldown--;

        if (attacking)
            attackAnimation();
        if (attackCooldown > 0)
            attackCooldown--;
    }

    public void setAction() {
        // Hitung jarak ke player
        int dx = gamePanel.player.worldX - worldX;
        int dy = gamePanel.player.worldY - worldY;
        int distance = Math.max(Math.abs(dx), Math.abs(dy));

        boolean nowInRadius = distance < detectRadius;

        if (nowInRadius) {
            // Player dalam radius → kejar
            arah = getDirectionToPlayer();
            kecepatanBergerak = 5;

            // Serang periodic
            if (chaseCurrentCooldown == 0 && !attacking) {
                attacking = true;
                spriteNumber = 0;
                spriteCounter = 0;
                chaseCurrentCooldown = chaseAttackCooldown;
                kecepatanBergerak = 0;
            }
            playerPreviouslyInRadius = true;
        } else {
            // Player di luar radius → jalan random santai (gunakan kecepatan > 0)
            playerPreviouslyInRadius = false;
            attacking = false;
            // Sebelumnya 0 sehingga demon tidak pernah bergerak ketika pemain jauh.
            // Set ke 1 supaya demon bergerak 'santai' saat tidak mengejar.
            kecepatanBergerak = 2;
            chaseCurrentCooldown = 0;

            actionLockCounter++;
            if (actionLockCounter > 120) {
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

    public void attackAnimation() {
        spriteCounter++;
        if (spriteCounter > 5) {
            if (spriteNumber == 2)
                checkAttackHitbox();

            spriteNumber++;
            spriteCounter = 0;

            if (spriteNumber >= 23) { // selesai setelah 23 frame
                spriteNumber = 0;
                attacking = false;
                attackCooldown = 60; // jeda 1 detik
            }

        }
    }

    public void checkAttackHitbox() {
        int currentX = worldX;
        int currentY = worldY;
        int oldW = solidArea.width;
        int oldH = solidArea.height;

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

        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        boolean hit = gamePanel.collisioinCheck.checkPlayer(this);

        if (hit && !gamePanel.player.invincible && !gamePanel.player.invincible2) {
            gamePanel.player.life -= damage;
            gamePanel.player.invincible = true;
            System.out.println("Demon Hit Player! HP: " + gamePanel.player.life);
        }

        worldX = currentX;
        worldY = currentY;
        solidArea.width = oldW;
        solidArea.height = oldH;
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
    public void draw(Graphics2D g2) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        BufferedImage gambar = null;
        ArrayList<BufferedImage> animasiAktif = attacking
                ? (arah.equals("atas") ? attackAtas
                        : arah.equals("bawah") ? attackBawah : arah.equals("kiri") ? attackKiri : attackKanan)
                : (arah.equals("atas") ? jalanAtas
                        : arah.equals("bawah") ? jalanBawah : arah.equals("kiri") ? jalanKiri : jalanKanan);

        if (animasiAktif != null && !animasiAktif.isEmpty()) {
            if (spriteNumber >= animasiAktif.size())
                spriteNumber = 0;
            gambar = animasiAktif.get(spriteNumber);
        }

        if (gambar != null)
            g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak * 2, gamePanel.perbesarKotak * 2, null);

        // Hitbox kuning saat menyerang
        if (attacking) {
            g2.setColor(new Color(255, 255, 0, 150));
            int tempX = screenX + solidArea.x;
            int tempY = screenY + solidArea.y;
            switch (arah) {
                case "atas":
                    tempY -= attackArea.height;
                    break;
                case "bawah":
                    tempY += solidArea.height;
                    break;
                case "kiri":
                    tempX -= attackArea.width;
                    break;
                case "kanan":
                    tempX += solidArea.width;
                    break;
            }
            // g2.drawRect(tempX, tempY, attackArea.width, attackArea.height);
        }

        // HP Bar
        if (hpBarOn || invincible) {
            double oneScale = (double) gamePanel.ukuranKotak / maxLife;
            double hpBarValue = oneScale * life;
            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(screenX - 1, screenY - 16, gamePanel.ukuranKotak + 2, 12);
            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
        }

        // Efek transparan saat kena hit
        if (invincible) {
            hpBarOn = true;
            hpBarCounter++;
            if (hpBarCounter > 40) {
                invincible = false;
                hpBarCounter = 0;
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
}
