import com.jogamp.opengl.GL2;

import java.awt.Color;

@SuppressWarnings("WeakerAccess")
public abstract class Drawer {

    protected Rectangle[][] field;
    protected GL2 gl;

    public Drawer(Rectangle[][] field, GL2 gl) {
        this.field = field;
        this.gl = gl;
    }

    protected void drawRectangle(Rectangle r, Color color) {
        setColor(color);
        r.setColor(color);
        gl.glRectd(r.x1(), r.y1(), r.x2(), r.y2());
    }

    protected void setColor(Color color) {
        gl.glColor4ub(
                (byte) color.getRed(),
                (byte) color.getGreen(),
                (byte) color.getBlue(),
                (byte) color.getAlpha());
    }

    protected Color addVectorizedColor(Color start, Color end, double mul) {
        int r = (int) Math.round(start.getRed()   + (end.getRed()   - start.getRed())   * mul);
        int g = (int) Math.round(start.getGreen() + (end.getGreen() - start.getGreen()) * mul);
        int b = (int) Math.round(start.getBlue()  + (end.getBlue()  - start.getBlue())  * mul);
        int a = (int) Math.round(start.getAlpha() + (end.getBlue()  - start.getAlpha()) * mul);
        return new Color(r, g, b, a);
    }
}