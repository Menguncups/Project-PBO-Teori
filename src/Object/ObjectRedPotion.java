package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

import Entity.Entity;
import Main.GamePanel;

public class ObjectRedPotion extends SupperObject {
    int value = 5;
    GamePanel gamePanel;

    public ObjectRedPotion(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        name = "Red Potion";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/006.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void use(Entity entity) {
        gamePanel.playSE(8);
        entity.life += 5;
        if (gamePanel.player.life > gamePanel.player.maxLife) {
            gamePanel.player.life = gamePanel.player.maxLife;
        }
    }

}
