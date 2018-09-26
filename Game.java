import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Game extends Canvas {

    static final int WIDTH = 815, HEIGHT = 840;

    enum State {RUN, MENU_START, MENU_GAME, MENU_END, MENU_SETTINGS}

    State dameState;
    private Run run;
    private MenuStart menuStart;
    private MenuGame menuGame;
    private MenuEnd menuEnd;
    private Settings settings;
    private Field field;
    private Animator animator;
    private boolean render;

    public Game() {
        new Window(WIDTH, HEIGHT, "DeadDame", this);
        animator = new Animator();
        field = new Field(animator);
        run = new Run(this, field);
        menuStart = new MenuStart(this);
        menuGame = new MenuGame(this);
        menuEnd = new MenuEnd(this);
        settings = new Settings(this);
        dameState = State.MENU_START;
        render = true;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseAction(e);
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyAction(e);
            }
        });
    }

    public void start() {
        // Starts rendering with given FPS
        long lastTime = System.nanoTime();
        double FPS = 100;
        double ns = 1000000000 / FPS;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (render) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                delta--;
                frames++;
                render();
                if (System.currentTimeMillis() - timer > 1000) {
                    timer = System.currentTimeMillis();
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }
        }
    }

    // Add mouse listener methods
    private void mouseAction(MouseEvent e) {
        switch (dameState) {
            case MENU_START:
                menuStart.mouseAction(e);
                break;
            case RUN:
                run.mouseAction(e);
                break;
            case MENU_GAME:
                run.mouseAction(e);
                menuGame.mouseAction(e);
                break;
            case MENU_END:
                menuEnd.mouseAction(e);
                break;
            case MENU_SETTINGS:
                settings.mouseAction(e);
                break;
            default:
                menuStart.mouseAction(e);
                break;
        }
    }

    // Add key listener methods
    private void keyAction(KeyEvent e) {
        switch (dameState) {
            case RUN:
                run.keyAction(e);
                break;
            default:
                break;
        }
    }

    // Render GUI
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        switch (dameState) {
            case MENU_START:
                menuStart.render(g);
                break;
            case RUN:
                run.render(g);
                animator.render(g);
                break;
            case MENU_GAME:
                run.render(g);
                menuGame.render(g);
                break;
            case MENU_END:
                menuEnd.render(g);
                break;
            case MENU_SETTINGS:
                settings.render(g);
                break;
            default:
                menuStart.render(g);
                break;
        }

        g.dispose();
        bs.show();
    }

    // GETTER AND SETTER

    public Run getRun() {
        return run;
    }

    public Field getField() {
        return field;
    }

    // Start game
    public static void main(String args[]) {
        (new Game()).start();
    }

}
