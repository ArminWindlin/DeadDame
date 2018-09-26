import java.awt.*;
import java.awt.event.MouseEvent;

class MenuGame extends Menu {
    private Game game;

    MenuGame(Game game) {
        this.game = game;
    }

    @Override
    void mouseAction(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        // Button: Continue
        if (Utility.mouseOver(mx, my, 200, 250, 400, 80)) {
            game.dameState = Game.State.RUN;
        }
        // Button: Menu
        if (Utility.mouseOver(mx, my, 200, 400, 400, 80)) {
            game.dameState = Game.State.MENU_START;
        }
    }

    @Override
    void render(Graphics g) {
        g.setColor(new Color(117, 155, 216, 100));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(new Color(62, 66, 71, 240));
        g.fillRect(200, 250, 400, 80);
        g.fillRect(200, 400, 400, 80);

        g.setFont(TITLE_FONT);
        g.setColor(Color.white);
        g.drawString("Continue", 300, 310);
        g.drawString("Menu", 300, 460);
    }
}
