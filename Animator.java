import java.awt.*;
import java.awt.image.BufferedImage;

public class Animator {
    Field field;
    private int animateX = 0;
    private int animateY = 0;
    private int incAnimationSpeed = 0;
    private int animateStatus = 0; // 0: nothing, -1: redpool, 1: bluepool, -2: reddame, 2: bluedame
    private boolean swordAnimation = false;
    private BufferedImage deadpool;
    private BufferedImage darkpool;
    private BufferedImage deaddame;
    private BufferedImage darkdame;
    private BufferedImage sword_left;
    private BufferedImage sword_right;

    Animator() {
        // Setup images for GUI
        SpriteSheet ss = new SpriteSheet(new BufferedImageLoader().loadImage());
        deadpool = ss.grabImage(0, 0, 80, 80);
        darkpool = ss.grabImage(80, 0, 80, 80);
        deaddame = ss.grabImage(160, 0, 80, 80);
        darkdame = ss.grabImage(240, 0, 80, 80);
        sword_left = ss.grabImage(320, 0, 80, 80);
        sword_right = ss.grabImage(400, 0, 80, 80);
    }

    /**
     * Animates a move
     *
     * @param x1,     x start of piece
     * @param y1,     y start of piece
     * @param x2,     x goal of piece
     * @param y2,     y goal of piece
     * @param status, status of piece
     * @param eat,    does move eat or not
     * @param field,  current play field
     * @param eatX,   x of piece that gets eaten
     * @param eatY,   y of piece that gets eaten
     */
    void animate(int x1, int y1, int x2, int y2, int status, boolean eat, Field field, int eatX, int eatY) {
        animateX = x1;
        animateY = y1;
        animateStatus = status;
        this.field = field;
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        int animationLength = Math.abs(animateY - y2);

        while (animateY - y2 != 0) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                if (x1 < x2)
                    animateX += 2;
                else
                    animateX -= 2;
                if (y1 < y2)
                    animateY += 2;
                else
                    animateY -= 2;
                delta--;

                // Sword Animation
                if (eat) {
                    if (Math.abs(animateY - y2) < animationLength - animationLength / 4)
                        swordAnimation = true;
                    if (Math.abs(animateY - y2) < animationLength - animationLength / 4)
                        incAnimationSpeed += 2;
                    if (Math.abs(animateY - y2) < animationLength - animationLength / 2)
                        field.setStatus(eatX, eatY, 0);
                    if (Math.abs(animateY - y2) < animationLength / 3)
                        swordAnimation = false;
                }
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        animateStatus = 0;
        incAnimationSpeed = 0;
    }

    void render(Graphics g) {
        switch (animateStatus) {
            case 1:
                g.drawImage(darkpool, animateX, animateY, null);
                break;
            case -1:
                g.drawImage(deadpool, animateX, animateY, null);
                break;
            case 2:
                g.drawImage(darkdame, animateX, animateY, null);
                break;
            case -2:
                g.drawImage(deaddame, animateX, animateY, null);
                break;
            default:
                break;
        }
        if (swordAnimation) {
            g.drawImage(sword_left, animateX - 15 + incAnimationSpeed, animateY, null);
            g.drawImage(sword_right, animateX + 15 - incAnimationSpeed, animateY, null);
        }
    }

}
