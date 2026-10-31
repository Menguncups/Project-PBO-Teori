package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class ObjectKey2 extends SupperObject {
    GamePanel gamePanel;

    public ObjectKey2(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        name = "Key2";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/017.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
