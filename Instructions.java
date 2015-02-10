import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Instructions extends JPanel {

    private static final int COURT_WIDTH = 500;
    private static final int COURT_HEIGHT = 800;

    private BufferedImage img;

    public Instructions() {
        try {
            if (img == null) {
                img = ImageIO.read(new File("instr.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

}
