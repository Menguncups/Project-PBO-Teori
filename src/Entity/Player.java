package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.Keybind;

public class Player extends Entity {
    GamePanel gamePanel;
    Keybind keyBind;

    public Player(GamePanel gamePanel, Keybind keyBind) {
        this.gamePanel = gamePanel;
        this.keyBind = keyBind;
        posisiAwal();
        getJalanPlayer();
    }

    public void posisiAwal() {
        posisiX = 100;
        posisiY = 100;
        kecepatanBergerak = 4;
        arah = "kanan";
    }

    public void getJalanPlayer() {
        try {
            for (int i = 0; i < 9; i++) {
                jalanAtas[i] = ImageIO.read(
                        getClass().getResourceAsStream("/Sprite_Sheet/Player/Jalan_Atas_/Jalan_Atas_" + i + ".png"));
                jalanBawah[i] = ImageIO.read(
                        getClass().getResourceAsStream("/Sprite_Sheet/Player/Jalan_Bawah_/Jalan_Bawah_" + i + ".png"));
                jalanKiri[i] = ImageIO.read(
                        getClass().getResourceAsStream("/Sprite_Sheet/Player/Jalan_Kiri_/Jalan_Kiri_" + i + ".png"));
                jalanKanan[i] = ImageIO.read(
                        getClass().getResourceAsStream("/Sprite_Sheet/Player/Jalan_Kanan_/Jalan_Kanan_" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        boolean bergerak = false;

        if (keyBind.keAtas == true) {
            arah = "atas";
            posisiY -= kecepatanBergerak;
            bergerak = true;
        }
        if (keyBind.keBawah == true) {
            arah = "bawah";
            posisiY += kecepatanBergerak;
            bergerak = true;
        }
        if (keyBind.keKiri == true) {
            arah = "kiri";
            posisiX -= kecepatanBergerak;
            bergerak = true;
        }
        if (keyBind.keKanan == true) {
            arah = "kanan";
            posisiX += kecepatanBergerak;
            bergerak = true;
        }
        if (bergerak) {
            spriteCounter++;
            if (spriteCounter > 5) {
                spriteNumber++;
                if (spriteNumber >= 9)
                    spriteNumber = 0;
                spriteCounter = 0;
            }
        } else {
            spriteNumber = 0;
        }

    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.WHITE);
        // g2.fillRect(posisiX, posisiY, gamePanel.perbesarKotak,
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

        g2.drawImage(gambar, posisiX, posisiY,
                gamePanel.perbesarKotak, gamePanel.perbesarKotak, null);

    }
}
