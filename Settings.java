import java.awt.*;
import java.awt.event.MouseEvent;


class Settings extends Menu {
    private Game game;
    private int botDifficulty = 1;

    Settings(Game game) {
        this.game = game;
    }

    @Override
    void mouseAction(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        // Button: Menu
        if (Utility.mouseOver(mx, my, 200, 600, 400, 80)) {
            game.dameState = Game.State.MENU_START;
        }
        // Button: Animation On / Off
        if (Utility.mouseOver(mx, my, 325, 280, 150, 50)) {
            game.getField().setAnimated(!game.getField().getAnimated());
        }
        // Button: Bot Difficulty
        if (Utility.mouseOver(mx, my, 325, 480, 150, 50)) {
            botDifficulty++;
            if (botDifficulty > 2)
                botDifficulty = 0;
            switch (botDifficulty) {
                case 0:
                    game.getRun().setBot(new Bot(game.getField(), 1, 0));
                    break;
                case 1:
                    game.getRun().setBot(new Bot(game.getField(), 1, 1));
                    break;
                case 2:
                    game.getRun().setBot(new Bot(game.getField(), 1, 2));
                    break;
                default:
                    game.getRun().setBot(new Bot(game.getField(), 1, 1));
                    break;
            }
        }
    }

    @Override
    void render(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(FOREGROUND_COLOR);
        g.fillRect(200, 600, 400, 80);

        g.setFont(TITLE_FONT);
        g.setColor(MENU_COLOR);
        g.drawString("Settings", 300, 100);
        g.drawString("Menu", 330, 660);

        g.setFont(new Font("arial", Font.BOLD, 30));
        g.setColor(MENU_COLOR);
        g.drawString("Animation", 325, 250);
        g.drawString("Bot", 370, 450);

        g.drawRect(325, 280, 150, 50);

        // Animation switch
        if (game.getField().getAnimated()) {
            g.fillRect(400, 280, 75, 50);
            g.setColor(MENU_COLOR);
            g.drawString("Off", 335, 316);
            g.setColor(BACKGROUND_COLOR);
            g.drawString("On", 415, 316);
        } else {
            g.fillRect(325, 280, 75, 50);
            g.setColor(BACKGROUND_COLOR);
            g.drawString("Off", 335, 316);
            g.setColor(MENU_COLOR);
            g.drawString("On", 415, 316);

        }

        // Bot Difficulty Setting
        g.setColor(new Color(200, 200, 200));
        g.fillRect(325, 480, 150, 50);
        g.setColor(new Color(62, 66, 71, 250));
        switch (botDifficulty) {
            case 0:
                g.drawString("Easy", 335, 516);
                break;
            case 1:
                g.drawString("Medium", 335, 516);
                break;
            case 2:
                g.drawString("Hard", 335, 516);
                break;
            default:
                g.drawString("Easy", 335, 516);
                break;
        }

    }
}
