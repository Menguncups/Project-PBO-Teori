package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Main.GamePanel;

public class Entity {
    GamePanel gamePanel;
    public int worldX, worldY, kecepatanBergerak;
    public BufferedImage[] jalanAtas = new BufferedImage[9];
    public BufferedImage[] jalanBawah = new BufferedImage[9];
    public BufferedImage[] jalanKiri = new BufferedImage[9];
    public BufferedImage[] jalanKanan = new BufferedImage[9];
    public String dialouges[] = new String[20];

    public String arah;
    public int spriteCounter = 0;
    public int spriteNumber = 0;

    public Rectangle solidArea = new Rectangle(15, 30, 34, 34);
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;

    public boolean collisionOn = false;
    public int actionLockCounter = 0;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D g2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    public void speak() {

    }

    public void setAction() {

    }

    public void update() {
        setAction();
        collisionOn = false;
        gamePanel.collisioinCheck.checkTile(this);
        gamePanel.collisioinCheck.checkObject(this, false);
        gamePanel.collisioinCheck.checkPlayer(this);
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
    }
}
