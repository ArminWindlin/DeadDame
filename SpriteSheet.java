import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage sprite;

    SpriteSheet(BufferedImage ss) {
        this.sprite = ss;
    }

    public BufferedImage grabImage(int col, int row, int width, int height) {
        return sprite.getSubimage(col, row, width, height);
    }

}
