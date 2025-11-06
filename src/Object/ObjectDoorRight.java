package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjectDoorRight extends SupperObject{
    public ObjectDoorRight(){
        name = "DoorRight";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/005.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;

    }
}
