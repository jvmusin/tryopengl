import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

public class BresenhamLineDrawer extends LineDrawer {

    @SuppressWarnings("WeakerAccess")
    public BresenhamLineDrawer(Rectangle[][] field, Color color, GL2 gl) {
        super(field, color, gl);
    }

    @Override
    public void draw(int x1, int y1, int x2, int y2) {
        setup();

        int distX = x2 - x1;
        int distY = y2 - y1;
        boolean moveByX = Math.abs(distX) > Math.abs(distY);
        int iterationCount;
        double delta;
        if (moveByX) {
            iterationCount = Math.abs(distX) + 1;
            delta = distY / (double) distX;
        } else {
            iterationCount = Math.abs(distY) + 1;
            delta = distX / (double) distY;
        }
        delta = Math.abs(delta);

        for (double x = x1, y = y1, error = 0, i = 0; i < iterationCount; i++) {
            drawRectangle(field[(int) Math.round(x)][(int) Math.round(y)]);

            error += delta;
            if (moveByX) {
                x += distX > 0 ? 1 : -1;
                if (error > 0.5) {
                    error--;
                    y += distY > 0 ? 1 : -1;
                }
            } else {
                y += distY > 0 ? 1 : -1;
                if (error > 0.5) {
                    error--;
                    x += distX > 0 ? 1 : -1;
                }
            }
        }
    }
}