package Object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjectKey extends SupperObject{
    public ObjectKey(){
        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Gambar/Object/002.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
