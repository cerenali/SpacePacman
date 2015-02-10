import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Ghost implements GameCharacter {
    private BufferedImage img;
    private BufferedImage origImg;
    private BufferedImage frightenedImg;
    private BufferedImage frightenedImg_white;
    private boolean frightened;
    private boolean frightened_white;
    private double origx;
    private double origy;
    private double x;
    private double y;
    private double vx;
    private double vy;

    private int bounceCount;

    private Random rand;

    private enum Phase {
        WAIT, EXIT, CHASE, SCATTER
    }

    private Phase phase;

    public static final int WIDTH = 30;
    private static final int SPEED = 2;
    private static final int MAXSPEED = 10;
    private static final int BOUNCETHRESHOLD = 4;

    private Timer startTimer;
    private Timer frightenTimer;
    private Timer phaseTimer;
    private int phaseIndex;

    private double targetx;
    private double targety;

    public Ghost(String file, int x, int y) {
        try {
            if (origImg == null) {
                origImg = ImageIO.read(new File(file));
            }
            if (frightenedImg == null) {
                frightenedImg = ImageIO.read(new File("frightenedghost.png"));
            }
            if (frightenedImg_white == null) {
                frightenedImg_white = ImageIO.read(new File(
                    "frightenedghost_white.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        img = origImg;

        frightened = false;
        frightened_white = false;

        // x and y are coordinates for center of box that contains the ghost
        origx = x;
        origy = y;
        this.x = x;
        this.y = y;
        vx = 0;
        vy = 0;
        bounceCount = 0; // if bounceCount exceeds certain number, time to find
                         // a new path
        phase = Phase.WAIT;
        // side = CollideSide.NONE;
        rand = new Random();

        phaseIndex = 0;
        phaseTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frightened)
                    phase = Phase.SCATTER;
                else if (phaseIndex < 200)
                    phase = Phase.CHASE;
                else if (phaseIndex < 400)
                    phase = Phase.SCATTER;
                else if (phaseIndex >= 400)
                    phaseIndex = 0; // reset
                phaseIndex++;
            }
        });

        startTimer = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                phase = Phase.EXIT;
                phaseTimer.start();
            }
        });
        startTimer.setInitialDelay(2000);
        startTimer.setRepeats(false);
        startTimer.start();
    }

    @Override
    public void move() {
        // bounce off boundaries of game
        if (x - WIDTH < 0 || x > GameCourt.COURT_WIDTH - WIDTH || y - WIDTH < 0
            || y > GameCourt.COURT_HEIGHT - WIDTH) {
            vx = -vx;
            vy = -vy;
        }
        if (x - WIDTH < 0)
            x = WIDTH;
        else if (x + WIDTH > GameCourt.COURT_WIDTH)
            x = GameCourt.COURT_WIDTH - WIDTH;
        if (y - WIDTH < 0)
            y = WIDTH;
        else if (y + WIDTH > GameCourt.COURT_HEIGHT)
            y = GameCourt.COURT_HEIGHT - WIDTH;

        // calculate x and y components of vector from here to targetx, targety
        // add them to x and y
        // but what if collide into wall?
        switch (phase) {
        case EXIT:
            vy = -SPEED;
            break;
        case SCATTER:
            // pick a random direction and move in it for x amount of time?
            int dir = 1;
            if (rand.nextBoolean())
                dir = -1;
            if (rand.nextBoolean()) { // go in x-dir
                vx = dir * SPEED;
            } else { // go in y-dir
                vy = dir * SPEED;
            }
            break;
        case CHASE:
            double dx = (targetx - x) / (Math.sqrt(targetx * targetx + x * x));
            double dy = (targety - y) / (Math.sqrt(targety * targety + y * y));
            if (bounceCount > BOUNCETHRESHOLD) {
                bounceCount = 0; // reset bounceCount
                // find a different path!
                // dx *= -1;
            }
            vx = 3.5 * dx;
            vy = 3.5 * dy;
            break;
        default:
            break;
        }
        if (Math.abs(vx) > MAXSPEED)
            vx = MAXSPEED * (Math.abs(vx) / vx);
        if (Math.abs(vy) > MAXSPEED)
            vy = MAXSPEED * (Math.abs(vy) / vy);
        x += vx;
        y += vy;
    }

    public void bounce(int x1, int y1, int x2, int y2) {
        // reverse velocity
        if (vx != 0) {
            vx = -vx;
            x += (Math.abs(vx) / vx) * BOUNCETHRESHOLD;
        }
        if (vy != 0) {
            vy = -vy;
            y += (Math.abs(vy) / vy) * BOUNCETHRESHOLD;
        }

        bounceCount++;
    }

    @Override
    public void draw(Graphics g) {
        if (frightened_white)
            img = frightenedImg_white;
        else if (frightened)
            img = frightenedImg;
        else
            img = origImg;
        g.drawImage(img, (int) (x - WIDTH / 2), (int) (y - WIDTH / 2), WIDTH,
            WIDTH, null);
    }

    @Override
    public int getX() {
        return (int) x;
    }

    @Override
    public int getY() {
        return (int) y;
    }

    @Override
    public int getRadius() {
        return WIDTH / 2;
    }

    public void stop() {
        vx = 0;
        vy = 0;
    }

    public void reset() {
        x = origx;
        y = origy;
        targetx = 0;
        targety = 0;

        vx = 0;
        vy = 0;

        frightened = false;
        frightened_white = false;
        img = origImg;

        phaseIndex = 0;
        phaseTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frightened || frightened_white)
                    phase = Phase.SCATTER;
                else if (phaseIndex < 200)
                    phase = Phase.CHASE;
                else if (phaseIndex < 400)
                    phase = Phase.SCATTER;
                else if (phaseIndex >= 400)
                    phaseIndex = 0; // reset
                phaseIndex++;
            }
        });
        startTimer.setInitialDelay(1000);
        startTimer.setRepeats(false);
        startTimer.start();
    }

    public void update(double targetx, double targety) {
        this.targetx = targetx;
        this.targety = targety;
    }

    public void setFrightened() {
        frightened = true;
        img = frightenedImg;
        frightenTimer = new Timer(800, new ActionListener() {
            private int counter = 0;

            public void actionPerformed(ActionEvent e) {
                frightened_white = !frightened_white;
                if (++counter == 2000) {
                    frightenTimer.stop();
                    System.out.println(counter);
                    counter = 0;
                }
            }
        });
        frightenTimer.setRepeats(false);
        frightenTimer.setInitialDelay(3000);
        frightenTimer.start();
    }

    public boolean isFrightened() {
        return frightened;
    }

    public void setNotFrightened() {
        frightened = false;
        frightened_white = false;
        img = origImg;
        frightenTimer.stop();
    }

    public void die() {
        reset();
    }

}
