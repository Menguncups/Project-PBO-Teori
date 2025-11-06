package Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Main.GamePanel;

public class SupperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea= new Rectangle(0,0,64,64);
    public int solidAreaDefaultX=0;
    public int solidAreaDefaultY=0;

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
}
