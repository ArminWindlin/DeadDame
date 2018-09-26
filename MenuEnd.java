import java.awt.*;
import java.awt.event.MouseEvent;


class MenuEnd extends Menu {
    private Game game;

    MenuEnd(Game game) {
        this.game = game;
    }

    @Override
    void mouseAction(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        // Button: Menu
        if (Utility.mouseOver(mx, my, 200, 500, 400, 80)) {
            game.dameState = Game.State.MENU_START;
        }
    }

    @Override
    void render(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(FOREGROUND_COLOR);
        g.fillRect(200, 500, 400, 80);

        g.setFont(TITLE_FONT);
        g.setColor(MENU_COLOR);
        g.drawString("Menu", 330, 560);
        switch (game.getRun().getWinner()) {
            case 0:
                g.setColor(MENU_COLOR);
                g.drawString("Draw!", 325, 320);
                break;
            case 1:
                g.setColor(Color.blue);
                g.drawString("Blue Deadpool Wins!", 140, 320);
                break;
            case -1:
                g.setColor(Color.red);
                g.drawString("Red Deadpool Wins!", 140, 320);
                break;
            case 3:
                g.setColor(MENU_COLOR);
                g.drawString("Computer Wins!", 200, 320);
                break;
        }
    }
}
