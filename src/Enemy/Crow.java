package Enemy;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import Entity.Entity;
import Main.GamePanel;
import Object.ObjectHealthDrop;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Crow extends Entity {
    GamePanel gamePanel;
    boolean hpBarOn = false;
    boolean attack = false;
    boolean playerPreviouslyInRadius = false;
    int hpBarCounter = 0;
    int attackCounter = 0;
    int attackFrame = 0;
    int attackDamage = 2;
    int attackCooldown = 120;
    int currentCooldown = 0;
    int detectRadius;
    int standardAttackWidth = 60;
    int standardAttackHeight = 36;

    public Crow(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        name = "Crow";
        kecepatanBergerak = 2;
        maxLife = 3;
        life = maxLife;
        type = 2;
        arah = "bawah";

        solidArea.x = 25 * 2;
        solidArea.y = 26 * 2;
        solidArea.width = 19 * 2;
        solidArea.height = 23 * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = standardAttackWidth;
        attackArea.height = standardAttackHeight;
        detectRadius = gamePanel.ukuranKotak * 2;

        getImage();
        getAttackImage();
    }

    public void getImage() {
        try {
            for (int i = 0; i < 5; i++) {
                String num = String.valueOf(i); // 0, 1, 2, 3, 4
                jalanAtas.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Jalan/Atas/Crow_Jalan_Atas_" + num + ".png")));
                jalanBawah.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Jalan/Bawah/Crow_Jalan_Bawah_" + num + ".png")));
                jalanKiri.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Jalan/Kiri/Crow_Jalan_Kiri_" + num + ".png")));
                jalanKanan.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Jalan/Kanan/Crow_Jalan_Kanan_" + num + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAttackImage() {
        try {
            for (int i = 0; i < 5; i++) {
                String num = String.valueOf(i); // 0, 1, 2, 3, 4
                attackAtas.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Attack/Atas/Crow_Attack_Atas_" + num + ".png")));
                attackBawah.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Attack/Bawah/Crow_Attack_Bawah_" + num + ".png")));
                attackKiri.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Attack/Kiri/Crow_Attack_Kiri_" + num + ".png")));
                attackKanan.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Enemy/Crow/Attack/Kanan/Crow_Attack_Kanan_" + num + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void setAction() {
        int dx = gamePanel.player.worldX - worldX;
        int dy = gamePanel.player.worldY - worldY;
        int distance = Math.max(Math.abs(dx), Math.abs(dy));
        boolean nowInRadius = distance < detectRadius;

        if (nowInRadius) {
            arah = getDirectionToPlayer();
            kecepatanBergerak = 2;

            if ((arah.equals("atas") || arah.equals("bawah"))) {
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
                if (i <= 25) {
                    arah = "atas";
                } else if (i <= 50) {
                    arah = "bawah";
                } else if (i <= 75) {
                    arah = "kiri";
                } else {
                    arah = "kanan";
                    actionLockCounter = 0;
                }
            }
        }

    }

    // attack() hanya untuk logika serangan
    public void attack() {
        attackCounter++;
        if (attackCounter > 8) {
            attackFrame++;

            if (attackFrame == 3) {
                // cek collision dan kasih damage
            }

            if (attackFrame >= 5) {
                attackFrame = 0;
                attack = false;
                attackCounter = 0;
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

        // 1. Tentukan animasi berdasarkan status
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

        // 2. Ambil gambar animasi
        if (animasiAktif != null && !animasiAktif.isEmpty()) {
            gambar = animasiAktif.get(attack ? attackFrame % animasiAktif.size() : spriteNumber % animasiAktif.size());
        }

        // 3. Gambar enemy
        if (gambar != null) {
            g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak * 2, gamePanel.perbesarKotak * 2, null);
        }

        // 4. Hitbox serangan (kuning) & logika damage
        if (attack) {
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

            // gambar hitbox kuning
            // g2.setColor(java.awt.Color.YELLOW);
            // g2.drawRect(attackX - gamePanel.player.worldX + gamePanel.player.screenX,
            // attackY - gamePanel.player.worldY + gamePanel.player.screenY,
            // attackW, attackH);

            // cek collision player
            int playerX = gamePanel.player.worldX + gamePanel.player.solidArea.x;
            int playerY = gamePanel.player.worldY + gamePanel.player.solidArea.y;
            int playerW = gamePanel.player.solidArea.width;
            int playerH = gamePanel.player.solidArea.height;

            boolean hit = attackX < playerX + playerW &&
                    attackX + attackW > playerX &&
                    attackY < playerY + playerH &&
                    attackY + attackH > playerY;

            if (hit && !gamePanel.player.invincible && !gamePanel.player.invincible2) {
                gamePanel.player.life -= 2; // damage 2
                gamePanel.player.invincible = true;
                gamePanel.playSE(6);
                System.out.println("💥 Player kena damage! HP: " + gamePanel.player.life);
            }
        }

        // 5. HP bar & efek hit
        if (hpBarOn || invincible) {
            double oneScale = (double) (gamePanel.ukuranKotak) / maxLife;
            double hpBarValue = oneScale * life;

            g2.setColor(new java.awt.Color(35, 35, 35));
            g2.fillRect(screenX - 1, screenY - 16, gamePanel.ukuranKotak + 2, 12);

            g2.setColor(new java.awt.Color(255, 0, 30));
            g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
        }

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

        // Debug hitbox enemy
        // g2.setColor(java.awt.Color.BLUE);
        // g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width,
        // solidArea.height);
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
}