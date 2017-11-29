import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class LineDrawer {

    private final GridDrawer drawer;

    public LineDrawer(GridDrawer drawer) {
        this.drawer = drawer;
    }

    public void drawLineWithColoredEnds(int x1, int y1, int x2, int y2) {
        Color startColor = drawer.getColor(x1, y1);
        int distX = x2 - x1;
        int distY = y2 - y1;
        boolean moveByX = Math.abs(distX) > Math.abs(distY);
        int iterationCount = Math.abs((moveByX ? distX : distY)) + 1;
        double dx = distX / (double) (iterationCount - 1);
        double dy = distY / (double) (iterationCount - 1);

        for (double x = x1, y = y1, i = 0; i < iterationCount; x += dx, y += dy, i++) {
            int curX = (int) Math.round(x);
            int curY = (int) Math.round(y);
            Color newColor = iterationCount == 1
                    ? startColor :
                    addVectorizedColor(startColor, drawer.getColor(x2, y2), i / (double) (iterationCount - 1));
            drawer.drawRectangle(curX, curY, newColor);
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        int distX = x2 - x1;
        int distY = y2 - y1;
        boolean moveByX = Math.abs(distX) > Math.abs(distY);
        int iterationCount = Math.abs((moveByX ? distX : distY)) + 1;
        double dx = distX / (double) (iterationCount - 1);
        double dy = distY / (double) (iterationCount - 1);

        for (double x = x1, y = y1, i = 0; i < iterationCount; x += dx, y += dy, i++) {
            int curX = (int) Math.round(x);
            int curY = (int) Math.round(y);
            drawer.drawRectangle(curX, curY, color);
        }
    }

    public void drawLine2Colored(int x1, int y1, int x2, int y2, Rectangle rect, Color colorInside, Color colorOutside) {
        int distX = x2 - x1;
        int distY = y2 - y1;
        boolean moveByX = Math.abs(distX) > Math.abs(distY);
        int iterationCount = Math.abs((moveByX ? distX : distY)) + 1;
        double dx = distX / (double) (iterationCount - 1);
        double dy = distY / (double) (iterationCount - 1);

        for (double x = x1, y = y1, i = 0; i < iterationCount; x += dx, y += dy, i++) {
            int curX = (int) Math.round(x);
            int curY = (int) Math.round(y);
            boolean inside = (rect.x1() <= curX && curX <= rect.x2()) && (rect.y1() <= curY && curY <= rect.y2());
            Color color = inside ? colorInside : colorOutside;
            drawer.drawRectangle(curX, curY, color);
        }
    }

    private Color addVectorizedColor(Color start, Color end, double mul) {
        int r = (int) Math.round(start.getRed()   + (end.getRed()   - start.getRed())   * mul);
        int g = (int) Math.round(start.getGreen() + (end.getGreen() - start.getGreen()) * mul);
        int b = (int) Math.round(start.getBlue()  + (end.getBlue()  - start.getBlue())  * mul);
        int a = (int) Math.round(start.getAlpha() + (end.getBlue()  - start.getAlpha()) * mul);
        return new Color(r, g, b, a);
    }
}