package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ObejctDoor12 extends SupperObject {
    public ObejctDoor12() {
        name = "Door12";
        pickupable = false;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/014.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = true;
    }
}
