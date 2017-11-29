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
        if (tick > 0) return;
        if (tick++ % 10000 != 0) return;

        GridDrawer gridDrawer = drawGrid(rows, columns, 10, 3, true);

        int size = rows;
        int border = 15;
        int bot = border;
        int top = size - border - 1;
        int left = border;
        int right = size - border - 1;

        int curX = left;
        int curY = bot;
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int dir = 1;
        int step = size - border * 2;
        for (int i = 0; i < 4; i++) {
            double x0 = gridDrawer.getGlX(curX);
            double y0 = gridDrawer.getGlY(curY);
            curX += dx[dir % 4] * step;
            curY += dy[dir % 4] * step;
            dir++;
            double x1 = gridDrawer.getGlX(curX);
            double y1 = gridDrawer.getGlY(curY);
            gridDrawer.drawLine(x0, y0, x1, y1, Color.BLACK, 5);
        }

        int[][] lines = {
                {5, 5, size - 5, size - 20},
                {20, 40, 30, 70},
                {40, 50, 10, size - 5},
                {10, 25, 10, 70},
                {85, 30, 50, 40}
        };
        LineDrawer lineDrawer = new LineDrawer(gridDrawer);
        Rectangle rect = new Rectangle(left, bot, right, top);
        for (int[] line : lines) {
            lineDrawer.drawLine2Colored(line[0], line[1], line[2], line[3], rect, Color.GREEN, Color.RED);
        }

//        for (int x = 0; x < gridDrawer.getFieldWidth(); x++) {
//            for (int y = 0; y < gridDrawer.getFieldHeight(); y++) {
//                if (y < bot || y > top)
//                    gridDrawer.drawRectangle(x, y, Color.BLACK);
//            }
//        }

//        drawTriangles(gridDrawer);
//        drawImages(gridDrawer);

        window.swapBuffers();
    }

    private GridDrawer drawGrid(int rows, int columns, int squareSize, int delimSize, boolean fill) {
        Color backgroundColor = Color.WHITE;
        Color pixelColor = Color.CYAN;

        GridDrawer gridDrawer = new GridDrawer(rows, columns, squareSize, delimSize, gl);
        if (fill) gridDrawer.fullRefill(pixelColor, backgroundColor);

        return gridDrawer;
    }

    private void drawTriangles(GridDrawer drawer) {
        LineDrawer lineDrawer = new LineDrawer(drawer);
        GouraudTriangleDrawer gouraudTriangleDrawer = new GouraudTriangleDrawer(lineDrawer, drawer);
        BarycentricTriangleDrawer barycentricTriangleDrawer = new BarycentricTriangleDrawer(lineDrawer, drawer);

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

        TextDrawer textDrawer = new TextDrawer(window.getWidth(), window.getHeight());
        textDrawer.draw("Gouraud method", drawer.getGlX(dx[0] + 7), drawer.getGlY(dy + 37));
        textDrawer.draw("Barycentric method", drawer.getGlX(dx[1] + 5), drawer.getGlY(dy + 37));
    }

    private void drawImages(GridDrawer drawer) {
        for (int x = 0; x < drawer.getFieldWidth(); x++)
            for (int y = 0; y < drawer.getFieldHeight() / 2; y++)
                drawer.drawRectangle(x, y, Color.WHITE);
        drawer.drawLine(-1, 0, 1, 0, Color.RED, 3);
        drawer.drawLine(0, -1, 0, 1, Color.RED, 3);

        int height = rows * scale;
        int width = columns * scale;
        drawer = drawGrid(height, width, 1, 0, false);

        Color[][] image = new ImageReader().readImage("image.bmp");

        ImageDrawer imageDrawer = new ImageDrawer(drawer);
        TextDrawer textDrawer = new TextDrawer(width, height);

        drawImage(image, 0, "Original image", drawer, imageDrawer, textDrawer);

        image = new ImageStretcher().stretchImage(image, 1.4, 0.7);
        drawImage(image, width / 2, "Stretched image", drawer, imageDrawer, textDrawer);
    }

    private void drawImage(Color[][] image, int dx, String name, GridDrawer gridDrawer, ImageDrawer imageDrawer, TextDrawer textDrawer) {
        int width = columns * scale;
        int height = rows * scale;

        int imageWidth = image.length;
        int imageHeight = image[0].length;

        int startX = dx + (width / 2 - imageWidth) / 2;
        int startY = (height / 2 - imageHeight) / 2;
        imageDrawer.drawImage(image, startX, startY);

        int textY = (height / 2 - imageHeight) / 2 + 5 + imageHeight;
        textDrawer.draw(name, gridDrawer.getGlX(width / 8 + 20 + dx), gridDrawer.getGlY(textY));
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