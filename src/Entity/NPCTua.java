package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class NPCTua extends Entity {

    public NPCTua(GamePanel gamePanel) {
        super(gamePanel);
        name = "NPCTua";
        arah = "bawah";
        kecepatanBergerak = 2;
        getJalanNPC(9);
        solidArea.x = 15;
        solidArea.y = 30;
        solidArea.width = 34;
        solidArea.height = 34;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDialog();
    }

    public void speak() {
        isTalking = true;
        gamePanel.ui.currentDialog = dialouges[0];

        switch (gamePanel.player.arah) {
            case "atas":
                arah = "bawah";
                break;
            case "bawah":
                arah = "atas";
                break;
            case "kiri":
                arah = "kanan";
                break;
            case "kanan":
                arah = "kiri";
                break;

            default:
                break;
        }
    }

    public void setDialog() {
        dialouges[0] = "Hello, traveler! So you have come to this place?";
        dialouges[1] = "This village has been quiet for years...";
        dialouges[2] = "Good luck on your journey!";
    }

    // public void posisiAwal() {
    // worldX = gamePanel.ukuranKotak * 26; // untuk meletakan posisi awal player
    // worldY = gamePanel.ukuranKotak * 87;
    // arah = "kanan";
    // }

    public void getJalanNPC(int jumlahFrame) {
        try {
            for (int i = 0; i < jumlahFrame; i++) {
                jalanAtas.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Npc/NPC_Jalan_Atas_/NPC_Jalan_Atas_" + i + ".png")));
                jalanBawah.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Npc/NPC_Jalan_Bawah_/NPC_Jalan_Bawah_" + i + ".png")));
                jalanKiri.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Npc/NPC_Jalan_Kiri_/NPC_Jalan_Kiri_" + i + ".png")));
                jalanKanan.add(ImageIO.read(getClass()
                        .getResourceAsStream("/Gambar/Npc/NPC_Jalan_Kanan_/NPC_Jalan_Kanan_" + i + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {

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
                default:
                    break;
            }
            g2.drawImage(gambar, screenX, screenY, gamePanel.perbesarKotak, gamePanel.perbesarKotak, null);

            // // debug = hitboc
            // g2.setColor(java.awt.Color.red);
            // int collisionX = screenX + solidArea.x;
            // int collisionY = screenY + solidArea.y;
            // g2.drawRect(collisionX, collisionY, solidArea.width, solidArea.height);

            // // debug = ukuran image
            // g2.setColor(java.awt.Color.GREEN);
            // g2.drawRect(screenX, screenY, gamePanel.perbesarKotak, gamePanel.perbesarKotak);
        }
    }
}
