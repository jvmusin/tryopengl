import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

@SuppressWarnings("WeakerAccess")
public abstract class Drawer {

    protected Rectangle[][] field;
    private Color color;
    protected GL2 gl;

    public Drawer(Rectangle[][] field, Color color, GL2 gl) {
        this.field = field;
        this.color = color;
        this.gl = gl;
    }

    protected void drawRectangle(Rectangle r) {
        gl.glRectd(r.x1(), r.y1(), r.x2(), r.y2());
    }

    protected void setup() {
        gl.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }
}