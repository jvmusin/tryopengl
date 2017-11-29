import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class TextDrawer {

    private final int windowWidth;
    private final int windowHeight;

    public TextDrawer(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void draw(String text, double x, double y) {
        TextRenderer tr = new TextRenderer(new Font(null, Font.BOLD, 25));

        tr.beginRendering(windowWidth, windowHeight);
        tr.setColor(Color.BLACK);
        int xNorm = normalize(x, windowWidth);
        int yNorm = normalize(y, windowHeight);
        tr.draw(text, xNorm, yNorm);
        tr.endRendering();
    }

    private int normalize(double coord, int windowLen) {
        return (int) ((coord + 1) / 2 * windowLen);
    }
}