package Object;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Entity.Entity;
import Main.GamePanel;

public class ObjectHealthDrop extends SupperObject {
    GamePanel gamePanel;

    public ObjectHealthDrop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        name = "Health Drop";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/008.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void use(Entity entity) {
        gamePanel.playSE(2);
        entity.life += 3;
        if (gamePanel.player.life > gamePanel.player.maxLife) {
            gamePanel.player.life = gamePanel.player.maxLife;
        }
    }

    @Override
    public void checkDrop() {
        // Drop pasti 1 heart di posisi slime mati
        ObjectHealthDrop heart = new ObjectHealthDrop(gamePanel);
        heart.worldX = this.worldX; // posisinya sama dengan slime
        heart.worldY = this.worldY;

        // simpan di gamePanel.obj[] yang kosong
        for (int i = 0; i < gamePanel.obj.length; i++) {
            if (gamePanel.obj[i] == null) {
                gamePanel.obj[i] = heart;
                break;
            }
        }
    }

}
