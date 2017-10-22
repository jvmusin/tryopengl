import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

public abstract class LineDrawer extends Drawer {

    @SuppressWarnings("WeakerAccess")
    public LineDrawer(Rectangle[][] field, Color color, GL2 gl) {
        super(field, color, gl);
    }

    public abstract void draw(int x1, int y1, int x2, int y2);
}