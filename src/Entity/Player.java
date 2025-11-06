package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.Keybind;

public class Player extends Entity {
    Keybind keyBind;

    public final int screenX, screenY;
    int hasKey = 0;
    int useKey = 0;

    public Player(GamePanel gamePanel, Keybind keyBind) {
        super(gamePanel);
        this.keyBind = keyBind;
        screenX = gamePanel.screenWidth / 2 - (gamePanel.perbesarKotak / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.perbesarKotak / 2);

        solidArea = new Rectangle();
        solidArea.x = 15;
        solidArea.y = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 34;
        solidArea.height = 34;

        posisiAwal();
        getJalanPlayer();
    }

    public void posisiAwal() {
        worldX = gamePanel.ukuranKotak * 49; // untuk meletakan posisi awal player
        worldY = gamePanel.ukuranKotak * 36;
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
            //
            int objIndex = gamePanel.collisioinCheck.checkObject(this, true);
            pickUpObject(objIndex);
            // ngecheck npc collision
            int npcIndex = gamePanel.collisioinCheck.checkEntity(this, gamePanel.npc);
            interactNPC(npcIndex);

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

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gamePanel.obj[i].name;
            switch (objectName) {
                case "Key":
                    hasKey++;
                    gamePanel.obj[i] = null;
                    gamePanel.playSE(2);
                    break;
                case "DoorLeft":
                    if (hasKey > 0) {
                        gamePanel.obj[i] = null;
                        gamePanel.playSE(1);
                        useKey++;
                        if (useKey > 1) {
                            hasKey--;
                        }
                    }
                    break;
                case "DoorRight":
                    if (hasKey > 0) {
                        gamePanel.obj[i] = null;
                        gamePanel.playSE(1);
                        useKey++;
                        if (useKey > 1) {
                            hasKey--;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            gamePanel.gameState = gamePanel.dialogueState;
            gamePanel.ui.currentDialogIndex = 0;
            gamePanel.ui.currentDialog = gamePanel.npc[i].dialouges[gamePanel.ui.currentDialogIndex];
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
        g2.setColor(java.awt.Color.RED);
        int collisionX = screenX + solidArea.x;
        int collisionY = screenY + solidArea.y;
        g2.drawRect(collisionX, collisionY, solidArea.width, solidArea.height);

    }
}
