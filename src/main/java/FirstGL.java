import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

import java.util.Random;

public class FirstGL implements GLEventListener {

    private GLWindow window;
    private GL2 gl;
    private Animator animator;
    private Random rnd = new Random(239);

    public static void main(String[] args) {
        new FirstGL().setup();
    }

    private void setup() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);
        window.setSize(1024, 768);
        window.setVisible(true);
        window.setAutoSwapBufferMode(false);
        gl = window.getGL().getGL2();

        window.addGLEventListener(this);

        animator = new Animator(window);
        animator.start();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
                System.exit(1);
            }
        });

        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseMoved = true;
                mouseX = normalizeCoord(e.getX(), window.getWidth());
                mouseY = normalizeCoord(e.getY(), window.getHeight());
                System.err.println(e.getX() + " " + e.getY());
            }
        });

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_1) mode = 1;
                if (e.getKeyCode() == KeyEvent.VK_2) mode = 2;
                if (e.getKeyCode() == KeyEvent.VK_3) mode = 3;
                mouseMoved = true;
            }
        });
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if (mode == 1) drawSinus();
        if (mode == 2) drawTriangles();
        if (mode == 3) drawFire();
    }

    private int mode = 1;
    private boolean mouseMoved;
    private double mouseX, mouseY;

    //mode 3
    private void drawFire() {
        if (!mouseMoved) return;
        mouseMoved = false;

        clear();
        gl.glEnable(GL2.GL_LINE_STIPPLE);
        for (int i = 0; i < 10; i++) {
            double x1 = randomCoord() / 2;
            double y1 = randomCoord() / 2;
            double x2 = -x1;
            double y2 = -y1;

            randomizeColor();
            gl.glLineStipple(rnd.nextInt(5) + 1, (short) rnd.nextInt());

            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2d(x1 + mouseX, y1 - mouseY);
            gl.glVertex2d(x2 + mouseX, y2 - mouseY);
            gl.glEnd();
        }
        gl.glDisable(GL2.GL_LINE_STIPPLE);
        window.swapBuffers();
    }

    //mode 2
    private void drawTriangles() {
        if (!mouseMoved) return;
        mouseMoved = false;

        clear();
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        for (int i = 0; i < 15; i++) {
            double x = randomCoord();
            double y = randomCoord();
            gl.glVertex2d(x, y);
            randomizeColor();
        }
        gl.glEnd();
        window.swapBuffers();
    }

    //mode 1
    private void drawSinus() {
        int periodCount = 5;
        double step = getStep();
        int vertexCount = (int) (periodCount * 2 * Math.PI / step);

        clear();
        gl.glLineWidth(5f);
//        if (rnd.nextInt(30) == 0)
//            randomizeColor();
        gl.glBegin(GL2.GL_LINE_STRIP);
        for (int i = 0; i < vertexCount; i++) {
            double x = (i * step) / (periodCount * Math.PI) - 1;
            double y = Math.sin(i * step) * 0.5;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
        window.swapBuffers();
    }

    double getStep() {
        if (true) return 0.01;
        return (rnd.nextInt(100) + 1) / 100.0;
    }

    private void clear() {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    }

    private double randomCoord() {
        return rnd.nextDouble() * 2 - 1;
    }

    private double normalizeCoord(double value, double windowLength) {
        windowLength /= 2;
        return (value - windowLength) / windowLength;
    }

    private void randomizeColor() {
        double red = rnd.nextDouble();
        double green = rnd.nextDouble();
        double blue = rnd.nextDouble();
        gl.glColor3d(red, green, blue);
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