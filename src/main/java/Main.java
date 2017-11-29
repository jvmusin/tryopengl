import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

import java.awt.*;

public class Main implements GLEventListener {

    private GLWindow window;
    private GL2 gl;
    private int rows = 90, columns = 90, scale = 10;

    public static void main(String[] args) {
        new Main().setup();
    }

    private void setup() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);
        window.setSize(columns * scale, rows * scale);
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

    private int tick;
    @Override
    public void display(GLAutoDrawable drawable) {
        if (tick++ % 10000 != 0) return;

        Rectangle[][] field = drawGrid(rows, columns, 10, 3, true);
        drawTriangles(field);
        drawImages(new Drawer(field, gl) {});

        window.swapBuffers();
    }

    private Rectangle[][] drawGrid(int rows, int columns, int squareSize, int delimSize, boolean fill) {
        Color backgroundColor = Color.WHITE;
        Color pixelColor = Color.CYAN;
        GridDrawer gridDrawer = new GridDrawer(backgroundColor, pixelColor, gl);
        Rectangle[][] field = gridDrawer.draw(rows, columns, squareSize, delimSize, fill);

        if (fill)
            for (int x = 0; x < columns; x++)
                for (int y = 0; y < rows / 2; y++)
                    gridDrawer.drawRectangle(field[x][y], backgroundColor);

        return field;
    }

    private void drawTriangles(Rectangle[][] field) {
        LineDrawer lineDrawer = new LineDrawer(field, gl);
        GouraudTriangleDrawer gouraudTriangleDrawer = new GouraudTriangleDrawer(lineDrawer);
        BarycentricTriangleDrawer barycentricTriangleDrawer = new BarycentricTriangleDrawer(lineDrawer);

        int[][] points = {{16, 0}, {32, 9}, {32, 26}, {16, 35}, {0, 26}, {0, 9}};
        int[][][] triangles = {
                {points[0], points[1], points[2]},
                {points[0], points[4], points[5]},
                {points[0], points[4], points[2]},
                {points[3], points[4], points[2]}
        };

        int[] dx = {5, columns / 2 + 5};
        int dy = rows / 2 + 5;

        for (int[][] triangle : triangles) {
            gouraudTriangleDrawer.drawTriangle(triangle, dx[0], dy);
            barycentricTriangleDrawer.drawTriangle(triangle, dx[1], dy);
        }

        TextDrawer textDrawer = new TextDrawer(field, gl, window.getWidth(), window.getHeight());
        textDrawer.draw("Gouraud method", field[dx[0] + 7][dy + 37]);
        textDrawer.draw("Barycentric method", field[dx[1] + 5][dy + 37]);
    }

    private void drawImages(Drawer drawer) {
        int height = rows * scale;
        int width = columns * scale;
        Rectangle[][] field = drawGrid(height, width, 1, 0, false);

        drawer.drawLine(-1, 0, 1, 0, Color.RED, 3);
        drawer.drawLine(0, -1, 0, 1, Color.RED, 3);

        Color[][] image = new ImageReader().readImage("image.bmp");

        ImageDrawer imageDrawer = new ImageDrawer(field, gl);
        TextDrawer textDrawer = new TextDrawer(field, gl, width, height);

        drawImage(image, 0, "Original image", imageDrawer, textDrawer);

        image = new ImageStretcher().stretchImage(image, 1.4, 0.7);
        drawImage(image, width / 2, "Stretched image", imageDrawer, textDrawer);
    }

    private void drawImage(Color[][] image, int dx, String name, ImageDrawer imageDrawer, TextDrawer textDrawer) {
        int width = columns * scale;
        int height = rows * scale;

        int imageWidth = image.length;
        int imageHeight = image[0].length;

        int startX = dx + (width / 2 - imageWidth) / 2;
        int startY = (height / 2 - imageHeight) / 2;
        imageDrawer.drawImage(image, startX, startY);

        int textY = (height / 2 - imageHeight) / 2 + 5 + imageHeight;
        textDrawer.draw(name, imageDrawer.field[width / 8 + 20 + dx][textY]);
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