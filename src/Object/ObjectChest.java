package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjectChest  extends SupperObject{
        public ObjectChest(){
        name = "Chest   ";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/003.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
