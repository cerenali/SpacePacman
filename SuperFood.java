import java.awt.Color;
import java.awt.Graphics;

public class SuperFood extends Food {
    private boolean hide;
    public static final int RADIUS = 8;

    public SuperFood(int x, int y) {
        super(x, y);
        hide = false;
    }

    @Override
    public void draw(Graphics g) {
        if (hide)
            return;
        int red = 255;
        int green = 252;
        int blue = 212;
        g.setColor(new Color(red, green, blue));
        g.fillArc((int) (x - RADIUS), (int) (y - RADIUS), (int) (RADIUS * 2),
            (int) (RADIUS * 2), 0, 360);
    }

    public void hide() {
        hide = true;
    }

    public void show() {
        hide = false;
    }

    public boolean isHidden() {
        return hide;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getRadius() {
        return RADIUS;
    }

}
