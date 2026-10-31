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

public class Slime extends Entity {
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

    public Slime(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        detectRadius = gamePanel.ukuranKotak * 2; // 2 tiles
        super(gamePanel);
        name = "Slime";
        kecepatanBergerak = 1;
        maxLife = 2;
        arah = "bawah";
        life = maxLife;
        type = 2;

        solidArea.x = 4;
        solidArea.y = 20;
        solidArea.width = 56;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();

    }

    public void getImage() {
        try {
            for (int i = 0; i < 6; i++) {
                jalanAtas.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Slime/Slime_" + i + ".png")));
                // /Gambar/Enemy/Slime/Slime_0.png
                jalanBawah.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Slime/Slime_" + i + ".png")));
                jalanKiri.add(ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Enemy/Slime/Slime_" + i + ".png")));
                jalanKanan.add(ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Enemy/Slime/Slime_" + i + ".png")));
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
            kecepatanBergerak = 1;
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
                double oneScale = (double) gamePanel.ukuranKotak / 2;
                double hpBarValue = oneScale * life;

                g2.setColor(new java.awt.Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 13, gamePanel.ukuranKotak + 2, 12);

                g2.setColor(new java.awt.Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 12, (int) hpBarValue, 10);
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
            g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak, gamePanel.perbesarKotak, null);

            // kembalikan transparansi normal
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // // debug = hitbox
            // g2.setColor(java.awt.Color.RED);
            // int collisionX = screenX + solidArea.x;
            // int collisionY = screenY + solidArea.y;
            // g2.drawRect(collisionX, collisionY, solidArea.width, solidArea.height);

            // // debug = ukuran image
            // g2.setColor(java.awt.Color.GREEN);
            // g2.drawRect(screenX, screenY, gamePanel.perbesarKotak,
            // gamePanel.perbesarKotak);
        }
    }

    @Override
    public void checkDrop() {
        int chance = new Random().nextInt(100); // 0–99
        int threshold = 50;
        lastDropChance = "Drop Roll: " + chance + " / " + threshold + " -> "; // debug track
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
