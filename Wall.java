import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Wall {
    public static final int STROKE = 5;
    public int x;
    public int y;
    public int w;
    public int h;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(STROKE));
        g.fillRect(x, y, w, h);

    }
}
