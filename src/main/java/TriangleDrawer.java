import java.awt.*;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public abstract class TriangleDrawer {
    protected final LineDrawer lineDrawer;
    protected static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};

    public TriangleDrawer(LineDrawer lineDrawer) {
        this.lineDrawer = lineDrawer;
    }

    private Random rnd = new Random(239);
    protected void shuffleColors() {
//        if (true) return;
        for (int i = 0; i < 3; i++) {
            int j = rnd.nextInt(i + 1);
            Color c = colors[i];
            colors[i] = colors[j];
            colors[j] = c;
        }
    }

    public abstract void drawTriangle(int[][] triangle, int dx, int dy);
}