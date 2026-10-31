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

public class Hog extends Entity {
    GamePanel gamePanel;
    boolean hpBarOn = false;

    int hpBarCounter = 0;
    int detectRadius;
    int attackCooldown = 120; // 1 detik
    int currentCooldown = 0;
    int attackDamage = 1;
    boolean playerPreviouslyInRadius = false;
    int contactDamageCooldown = 0; // cooldown untuk contact damage
    int contactDamageInterval = 60; // interval damage (1 detik = 60 frame)

    public Hog(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        detectRadius = gamePanel.ukuranKotak * 2; // 2 tiles
        super(gamePanel);
        name = "Hog";
        kecepatanBergerak = 2;
        maxLife = 3;
        arah = "bawah";
        life = maxLife;
        type = 2;

        solidArea.x = 25 * 2;
        solidArea.y = 26 * 2;
        solidArea.width = 19 * 2;
        solidArea.height = 23 * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();

    }

    public void getImage() {
        try {
            for (int i = 0; i < 4; i++) {
                jalanAtas.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Hog/Jalan/Atas/Hog_Jalan_Atas_" + i + ".png")));
                // /Gambar/Enemy/Hog/Jalan/Atas/Hog_Jalan_Atas_0.png
                jalanBawah.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Hog/Jalan/Bawah/Hog_Jalan_Bawah_" + i + ".png")));
                jalanKiri.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Hog/Jalan/Kiri/Hog_Jalan_Kiri_" + i + ".png")));
                jalanKanan.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Hog/Jalan/Kanan/Hog_Jalan_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            kecepatanBergerak = 2;

            // Serang periodic
            if (currentCooldown == 0 && !attack) {
                attack = true;
                currentCooldown = attackCooldown;
                kecepatanBergerak = 0;
            }
            playerPreviouslyInRadius = true;
        } else {
            // Player di luar radius → jalan random
            playerPreviouslyInRadius = false;
            kecepatanBergerak = 2;
            attack = false;
            currentCooldown = 0;

            actionLockCounter++;
            if (actionLockCounter == 60) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;

                if (i <= 25) {
                    arah = "atas";
                }
                if (i > 25 && i <= 50) {
                    arah = "bawah";
                }
                if (i > 50 && i <= 75) {
                    arah = "kiri";
                }
                if (i > 75 && i <= 100) {
                    arah = "kanan";
                }

                actionLockCounter = 0;
            }
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

    public void draw(Graphics2D g2) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        BufferedImage gambar = null;

        if (worldX + gamePanel.ukuranKotak > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.ukuranKotak < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.ukuranKotak > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.ukuranKotak < gamePanel.player.worldY + gamePanel.player.screenY) {

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

            // // debug = hitbox
            // g2.setColor(java.awt.Color.RED);
            // int collisionX = screenX + solidArea.x;
            // int collisionY = screenY + solidArea.y;
            // g2.drawRect(collisionX, collisionY, solidArea.width, solidArea.height);

            // // debug = ukuran image
            // g2.setColor(java.awt.Color.GREEN);
            // g2.drawRect(screenX, screenY, gamePanel.perbesarKotak * 2,
            // gamePanel.perbesarKotak * 2);
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
}
