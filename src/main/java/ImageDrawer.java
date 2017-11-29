import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class ImageDrawer {

    private final GridDrawer drawer;

    public ImageDrawer(GridDrawer drawer) {
        this.drawer = drawer;
    }

    public void drawImage(Color[][] image, int dx, int dy) {
        int width = image.length;
        int height = image[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int y1 = height - y - 1;
                Color color = image[x][y];
                drawer.drawRectangle(x + dx, y1 + dy, color);
            }
        }
    }
}