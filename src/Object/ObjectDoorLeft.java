package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjectDoorLeft extends SupperObject{
    public ObjectDoorLeft(){
        name = "DoorLeft";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/004.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = true;
    }
}
