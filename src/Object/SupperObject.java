package Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Entity.Entity;
import Main.GamePanel;

public class SupperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 64, 64);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public boolean pickupable = true;
    public BufferedImage[] heart = new BufferedImage[13];
    public int targetMap = -1;
    public int targetX = 0;
    public int targetY = 0;

    public void draw(Graphics2D g2, GamePanel gamePanel) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        if (worldX + gamePanel.ukuranKotak > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.ukuranKotak < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.ukuranKotak > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.ukuranKotak < gamePanel.player.worldY + gamePanel.player.screenY) {

            g2.drawImage(image, screenX, screenY, gamePanel.ukuranKotak, gamePanel.ukuranKotak, null);
        }
    }

    public void checkDrop() {

    }

    public void dropItem(Entity dropItem) {

    }
}
