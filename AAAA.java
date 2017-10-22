Rectangle.java

@SuppressWarnings("WeakerAccess")
public class Rectangle {

    private double x1, y1;
    private double x2, y2;

    public Rectangle(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double x1() { return x1; }
    public double y1() { return y1; }
    public double x2() { return x2; }
    public double y2() { return y2; }
    public double dx() { return x2 - x1; }
    public double dy() { return y2 - y1; }
}

---

Drawer.java

import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

@SuppressWarnings("WeakerAccess")
public abstract class Drawer {

    protected Rectangle[][] field;
    private Color color;
    protected GL2 gl;

    public Drawer(Rectangle[][] field, Color color, GL2 gl) {
        this.field = field;
        this.color = color;
        this.gl = gl;
    }

    protected void drawRectangle(Rectangle r) {
        gl.glRectd(r.x1(), r.y1(), r.x2(), r.y2());
    }

    protected void setup() {
        gl.glColor4d(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getOpacity());
    }
}

---

GridDrawer.java

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
    public Rectangle[][] draw(int rows, int columns,
                              double squareSize, double delimSize) {
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

---

LineDrawer.java

import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

public abstract class LineDrawer extends Drawer {

    @SuppressWarnings("WeakerAccess")
    public LineDrawer(Rectangle[][] field, Color color, GL2 gl) {
        super(field, color, gl);
    }

    public abstract void draw(int x1, int y1, int x2, int y2);
}

---

SolidLineDrawer.java

import com.jogamp.opengl.GL2;
import javafx.scene.paint.Color;

public class SolidLineDrawer extends LineDrawer {

    @SuppressWarnings("WeakerAccess")
    public SolidLineDrawer(Rectangle[][] field, Color color, GL2 gl) {
        super(field, color, gl);
    }

    @Override
    protected void setup() {
        super.setup();
        gl.glLineWidth(2f);
    }

    @Override
    public void draw(int x1, int y1, int x2, int y2) {
        setup();

        Rectangle first = field[x1][y1];
        Rectangle last = field[x2][y2];
        double dx2 = first.dx() / 2;
        double dy2 = first.dy() / 2;
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(first.x1() + dx2, first.y1() + dy2);
        gl.glVertex2d(last.x1() + dx2, last.y1() + dy2);
        gl.glEnd();
    }
}

---

DDALineDrawer.java

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

        double x = x1, y = y1;
        for (int i = 0; i < iterationCount; x += dx, y += dy, i++)
            drawRectangle(field[(int) Math.round(x)][(int) Math.round(y)]);
    }
}

---

BresenhamLineDrawer.java

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

        double x = x1, y = y1, error = 0;
        for (int i = 0; i < iterationCount; i++) {
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

---

PseudoPixelDrawing.java

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.awt.TextRenderer;
import javafx.scene.paint.Color;

import java.awt.*;

public class PseudoPixelDrawing implements GLEventListener {

    private GLWindow window;
    private GL2 gl;
    private int rows = 45, columns = 85;

    public static void main(String[] args) {
        new PseudoPixelDrawing().setup();
    }

    private void setup() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);
        window.setSize(columns * 16, rows * 16);
        window.setVisible(true);
        window.setAutoSwapBufferMode(false);
        gl = window.getGL().getGL2();

        window.addGLEventListener(this);

        Animator animator = new Animator(window);
        animator.start();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
                System.exit(1);
            }
        });
    }

    @Override public void init(GLAutoDrawable drawable) {}
    @Override public void dispose(GLAutoDrawable drawable) {}
    @Override public void reshape(GLAutoDrawable drawable,
                                  int x, int y, int width, int height) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        GridDrawer gridDrawer =
                new GridDrawer(Color.WHITE, Color.SKYBLUE, gl);
        Rectangle[][] field =
                gridDrawer.draw(rows, columns, 5, 0.5);

        LineDrawer dda = new DDALineDrawer(field, Color.FORESTGREEN, gl);
        LineDrawer bresenham = new BresenhamLineDrawer(field, Color.RED, gl);
        LineDrawer solid = new SolidLineDrawer(field, Color.BLACK, gl);

        int[][] figure = {
                {16, 0}, {32, 9}, {32, 26}, {16, 35}, {0, 26}, {0, 9},
                {16, 0}, {32, 26}, {0, 26},
                {16, 0}
        };
        int[] dx = {5, 45};
        int dy = 5;

        drawFigure(figure, dx[0], dy, dda);
        drawFigure(figure, dx[0], dy, solid);

        drawFigure(figure, dx[1], dy, bresenham);
        drawFigure(figure, dx[1], dy, solid);

        TextRenderer textRenderer
                = new TextRenderer(new Font(null, Font.BOLD, 25));
        drawText("DDA Algorithm",
                field[dx[0] + 11][dy - 2], textRenderer);
        drawText("Bresenham's Algorithm",
                field[dx[1] + 9][dy - 2], textRenderer);

        window.swapBuffers();
    }

    private void drawText(String text, Rectangle start,
                          TextRenderer textRenderer) {
        int width = window.getWidth();
        int height = window.getHeight();

        textRenderer.beginRendering(width, height);
        textRenderer.setColor(java.awt.Color.BLACK);
        textRenderer.draw(text,
                normalize(start.x1(), width),
                normalize(start.y1(), height));
        textRenderer.endRendering();
    }

    private int normalize(double coord, int windowLen) {
        return (int) ((coord + 1) / 2 * windowLen);
    }

    private void drawFigure(int[][] figure, int dx, int dy,
                            LineDrawer drawer) {
        for (int i = 0; i + 1 < figure.length; i++) {
            int x1 = figure[i][0] + dx;
            int y1 = figure[i][1] + dy;
            int x2 = figure[i + 1][0] + dx;
            int y2 = figure[i + 1][1] + dy;
            drawer.draw(x1, y1, x2, y2);
        }
    }
}