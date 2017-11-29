import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("WeakerAccess")
public class GouraudTriangleDrawer extends TriangleDrawer {

    private final GridDrawer drawer;

    public GouraudTriangleDrawer(LineDrawer lineDrawer, GridDrawer drawer) {
        super(lineDrawer);
        this.drawer = drawer;
    }

    public void drawTriangle(int[][] triangle, int dx, int dy) {
        shuffleColors();

        colorVertices(triangle, dx, dy);
        drawBorder(triangle, dx, dy);
        drawInside(triangle, dx, dy);
    }

    private void colorVertices(int[][] triangle, int dx, int dy) {
        for (int i = 0; i < 3; i++) {
            int[] cur = triangle[i];
            drawer.drawRectangle(cur[0] + dx, cur[1] + dy, colors[i]);
        }
    }

    private void drawBorder(int[][] triangle, int dx, int dy) {
        for (int i = 0; i < 3; i++) {
            int[] cur = triangle[i];
            int[] next = triangle[(i + 1) % 3];
            int curX = cur[0], curY = cur[1];
            int nextX = next[0], nextY = next[1];
            lineDrawer.drawLineWithColoredEnds(curX + dx, curY + dy, nextX + dx, nextY + dy);
        }
    }

    private void drawInside(int[][] triangle, int dx, int dy) {
        Integer[] order = {0, 1, 2};
        Arrays.sort(order, Comparator.comparingInt(i -> triangle[i][1]));

        int x0 = triangle[order[0]][0] + dx;
        int x1 = triangle[order[1]][0] + dx;
        int x2 = triangle[order[2]][0] + dx;

        int y0 = triangle[order[0]][1] + dy;
        int y1 = triangle[order[1]][1] + dy;
        int y2 = triangle[order[2]][1] + dy;

        drawInside(x0, y0, x1, y1, x2, y2, x0, y0, y1);
        drawInside(x0, y0, x2, y2, x2, y2, x1, y1, y2);
    }

    private void drawInside(int x0, int y0, int x1, int y1, int x2, int y2, int startX, int startY, int finishY) {
        double deltaYA = y2 - y0;
        double deltaYB = y1 - startY;
        for (int y = startY + 1; y <= finishY; y++) {
            double kA = (y - y0) / deltaYA;
            double kB = (y - startY) / deltaYB;
            int xA = (int) Math.round((x2 - x0) * kA + x0);
            int xB = (int) Math.round((x1 - startX) * kB + startX);

            lineDrawer.drawLineWithColoredEnds(xA, y, xB, y);
        }
    }
}