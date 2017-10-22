import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

public class GridDrawer extends Drawer {

    private Color backgroundColor;

    @SuppressWarnings("WeakerAccess")
    public GridDrawer(Color backgroundColor, Color pixelColor, GL2 gl) {
        super(null, pixelColor, gl);
        this.backgroundColor = backgroundColor;
    }

    @Override
    protected void setup() {
        super.setup();

        gl.glClearColor(
                (float) backgroundColor.getRed(),
                (float) backgroundColor.getGreen(),
                (float) backgroundColor.getBlue(),
                (float) backgroundColor.getOpacity());
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    }

    @SuppressWarnings("WeakerAccess")
    public Rectangle[][] draw(int rows, int columns, double squareSize, double delimSize) {
        setup();

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
                gl.glRectd(x1, y1, x2, y2);
            }
        }

        return field;
    }
}