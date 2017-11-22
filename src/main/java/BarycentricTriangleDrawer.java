import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class BarycentricTriangleDrawer extends TriangleDrawer {

    public BarycentricTriangleDrawer(LineDrawer lineDrawer) {
        super(lineDrawer);
    }

    @Override
    public void drawTriangle(int[][] triangle, int dx, int dy) {
        shuffleColors();
        colorVertices(triangle, dx, dy);

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int i = 0; i < 3; i++) {
            int x = triangle[i][0];
            int y = triangle[i][1];
            minX = Math.min(minX, x); maxX = Math.max(maxX, x);
            minY = Math.min(minY, y); maxY = Math.max(maxY, y);
        }

        int fullArea2 = getArea2(triangle);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                double[] coefficients = getCoefficients(triangle, fullArea2, x, y);
                if (coefficients != null) {
                    double r = 0, g = 0, b = 0, a = 0;
                    for (int i = 0; i < 3; i++) {
                        r += coefficients[i] * colors[i].getRed();
                        g += coefficients[i] * colors[i].getGreen();
                        b += coefficients[i] * colors[i].getBlue();
                        a += coefficients[i] * colors[i].getAlpha();
                    }
                    drawRectangle(field[x + dx][y + dy], new Color(
                            (int) Math.round(r),
                            (int) Math.round(g),
                            (int) Math.round(b),
                            (int) Math.round(a)
                    ));
                }
            }
        }
    }

    double[] getCoefficients(int[][] triangle, int fullArea2, int x, int y) {
        int[] areas = new int[3];
        int curArea2 = 0;
        for (int i = 0; i < 3; i++) {
            int[][] newTriangle = new int[3][];
            for (int j = 0; j < 3; j++) {
                if (j == i) newTriangle[j] = new int[]{x, y};
                else newTriangle[j] = triangle[j];
            }
            areas[i] = getArea2(newTriangle);
            curArea2 += areas[i];
        }
        if (fullArea2 != curArea2) return null;
        double[] coefficients = new double[3];
        for (int i = 0; i < 3; i++) coefficients[i] = (double) areas[i] / fullArea2;
        return coefficients;
    }

    int getArea2(int[][] triangle) {
        int dx1 = triangle[1][0] - triangle[0][0];
        int dy1 = triangle[1][1] - triangle[0][1];
        int dx2 = triangle[2][0] - triangle[0][0];
        int dy2 = triangle[2][1] - triangle[0][1];

        return Math.abs(dx1 * dy2 - dy1 * dx2);
    }

    private void colorVertices(int[][] triangle, int dx, int dy) {
        for (int i = 0; i < 3; i++) {
            int[] cur = triangle[i];
            int x = cur[0];
            int y = cur[1];
            drawRectangle(field[x + dx][y + dy], colors[i]);
        }
    }
}