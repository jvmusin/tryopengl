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

    @Override
    public void display(GLAutoDrawable drawable) {
        GridDrawer gridDrawer = new GridDrawer(Color.WHITE, Color.SKYBLUE, gl);
        Rectangle[][] field = gridDrawer.draw(rows, columns, 5, 0.5);

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

        TextRenderer textRenderer = new TextRenderer(new Font(null, Font.BOLD, 25));
        drawText("DDA Algorithm", field[dx[0] + 11][dy - 2], textRenderer);
        drawText("Bresenham's Algorithm", field[dx[1] + 9][dy - 2], textRenderer);

        window.swapBuffers();
    }

    private void drawText(String text, Rectangle start, TextRenderer textRenderer) {
        int width = window.getWidth();
        int height = window.getHeight();

        textRenderer.beginRendering(width, height);
        textRenderer.setColor(java.awt.Color.BLACK);
        textRenderer.draw(text, normalize(start.x1(), width), normalize(start.y1(), height));
        textRenderer.endRendering();
    }

    private int normalize(double coord, int windowLen) {
        return (int) ((coord + 1) / 2 * windowLen);
    }

    private void drawFigure(int[][] figure, int dx, int dy, LineDrawer drawer) {
        for (int i = 0; i + 1 < figure.length; i++) {
            int x1 = figure[i][0] + dx;
            int y1 = figure[i][1] + dy;
            int x2 = figure[i + 1][0] + dx;
            int y2 = figure[i + 1][1] + dy;
            drawer.draw(x1, y1, x2, y2);
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }
}