import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

public class SolidLineDrawer extends LineDrawer {

    @SuppressWarnings("WeakerAccess")
    public SolidLineDrawer(Rectangle[][] field, Color color, GL2 gl) {
        super(field, color, gl);
    }

    @Override
    protected void setup() {
        super.setup();
        gl.glLineWidth(2f);
    }

    @Override
    public void draw(int x1, int y1, int x2, int y2) {
        setup();

        Rectangle first = field[x1][y1];
        Rectangle last = field[x2][y2];
        double dx2 = first.dx() / 2;
        double dy2 = first.dy() / 2;
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(first.x1() + dx2, first.y1() + dy2);
        gl.glVertex2d(last.x1() + dx2, last.y1() + dy2);
        gl.glEnd();
    }
}