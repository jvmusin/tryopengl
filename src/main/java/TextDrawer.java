import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class TextDrawer extends Drawer {

    private final int windowWidth, windowHeight;

    public TextDrawer(Rectangle[][] field, GL2 gl, int windowWidth, int windowHeight) {
        super(field, gl);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void draw(String text, Rectangle pos) {
        TextRenderer tr = new TextRenderer(new Font(null, Font.BOLD, 25));

        tr.beginRendering(windowWidth, windowHeight);
        tr.setColor(Color.BLACK);
        tr.draw(text, normalize(pos.x1(), windowWidth), normalize(pos.y1(), windowHeight));
        tr.endRendering();
    }

    private int normalize(double coord, int windowLen) {
        return (int) ((coord + 1) / 2 * windowLen);
    }
}