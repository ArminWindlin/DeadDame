import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

class BufferedImageLoader {

    private static BufferedImage sprite;

    BufferedImage loadImage() {
        if (sprite == null) {
            try {
                sprite = ImageIO.read(getClass().getResource("res/dame_sprite.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sprite;
    }
}

