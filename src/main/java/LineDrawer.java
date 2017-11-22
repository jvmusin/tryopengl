import com.jogamp.opengl.GL2;

import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class LineDrawer extends Drawer {

    public LineDrawer(Rectangle[][] field, GL2 gl) {
        super(field, gl);
    }

    public void drawLineWithColoredEnds(int x1, int y1, int x2, int y2) {
        Color startColor = field[x1][y1].getColor();
        int distX = x2 - x1;
        int distY = y2 - y1;
        boolean moveByX = Math.abs(distX) > Math.abs(distY);
        int iterationCount = Math.abs((moveByX ? distX : distY)) + 1;
        double dx = distX / (double) (iterationCount - 1);
        double dy = distY / (double) (iterationCount - 1);

        for (double x = x1, y = y1, i = 0; i < iterationCount; x += dx, y += dy, i++) {
            Rectangle cur = field[(int) Math.round(x)][(int) Math.round(y)];
            Color newColor = iterationCount == 1
                    ? startColor :
                    addVectorizedColor(startColor, field[x2][y2].getColor(), i / (double) (iterationCount - 1));
            drawRectangle(cur, newColor);
        }
    }
}