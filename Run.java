import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.Math;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Run {

    private Game game;
    private Field field;
    private int[][] status;
    private int side; // -1: Red Deadpool, 1: Blue Deadpool
    private int click1X = -1;
    private int click1Y = -1;
    private int winner = 0; // 0: Draw, -1: Red Deadpool, 1: Blue Deadpool, 3: Bot
    private int drawCount = 0;
    private boolean click1 = false;
    private boolean click1lock = false;
    private boolean hint1 = false;
    private boolean hint2 = false;
    private boolean hasBot = false;
    private Bot bot;
    private BufferedImage deadpool;
    private BufferedImage darkpool;
    private BufferedImage deaddame;
    private BufferedImage darkdame;

    final ExecutorService executor = Executors.newFixedThreadPool(4);

    Run(Game game, Field field) {
        this.game = game;
        this.field = field;
        side = -1;
        status = field.getStatus();
        bot = new Bot(field, 1, 1);

        // Setup images for GUI
        SpriteSheet ss = new SpriteSheet(new BufferedImageLoader().loadImage());
        deadpool = ss.grabImage(0, 0, 80, 80);
        darkpool = ss.grabImage(80, 0, 80, 80);
        deaddame = ss.grabImage(160, 0, 80, 80);
        darkdame = ss.grabImage(240, 0, 80, 80);
    }

    // Mouse listener
    void mouseAction(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        int x = (int) Math.floor(mx / 100);
        int y = (int) Math.floor(my / 100);

        // Throw "Not your turn!" hint
        if (status[x][y] == side * -1 || status[x][y] == side * -2) {
            executor.submit(() -> {
                hint2 = true;
                Utility.wait(1);
                hint2 = false;
            });
            return;
        }

        // Set click 1
        if (!click1 && (status[x][y] == side || status[x][y] == side * 2)) {
            if (field.canAnyEat(side) && !field.canEat(x, y, side)) {
                executor.submit(() -> {
                    hint1 = true;
                    Utility.wait(1);
                    hint1 = false;
                });
                return;
            }
            click1X = x;
            click1Y = y;
            click1 = true;
        }
        // Move (1 Field)
        else if (click1 && field.canMoveThere(click1X, click1Y, x, y, side)) {
            field.moveThere(click1X, click1Y, x, y);
            resetClick1();
            field.annouceDames();
            incDrawCount();
            winCondition(field.getWinner());
            if (!hasBot)
                side *= -1;
            else {
                bot.makeTurn();
                winCondition(field.getWinner());
            }
        }
        // Move and Eat (2 Fields)
        else if (click1 && field.canEatThere(click1X, click1Y, x, y, side)) {
            field.eatThat(click1X, click1Y, x, y);
            if (field.canEat(x, y, side)) {
                click1X = x;
                click1Y = y;
                click1lock = true;
            } else {
                resetClick1();
                click1lock = false;
                field.annouceDames();
                winCondition(field.getWinner());
                if (!hasBot)
                    side *= -1;
                else {
                    bot.makeTurn();
                    winCondition(field.getWinner());
                }
                resetDrawCount();
            }
        } else if (click1 && !click1lock) {
            resetClick1();
        }
    }

    // Key listener
    void keyAction(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case 27:
                game.dameState = Game.State.MENU_GAME;
        }
    }

    void render(Graphics g) {
        // Draw field background
        g.setColor(Color.white);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.fillRect(200 * i, 200 * j, 100, 100);
                g.fillRect(100 + 200 * i, 100 + 200 * j, 100, 100);
            }
        }
        g.setColor(new Color(85, 87, 91));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.fillRect(100 + 200 * i, 200 * j, 100, 100);
                g.fillRect(200 * i, 100 + 200 * j, 100, 100);
            }
        }

        // Mark selected field
        g.setColor(new Color(255, 0, 0, 150));
        g.fillRect(100 * click1X, 100 * click1Y, 100, 100);

        // Draw Red and Blue Deadpools
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (status[i][j] == 1) {
                    g.drawImage(darkpool, i * 100 + 10, j * 100 + 10, null);
                }
                if (status[i][j] == -1) {
                    g.drawImage(deadpool, i * 100 + 10, j * 100 + 10, null);
                }
                if (status[i][j] == 2) {
                    g.drawImage(darkdame, i * 100 + 10, j * 100 + 10, null);
                }
                if (status[i][j] == -2) {
                    g.drawImage(deaddame, i * 100 + 10, j * 100 + 10, null);
                }
            }
        }

        // Show hints
        if (hint1) {
            g.setColor(new Color(200, 200, 200));
            g.setFont(new Font("arial", Font.BOLD, 25));
            g.setColor(new Color(255, 0, 0, 220));
            g.fillRect(250, 25, 300, 50);
            g.setColor(Color.white);
            g.drawString("You can eat!", 325, 60);
        } else if (hint2) {
            g.setColor(new Color(200, 200, 200));
            g.setFont(new Font("arial", Font.BOLD, 25));
            g.setColor(new Color(255, 0, 0, 220));
            g.fillRect(250, 25, 300, 50);
            g.setColor(Color.white);
            g.drawString("Not your turn!", 325, 60);
        }
    }

    // UTILITY FUNCTIONS

    void resetRun() {
        side = -1;
        drawCount = 0;
        resetClick1();
        hasBot = false;
        field.resetField();
    }

    void resetClick1() {
        click1X = -1;
        click1Y = -1;
        click1 = false;
    }

    void winCondition(int cond) {
        System.out.println("Draw Count: " + getDrawCount());
        if (cond == 0 && drawCount < 50)
            return;
        if (hasBot && cond == 1)
            cond = 3;
        setWinner(cond);
        game.dameState = Game.State.MENU_END;
    }

    /**
     * Let two bots duel each other.
     * For test purposes.
     */
    void duel() {
        Bot bot1 = new Bot(field, 1, 2);
        Bot bot2 = new Bot(field, -1, 1);

        while (game.dameState == Game.State.RUN) {
            bot1.makeTurn();
            winCondition(field.getWinner());
            bot2.makeTurn();
            winCondition(field.getWinner());
        }
    }

    // GETTER AND SETTER

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public void setBot(boolean hasBot) {
        this.hasBot = hasBot;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void resetDrawCount() {
        drawCount = 0;
    }

    public void incDrawCount() {
        drawCount++;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }
}

