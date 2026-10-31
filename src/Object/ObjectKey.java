package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class ObjectKey extends SupperObject {
    GamePanel gamePanel;

    public ObjectKey(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/002.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
