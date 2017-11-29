import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class ImageStretcher {

    public Color[][] stretchImage(Color[][] image, double coefX, double coefY) {
        image = stretchImageByX(image, coefX);
        image = stretchImageByY(image, coefY);
        return image;
    }

    public Color[][] stretchImageByX(Color[][] image, double coef) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;

        int newImageWidth = (int) (imageWidth * coef);
        Color[][] newImage = new Color[newImageWidth][imageHeight];

        int l = 0;
        for (int x = 0; x < newImageWidth; x++) {
            while (l + 2 < imageWidth && (l + 1.) / imageWidth * newImageWidth < x) l++;
            double x0 = l / (double) imageWidth * newImageWidth;
            double x1 = (l + 1.) / imageWidth * newImageWidth;
            double k = 1 - (x - x0) / (x1 - x0);
            for (int y = 0; y < imageHeight; y++) {
                Color c1 = image[l][y];
                Color c2 = image[l + 1][y];
                Color newColor = sumColors(c1, c2, k);
                newImage[x][y] = newColor;
            }
        }

        return newImage;
    }

    public Color[][] stretchImageByY(Color[][] image, double coef) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;

        int newImageHeight = (int) (imageHeight * coef);
        Color[][] newImage = new Color[imageWidth][newImageHeight];

        int l = 0;
        for (int y = 0; y < newImageHeight; y++) {
            while (l + 2 < imageHeight && (l + 1.) / imageHeight * newImageHeight < y) l++;
            double y0 = l / (double) imageHeight * newImageHeight;
            double y1 = (l + 1.) / imageHeight * newImageHeight;
            double k = 1 - (y - y0) / (y1 - y0);
            for (int x = 0; x < imageWidth; x++) {
                Color c1 = image[x][l];
                Color c2 = image[x][l + 1];
                Color newColor = sumColors(c1, c2, k);
                newImage[x][y] = newColor;
            }
        }

        return newImage;
    }

    private Color sumColors(Color c1, Color c2, double k1) {
        double k2 = 1 - k1;
        int r = (int) Math.round(c1.getRed()   * k1 + c2.getRed()   * k2);
        int g = (int) Math.round(c1.getGreen() * k1 + c2.getGreen() * k2);
        int b = (int) Math.round(c1.getBlue()  * k1 + c2.getBlue()  * k2);
        int a = (int) Math.round(c1.getAlpha() * k1 + c2.getAlpha() * k2);
        return new Color(r, g, b, a);
    }
}