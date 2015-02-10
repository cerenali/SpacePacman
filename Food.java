import java.awt.Color;
import java.awt.Graphics;

public class Food implements GameCharacter {
    public static final int RADIUS = 3;

    public int x;
    public int y;

    private boolean hide;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
        hide = false;
    }

    @Override
    public void move() {
        // food does not move.
        return;
    }

    @Override
    public void draw(Graphics g) {
        if (hide)
            return;
        int red = 252;
        int green = 255;
        int blue = 168;
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
