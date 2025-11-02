package Entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY, kecepatanBergerak;
    public BufferedImage[] jalanAtas = new BufferedImage[9];
    public BufferedImage[] jalanBawah = new BufferedImage[9];
    public BufferedImage[] jalanKiri = new BufferedImage[9];
    public BufferedImage[] jalanKanan = new BufferedImage[9];

    public String arah;
    public int spriteCounter = 0;
    public int spriteNumber = 0;

    public Rectangle solidArea;
    public boolean collisionOn = false;
}
