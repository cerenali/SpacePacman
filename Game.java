/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // Instructions window
        JFrame instrFrame = new JFrame("INSTRUCTIONS: SPACE PACMAN");
        instrFrame.setLocation(100, 100);
        instrFrame.add(new Instructions());

        instrFrame.pack();
        instrFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instrFrame.setVisible(true);

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("SPACE PACMAN");
        frame.setLocation(300, 200);

        // control panel
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new GridLayout(3, 1));
        frame.add(control_panel, BorderLayout.EAST);
        // lives
        final JLabel lives = new JLabel("Lives: 3", JLabel.CENTER);
        control_panel.add(lives);
        // score
        final JLabel score = new JLabel("Score: 0", JLabel.CENTER);
        control_panel.add(score);

        // Main playing area
        final GameCourt court = new GameCourt(score, lives);
        court.addKeyListener(court);
        frame.add(court, BorderLayout.CENTER);

        // reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /*
     * Main method run to start and run the game Initializes the GUI elements
     * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
     * this in the final submission of your game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
