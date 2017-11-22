import com.jogamp.opengl.GL2;

import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class GridDrawer extends Drawer {

    private Color pixelColor;
    private Color backgroundColor;

    public GridDrawer(Color backgroundColor, Color pixelColor, GL2 gl) {
        super(null, gl);
        this.backgroundColor = backgroundColor;
        this.pixelColor = pixelColor;
    }

    protected void fillBackground() {
        gl.glClearColor(
                backgroundColor.getRed() / 255.0f,
                backgroundColor.getGreen() / 255.0f,
                backgroundColor.getBlue() / 255.0f,
                backgroundColor.getAlpha() / 255.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    }

    public Rectangle[][] draw(int rows, int columns, double squareSize, double delimSize, boolean refill) {
        if (refill) {
            fillBackground();
            setColor(pixelColor);
        }

        double cellSize = squareSize + delimSize;
        double fullHeight = cellSize * rows;
        double fullWidth = cellSize * columns;
        Rectangle[][] field = new Rectangle[columns][rows];
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
                field[ix][iy] = new Rectangle(x1, y1, x2, y2);
                if (refill) drawRectangle(field[ix][iy], pixelColor);
            }
        }

        return field;
    }
}