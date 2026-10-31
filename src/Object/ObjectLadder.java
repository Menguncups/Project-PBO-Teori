package Object;

import Main.GamePanel;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ObjectLadder extends SupperObject {
    GamePanel gamePanel;

    public ObjectLadder(GamePanel gp) {
        this.gamePanel = gp;
        name = "Ladder";
        collision = false;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/047.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 🧭 Pastikan ini diisi benar:
        targetMap = 1; // misalnya naik ke Map 1 (Dungeon)
        targetX = 10; // posisi X tujuan
        targetY = 12; // posisi Y tujuan
    }
}
