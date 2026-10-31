package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ObejctDoor11 extends SupperObject {
    public ObejctDoor11() {
        name = "Door11";
        pickupable = false;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/013.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = true;
    }
}
