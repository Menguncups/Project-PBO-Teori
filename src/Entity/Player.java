package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.Keybind;

public class Player extends Entity {
    GamePanel gamePanel;
    Keybind keyBind;

    public final int screenX, screenY;

    public Player(GamePanel gamePanel, Keybind keyBind) {
        this.gamePanel = gamePanel;
        this.keyBind = keyBind;
        screenX = gamePanel.screenWidth / 2 - (gamePanel.perbesarKotak / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.perbesarKotak / 2);

        solidArea = new Rectangle();
        solidArea.x = 15;
        solidArea.y = 30;
        solidArea.width = 34;
        solidArea.height = 34;

        posisiAwal();
        getJalanPlayer();
    }

    public void posisiAwal() {
        worldX = gamePanel.ukuranKotak * 19; // untuk meletakan posisi awal player
        worldY = gamePanel.ukuranKotak * 21;
        kecepatanBergerak = 4;
        arah = "kanan";
    }

    public void getJalanPlayer() {
        try {
            for (int i = 0; i < 9; i++) {
                jalanAtas[i] = ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Player/Jalan_Atas_/Fighter_Jalan_Atas_" + i + ".png"));
                jalanBawah[i] = ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Player/Jalan_Bawah_/Fighter_Jalan_Bawah_" + i + ".png"));
                jalanKiri[i] = ImageIO.read(
                        getClass().getResourceAsStream("/Gambar/Player/Jalan_Kiri_/Fighter_Jalan_Kiri_" + i + ".png"));
                jalanKanan[i] = ImageIO.read(
                        getClass()
                                .getResourceAsStream("/Gambar/Player/Jalan_Kanan_/Fighter_Jalan_Kanan_" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        boolean bergerak = false;

        // ✅ Gunakan if-else supaya hanya 1 arah yang aktif
        if (keyBind.keAtas == true) {
            arah = "atas";
            bergerak = true;
        } else if (keyBind.keBawah == true) {
            arah = "bawah";
            bergerak = true;
        } else if (keyBind.keKiri == true) {
            arah = "kiri";
            bergerak = true;
        } else if (keyBind.keKanan == true) {
            arah = "kanan";
            bergerak = true;
        }

        // Hanya cek collision jika player memang bergerak
        if (bergerak) {
            collisionOn = false;
            gamePanel.collisioinCheck.checkTile(this);

            // Bergerak hanya jika tidak ada collision
            if (collisionOn == false) {
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

                    default:
                        break;
                }
            }

            // Update animasi
            spriteCounter++;
            if (spriteCounter > 5) {
                spriteNumber++;
                if (spriteNumber >= 9)
                    spriteNumber = 0;
                spriteCounter = 0;
            }
        } else {
            // Reset animasi saat tidak bergerak
            spriteNumber = 0;
        }
    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.WHITE);
        // g2.fillRect(worldX, worldY, gamePanel.perbesarKotak,
        // gamePanel.perbesarKotak);
        BufferedImage gambar = null;

        switch (arah) {
            case "atas":
                gambar = jalanAtas[spriteNumber];
                break;
            case "bawah":
                gambar = jalanBawah[spriteNumber];
                break;
            case "kiri":
                gambar = jalanKiri[spriteNumber];
                break;
            case "kanan":
                gambar = jalanKanan[spriteNumber];
                break;
            default:
                break;
        }

        g2.drawImage(gambar, screenX, screenY,
                gamePanel.perbesarKotak, gamePanel.perbesarKotak, null);

    }
}
