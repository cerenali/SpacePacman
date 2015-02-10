import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class InvisibleGhostBoxWall extends Wall {

    public InvisibleGhostBoxWall(int x, int y, int width, int height) {
        super(x, y, width, height);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 255));
        g2.fillRect(x, y, w, h);
    }
}
