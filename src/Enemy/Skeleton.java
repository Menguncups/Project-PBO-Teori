package Enemy;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import Entity.Entity;
import Main.GamePanel;
import Object.ObjectHealthDrop;

public class Skeleton extends Entity {
    GamePanel gamePanel;

    boolean hpBarOn = false;
    int hpBarCounter = 0;

    boolean attack = false;
    int attackCounter = 0;
    int attackFrame = 0;
    int attackCooldown = 120; // frame cooldown
    int currentCooldown = 0;
    int attackDamage = 3;

    int detectRadius;
    int standardAttackWidth = 60;
    int standardAttackHeight = 36;
    boolean playerPreviouslyInRadius = false;

    public Skeleton(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        detectRadius = gamePanel.ukuranKotak * 2;
        useBaseSetAction = false;
        name = "Skeleton";
        kecepatanBergerak = 0;
        maxLife = 3;
        life = maxLife;
        type = 2; // Enemy
        arah = "bawah";

        attackArea.width = standardAttackWidth;
        attackArea.height = standardAttackHeight;

        solidArea.x = 12 * 2;
        solidArea.y = 14 * 2;
        solidArea.width = 39 * 2;
        solidArea.height = 36 * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        getAttackImage();
    }

    public void getImage() {
        try {
            for (int i = 0; i < 5; i++) {
                jalanAtas.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Skeleton/Jalan/Atas/Skeleton_Jalan_Atas_" + i + ".png")));
                jalanBawah.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Skeleton/Jalan/Bawah/Skeleton_Jalan_Bawah_" + i + ".png")));
                jalanKiri.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Skeleton/Jalan/Kiri/Skeleton_Jalan_Kiri_" + i + ".png")));
                jalanKanan.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Skeleton/Jalan/Kanan/Skeleton_Jalan_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAttackImage() {
        try {
            for (int i = 0; i < 4; i++) {
                attackAtas.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Skeleton/Attack/Atas/Skeleton_Attack_Atas_" + i + ".png")));
                attackBawah.add(ImageIO.read(getClass()
                        .getResourceAsStream(
                                "/Gambar/Enemy/Skeleton/Attack/Bawah/Skeleton_Attack_Bawah_" + i + ".png")));
                attackKiri.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Skeleton/Attack/Kiri/Skeleton_Attack_Kiri_" + i + ".png")));
                attackKanan.add(ImageIO.read(getClass()
                        .getResourceAsStream(
                                "/Gambar/Enemy/Skeleton/Attack/Kanan/Skeleton_Attack_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        setAction();
        super.update();

        if (currentCooldown > 0)
            currentCooldown--;

        if (attack) {
            kecepatanBergerak = 0;
            attack();
        }
    }

    public void attack() {
        attackCounter++;
        if (attackCounter > 8) {
            attackFrame++;

            // Hitbox damage di frame tertentu
            if (attackFrame == 3) {
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

                int playerX = gamePanel.player.worldX + gamePanel.player.solidArea.x;
                int playerY = gamePanel.player.worldY + gamePanel.player.solidArea.y;
                int playerW = gamePanel.player.solidArea.width;
                int playerH = gamePanel.player.solidArea.height;

                boolean hit = attackX < playerX + playerW &&
                        attackX + attackW > playerX &&
                        attackY < playerY + playerH &&
                        attackY + attackH > playerY;

                if (hit && !gamePanel.player.invincible && !gamePanel.player.invincible2) {
                    gamePanel.player.life -= attackDamage;
                    gamePanel.player.invincible = true;
                    gamePanel.playSE(6);
                    System.out.println("💥 Player kena damage! HP: " + gamePanel.player.life);
                }
            }

            if (attackFrame >= 4) {
                attackFrame = 0;
                attack = false;
            }
            attackCounter = 0;
        }
    }

    @Override
    public void setAction() {
        int dx = gamePanel.player.worldX - worldX;
        int dy = gamePanel.player.worldY - worldY;
        int distance = Math.max(Math.abs(dx), Math.abs(dy));
        boolean nowInRadius = distance < detectRadius;

        if (nowInRadius) {
            arah = getDirectionToPlayer();
            kecepatanBergerak = 3;

            if (arah.equals("atas") || arah.equals("bawah")) {
                attackArea.width = 40;
                attackArea.height = 60;
            } else {
                attackArea.width = 60;
                attackArea.height = 40;
            }

            if (currentCooldown == 0 && !attack) {
                attack = true;
                currentCooldown = attackCooldown;
                kecepatanBergerak = 0;
            }

            playerPreviouslyInRadius = true;
        } else {
            playerPreviouslyInRadius = false;
            kecepatanBergerak = 2;
            attack = false;
            currentCooldown = 0;

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

        // Pilih animasi
        if (attack) {
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

        // Render
        if (animasiAktif != null && !animasiAktif.isEmpty()) {
            gambar = animasiAktif.get(spriteNumber % animasiAktif.size());
        }
        if (gambar != null) {
            g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak * 2, gamePanel.perbesarKotak * 2, null);
        }

        // Hitbox kuning saat menyerang
        if (attack) {
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
