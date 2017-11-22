import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class ImageReader {
    public Color[][] readImage(String fileName) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Color[][] a = new Color[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                a[x][y] = getColor(image, x, y);
        return a;
    }

    private Color getColor(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x, y);
        int b = (((rgb >>> 0) % 256) + 256) % 256;
        int g = (((rgb >>> 8) % 256) + 256) % 256;
        int r = (((rgb >>> 16) % 256) + 256) % 256;
        return new Color(r, g, b);
    }
}