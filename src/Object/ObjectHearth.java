package Object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ObjectHearth extends SupperObject {

    public ObjectHearth() {
        name = "heart";
        heart = new BufferedImage[13];

        try {
            for (int i = 0; i < heart.length; i++) {
                heart[i] = ImageIO.read(getClass().getResourceAsStream(
                        "/Gambar/Object/HealthPlayer/Health_" + String.format("%02d", i) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
