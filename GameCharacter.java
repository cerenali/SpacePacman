import java.awt.Graphics;

public interface GameCharacter {
    int RADIUS = 0;

    public void move();

    public void draw(Graphics g);

    public int getX();

    public int getY();

    public int getRadius();
}
