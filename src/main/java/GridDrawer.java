import com.jogamp.opengl.GL2;

import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class GridDrawer {

    private final GL2 gl;
    private final Rectangle[][] grid;

    public GridDrawer(int rows, int columns,
                      double squareSize, double delimSize,
                      GL2 gl) {
        this.gl = gl;
        grid = createGrid(rows, columns, squareSize, delimSize);
    }

    public void fullRefill(Color pixelColor, Color backgroundColor) {
        clear(backgroundColor);
        for (Rectangle[] col : grid)
            for (Rectangle r : col)
                drawRectangle(r, pixelColor);
    }

    private Rectangle[][] createGrid(int rows, int columns, double squareSize, double delimSize) {
        double cellSize = squareSize + delimSize;
        double fullHeight = cellSize * rows;
        double fullWidth = cellSize * columns;
        Rectangle[][] grid = new Rectangle[columns][rows];
        for (int iy = 0; iy < rows; iy++) {
            for (int ix = 0; ix < columns; ix++) {
                double x1 = ix * cellSize + delimSize / 2;
                double y1 = iy * cellSize + delimSize / 2;
                double x2 = x1 + squareSize;
                double y2 = y1 + squareSize;

                x1 = x1 / fullWidth * 2 - 1;
                y1 = y1 / fullHeight * 2 - 1;
                x2 = x2 / fullWidth * 2 - 1;
                y2 = y2 / fullHeight * 2 - 1;
                grid[ix][iy] = new Rectangle(x1, y1, x2, y2);
            }
        }
        return grid;
    }

    public void drawLine(double x0, double y0, double x1, double y1, Color color, double lineWidth) {
        setColor(color);
        gl.glLineWidth((float) lineWidth);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(x0, y0);
        gl.glVertex2d(x1, y1);
        gl.glEnd();
    }

    public void drawRectangle(int x, int y, Color color) {
        setColor(color);
        Rectangle r = grid[x][y];
        r.setColor(color);
        gl.glRectd(r.x1(), r.y1(), r.x2(), r.y2());
    }

    private void drawRectangle(Rectangle r, Color color) {
        r.setColor(color);
        setColor(color);
        gl.glRectd(r.x1(), r.y1(), r.x2(), r.y2());
    }

    private void setColor(Color color) {
        gl.glColor4ub(
                (byte) color.getRed(),
                (byte) color.getGreen(),
                (byte) color.getBlue(),
                (byte) color.getAlpha());
    }

    public Color getColor(int x, int y) {
        return grid[x][y].getColor();
    }

    private void clear(Color withColor) {
        gl.glClearColor(
                withColor.getRed() / 255.0f,
                withColor.getGreen() / 255.0f,
                withColor.getBlue() / 255.0f,
                withColor.getAlpha() / 255.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    }

    public int getFieldWidth() {
        return grid.length;
    }

    public int getFieldHeight() {
        return grid[0].length;
    }

    public double getGlX(int x) {
        return grid[x][0].x1();
    }
    public double getGlY(int y) {
        return grid[0][y].y1();
    }
}