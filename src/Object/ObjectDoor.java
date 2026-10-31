package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjectDoor extends SupperObject {
    public ObjectDoor() {
        name = "Door";
        pickupable = false;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/005.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = true;
    }
}
