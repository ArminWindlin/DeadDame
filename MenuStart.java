import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

class MenuStart extends Menu {
    private Game game;
    private BufferedImage titleImgRed;
    private BufferedImage titleImgBlue;

    MenuStart(Game game) {
        this.game = game;

        // Setup images for GUI
        SpriteSheet ss = new SpriteSheet(new BufferedImageLoader().loadImage());
        titleImgRed = ss.grabImage(0, 80, 600, 100);
        titleImgBlue = ss.grabImage(0, 200, 600, 100);
    }

    @Override
    void mouseAction(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        // Button: 1 Player
        if (Utility.mouseOver(mx, my, 200, 300, 400, 80)) {
            game.getRun().resetRun();
            game.getRun().setBot(true);
            game.dameState = Game.State.RUN;
        }
        // Button: 2 Player
        if (Utility.mouseOver(mx, my, 200, 450, 400, 80)) {
            game.getRun().resetRun();
            game.dameState = Game.State.RUN;
        }
        // Button: Settings
        if (Utility.mouseOver(mx, my, 200, 600, 400, 80)) {
            game.dameState = Game.State.MENU_SETTINGS;
        }
    }

    @Override
    void render(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(FOREGROUND_COLOR);
        g.fillRect(200, 300, 400, 80);
        g.fillRect(200, 450, 400, 80);
        g.fillRect(200, 600, 400, 80);

        g.setFont(TITLE_FONT);
        g.setColor(MENU_COLOR);
        g.drawString("1 Player", 300, 360);
        g.drawString("2 Player", 300, 510);
        g.drawString("Settings", 300, 660);
        if (LocalDateTime.now().getSecond() % 2 == 1)
            g.drawImage(titleImgBlue, 102, 100, null);
        else
            g.drawImage(titleImgRed, 102, 100, null);

    }
}
