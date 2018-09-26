import java.util.Date;

public class Utility {

    // Mouse Over Test
    static boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return mx > x && mx < x + width && my > y && my < y + height;
    }

    // Wait (seconds)
    static void wait(int sec) {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        while (elapsedTime < sec * 1000) {
            elapsedTime = (new Date()).getTime() - startTime;
        }
    }
}
