import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

public class DDALineDrawer extends LineDrawer {

    @SuppressWarnings("WeakerAccess")
    public DDALineDrawer(Rectangle[][] field, Color color, GL2 gl) {
        super(field, color, gl);
    }

    @Override
    public void draw(int x1, int y1, int x2, int y2) {
        setup();

        int distX = x2 - x1;
        int distY = y2 - y1;
        boolean moveByX = Math.abs(distX) > Math.abs(distY);
        int iterationCount = Math.abs((moveByX ? distX : distY)) + 1;
        double dx = distX / (double) (iterationCount - 1);
        double dy = distY / (double) (iterationCount - 1);

        for (double x = x1, y = y1, i = 0; i < iterationCount; x += dx, y += dy, i++)
            drawRectangle(field[(int) Math.round(x)][(int) Math.round(y)]);
    }
}