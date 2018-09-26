import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class Menu {
    protected static final Color BACKGROUND_COLOR = new Color(62, 66, 71, 250);
    protected static final Color FOREGROUND_COLOR = new Color(50, 50, 50);
    protected static final Color MENU_COLOR = new Color(200, 200, 200);
    protected static final Font TITLE_FONT = new Font("arial", Font.BOLD, 50);

    abstract void mouseAction(MouseEvent e);

    abstract void render(Graphics g);
}
