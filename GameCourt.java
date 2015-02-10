/**
 * CIS 120 HW10
 * (c) Alice Ren
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel implements KeyListener {
    // the state of the game logic
    private Pacman pacman; // the pacman, keyboard-controlled
    private ArrayList<Wall> walls;
    private ArrayList<Food> foods;
    private ArrayList<Ghost> ghosts;
    private Timer timer;

    public boolean playing = false; // whether the game is running
    private int lives;
    private int score;
    private JLabel livesLbl;
    private JLabel scoreLbl;
    private boolean won;

    // arrow keys pressed
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    // Game constants
    public static final int COURT_WIDTH = 420;
    public static final int COURT_HEIGHT = 420;
    public static final int WALL_THICKNESS = 15;
    public static final int TILE_LENGTH = 30;
    public static final int WALL_LENGTH = 120;
    public static final int WALL_OFFSET = 60;

    public static final int NUM_DOTS = 120;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 15;

    // creates new instances of Wall at specific points and adds
    // them to the walls<Wall> ArrayList, to be drawn in a different method.
    private void initializeWalls() {
        Wall leftWallTop = new Wall(2 * TILE_LENGTH, 2 * TILE_LENGTH,
            TILE_LENGTH, TILE_LENGTH);
        walls.add(leftWallTop);
        Wall rightWallTop = new Wall(COURT_HEIGHT - 3 * TILE_LENGTH,
            2 * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
        walls.add(rightWallTop);

        Wall leftWallBottom = new Wall(2 * TILE_LENGTH, COURT_HEIGHT - 3
            * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
        walls.add(leftWallBottom);
        Wall rightWallBottom = new Wall(COURT_HEIGHT - 3 * TILE_LENGTH,
            COURT_HEIGHT - 3 * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
        walls.add(rightWallBottom);

        Wall centerWallTop1 = new Wall(6 * TILE_LENGTH, 2 * TILE_LENGTH,
            2 * TILE_LENGTH, TILE_LENGTH);
        walls.add(centerWallTop1);
        Wall centerWallTop2 = new Wall(5 * TILE_LENGTH, 3 * TILE_LENGTH,
            4 * TILE_LENGTH, TILE_LENGTH);
        walls.add(centerWallTop2);

        Wall bottomWallLeft = new Wall(5 * TILE_LENGTH, COURT_HEIGHT - 4
            * TILE_LENGTH, TILE_LENGTH, 2 * TILE_LENGTH);
        walls.add(bottomWallLeft);
        Wall bottomWallRight = new Wall(8 * TILE_LENGTH, COURT_HEIGHT - 4
            * TILE_LENGTH, TILE_LENGTH, 2 * TILE_LENGTH);
        walls.add(bottomWallRight);

        Wall sideWallLeft1 = new Wall(2 * TILE_LENGTH, 5 * TILE_LENGTH,
            TILE_LENGTH, 4 * TILE_LENGTH);
        walls.add(sideWallLeft1);

        Wall sideWallRight1 = new Wall(COURT_WIDTH - 3 * TILE_LENGTH,
            5 * TILE_LENGTH, TILE_LENGTH, 4 * TILE_LENGTH);
        walls.add(sideWallRight1);

        // add invisible wall
        Wall invisibleGhostBoxWall = new InvisibleGhostBoxWall(5 * TILE_LENGTH,
            6 * TILE_LENGTH, TILE_LENGTH * 4 + TILE_LENGTH / 3, 2 * TILE_LENGTH
                + TILE_LENGTH / 3);
        walls.add(invisibleGhostBoxWall);
        // change this to an actual ghost box
        Wall ghostBoxTopLeft = new GhostBoxWall(5 * TILE_LENGTH,
            6 * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH / 3);
        walls.add(ghostBoxTopLeft);
        Wall ghostBoxTopRight = new GhostBoxWall(COURT_WIDTH - 6 * TILE_LENGTH
            + TILE_LENGTH / 3, 6 * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH / 3);
        walls.add(ghostBoxTopRight);
        Wall ghostBoxLeft = new GhostBoxWall(5 * TILE_LENGTH, 6 * TILE_LENGTH,
            TILE_LENGTH / 3, 2 * TILE_LENGTH);
        walls.add(ghostBoxLeft);
        Wall ghostBoxRight = new GhostBoxWall(COURT_WIDTH - 5 * TILE_LENGTH,
            6 * TILE_LENGTH, TILE_LENGTH / 3, 2 * TILE_LENGTH);
        walls.add(ghostBoxRight);
        Wall ghostBoxBottom = new GhostBoxWall(5 * TILE_LENGTH,
            8 * TILE_LENGTH, 4 * TILE_LENGTH + TILE_LENGTH / 3, TILE_LENGTH / 3);
        walls.add(ghostBoxBottom);
    }

    public void initializeFoods() {
        // top and bottom line of foods
        for (int i = 1; i <= 13; i++) {
            foods.add(new Food(i * TILE_LENGTH, TILE_LENGTH));
            foods.add(new Food(i * TILE_LENGTH, 13 * TILE_LENGTH));
        }
        // left and right line of foods
        for (int i = 2; i < 13; i++) {
            foods.add(new Food(TILE_LENGTH, i * TILE_LENGTH));
            foods.add(new Food(13 * TILE_LENGTH, i * TILE_LENGTH));
        }

        // inner left and right lines
        for (int i = 2; i < 13; i++) {
            foods.add(new Food(4 * TILE_LENGTH, i * TILE_LENGTH));
            foods.add(new Food(10 * TILE_LENGTH, i * TILE_LENGTH));
        }
        // middle bottom line
        for (int i = 10; i <= 12; i++) {
            foods.add(new Food(7 * TILE_LENGTH, i * TILE_LENGTH));
        }

        // top and bottom of ghostbox
        for (int i = 5; i <= 9; i++) {
            foods.add(new Food(i * TILE_LENGTH, 5 * TILE_LENGTH));
            if (i != 7)
                foods.add(new Food(i * TILE_LENGTH, 9 * TILE_LENGTH));
        }

        // top left and bottom left corners
        for (int i = 2; i <= 3; i++) {
            foods.add(new Food(i * TILE_LENGTH, 4 * TILE_LENGTH));
            foods.add(new Food(i * TILE_LENGTH, 10 * TILE_LENGTH));
        }
        // top right and bottom right corners
        for (int i = 11; i <= 12; i++) {
            foods.add(new Food(i * TILE_LENGTH, 4 * TILE_LENGTH));
            foods.add(new Food(i * TILE_LENGTH, 10 * TILE_LENGTH));
        }

        // super foods
        foods.add(new SuperFood(5 * TILE_LENGTH, 2 * TILE_LENGTH));
        foods.add(new SuperFood(9 * TILE_LENGTH, 2 * TILE_LENGTH));
    }

    public GameCourt(JLabel lives, JLabel score) {
        up = false;
        down = false;
        left = false;
        right = false;

        won = false;

        this.livesLbl = lives;
        this.scoreLbl = score;
        this.lives = 3;
        this.score = 0;

        pacman = new Pacman(7 * TILE_LENGTH, 9 * TILE_LENGTH);
        walls = new ArrayList<Wall>();
        foods = new ArrayList<Food>();
        ghosts = new ArrayList<Ghost>();

        // walls!
        initializeWalls();

        // food/dot things -- for now, randomly generate + distribute?
        initializeFoods();

        // ghosties
        Ghost g1 = new Ghost("redghost.png", COURT_WIDTH / 2, COURT_HEIGHT / 2);
        ghosts.add(g1);

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.BLACK);

        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
        timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key
        // events will be handled by its key listener.
        setFocusable(true);
    }

    void frighten() {
        // make ghosts blue
        for (Ghost gh : ghosts) {
            gh.setFrightened();
        }
        Timer ftimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Ghost gh : ghosts) {
                    gh.setNotFrightened();
                }
            }
        });
        ftimer.setInitialDelay(5000);
        ftimer.setRepeats(false);
        ftimer.start();
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        up = false;
        down = false;
        left = false;
        right = false;
        pacman = new Pacman(7 * TILE_LENGTH, 9 * TILE_LENGTH);
        playing = true;
        lives = 3;
        score = 0;
        livesLbl.setText("Lives: 3");
        scoreLbl.setText("Score: 0");
        if (!timer.isRunning())
            timer.start();

        for (Food f : foods)
            f.show();

        for (Ghost gh : ghosts)
            gh.reset();

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    void checkIfWon() {
        boolean allHidden = true;
        for (Food f : foods) {
            if (!f.isHidden()) {
                allHidden = false;
            }
        }
        if (allHidden) {
            won = true;
            gameOver();
        }
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {

            if (lives <= 0) {
                won = false;
                gameOver();
            }

            checkIfWon();

            if (pacman.shouldReset) {
                Timer pause = new Timer(1, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        resetAfterDeath();
                    }

                });
                pause.setRepeats(false);
                pause.setInitialDelay(1000); // 1 second delay while u cry in
                                             // despair and re-evaluate all your
                                             // life decisions to date
                pause.start();
            }

            if (!pacman.dying) {
                pacman.update();
                pacman.move();

                for (Ghost gh : ghosts) {
                    gh.update(pacman.x, pacman.y);
                    gh.move();
                }

                collide();
            }

            // update the display
            repaint();
        }
    }

    void gameOver() {
        timer.stop();
        playing = false;
    }

    void resetAfterDeath() {
        pacman.dying = false;
        pacman = new Pacman(7 * TILE_LENGTH, 9 * TILE_LENGTH);

        for (Ghost gh : ghosts) {
            gh.reset();
        }
        pacman.shouldReset = false;
    }

    void collide() {
        // pacman should bounce off walls
        for (Wall wall : walls) {
            if (isWithin(pacman, wall.x, wall.y, wall.x + wall.w, wall.y
                + wall.h)
                && !(wall instanceof GhostBoxWall)) {
                pacman.bounce();
            }

            for (Ghost gh : ghosts) {
                // pacman and ghost intersect
                if (isWithin(pacman, gh.getX() - Ghost.WIDTH / 2, gh.getY()
                    - Ghost.WIDTH / 2, gh.getX() + Ghost.WIDTH / 2, gh.getY()
                    + Ghost.WIDTH / 2)) {
                    if (gh.isFrightened()) {
                        // pacman eats ghost, ghost should die
                        gh.die();
                        score += 20;
                        scoreLbl.setText("Score: " + score);
                    } else { // pacman dies
                        pacman.die();
                        gh.stop();
                        lives--;
                        livesLbl.setText("Lives: " + lives);
                        return;
                    }
                }

                // ghosts should also bounce off walls (but not ghostboxwalls)
                if (wall instanceof InvisibleGhostBoxWall)
                    continue;
                if (isWithin(gh, wall.x, wall.y, wall.x + wall.w, wall.y
                    + wall.h)) {
                    gh.bounce(wall.x, wall.y, wall.x + wall.w, wall.y + wall.h);
                }
            }
        }

        // pacman should eat all the foods
        for (Food f : foods) {
            double dx = Math.abs(f.x - pacman.x);
            double dy = Math.abs(f.y - pacman.y);
            double d = Math.sqrt(dx * dx + dy * dy);
            if (((d < Pacman.RADIUS - (Food.RADIUS / 2) && !(f instanceof SuperFood)))
                || ((f instanceof SuperFood) && d < Pacman.RADIUS
                    - (SuperFood.RADIUS / 2))) {
                if (f instanceof SuperFood && !f.isHidden()) {
                    frighten();
                }
                if (!f.isHidden()) {
                    f.hide(); // set hide = true;
                    score++;
                    scoreLbl.setText("Score: " + score);
                }
            }
        }
    }

    boolean isWithin(GameCharacter c, int x1, int y1, int x2, int y2) {
        return (((c.getX() + c.getRadius() + Pacman.THRESHOLD > x1)
            && (c.getX() - c.getRadius() - Pacman.THRESHOLD < x2)
            && (c.getY() + c.getRadius() + Pacman.THRESHOLD > y1) && (c.getY()
            - c.getRadius() - Pacman.THRESHOLD < y2)));
    }

    @Override
    public void paintComponent(Graphics g) {
        // game over screen
        if (!playing) {
            g.fillRect(0, 0, COURT_WIDTH, COURT_HEIGHT);
            Color c = g.getColor();
            g.setColor(Color.WHITE);
            String status = new String();
            if (won)
                status = "YOU WON :)";
            else
                status = "YOU LOST :(";

            g.drawString(status, COURT_WIDTH / 2 - TILE_LENGTH,
                COURT_HEIGHT / 2);
            g.setColor(c);
            return;
        }

        super.paintComponent(g);

        pacman.draw(g);

        for (Food f : foods) {
            f.draw(g);
        }

        for (Wall w : walls) {
            w.draw(g);
        }

        for (Ghost gh : ghosts) {
            gh.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_DOWN:
            down = true;
            break;
        case KeyEvent.VK_UP:
            up = true;
            break;
        case KeyEvent.VK_LEFT:
            left = true;
            break;
        case KeyEvent.VK_RIGHT:
            right = true;
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_DOWN:
            down = false;
            break;
        case KeyEvent.VK_UP:
            up = false;
            break;
        case KeyEvent.VK_LEFT:
            left = false;
            break;
        case KeyEvent.VK_RIGHT:
            right = false;
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    // Pacman is inner class of GameCourt so can share state of key booleans
    class Pacman implements GameCharacter {
        public static final double RADIUS = 15;

        public static final double SPEED = 11;

        public static final double FRICTIONFACTOR = 0.94;
        public static final double ACCELFACTOR = 0.8;

        public static final double THRESHOLD = 1;

        public static final double ROTATIONFACTOR = 0.2;

        public double x;
        public double y;
        public double vx;
        public double vy;

        private int addAngle;
        private int[] startAngles;
        private int[] addAngles;
        private int pacmanPhaseIndex;
        private int rotationAngle;

        private Timer animTimer;

        private int pacmanPhaseAtTimeOfDeath;
        public boolean dying;
        public boolean shouldReset;

        public Pacman(int x, int y) {
            this.x = x;
            this.y = y;

            vx = 0;
            vy = 0;

            rotationAngle = 0; // 0 = 3 o'clock position
            startAngles = new int[] { 0, 15, 45, 15 };
            addAngles = new int[] { 360, 330, 270, 330 };
            addAngle = 300;
            pacmanPhaseIndex = 0;
            pacmanPhaseAtTimeOfDeath = 0;

            animTimer = new Timer(100, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pacmanPhaseIndex++;
                    if (pacmanPhaseIndex > 3)
                        pacmanPhaseIndex = 0;
                }
            });
            animTimer.start();

            dying = false;
            shouldReset = false;
        }

        public void die() {
            dying = true;
            pacmanPhaseAtTimeOfDeath = pacmanPhaseIndex;
        }

        private void update() {
            if (right) {
                if (vx < SPEED) {
                    vx += ACCELFACTOR;
                }
                if (rotationAngle < 180) {
                    rotationAngle += -rotationAngle * ROTATIONFACTOR;
                } else {
                    rotationAngle += (360 - rotationAngle) * ROTATIONFACTOR;
                }
            }
            if (left) {
                if (vx > -SPEED) {
                    vx -= ACCELFACTOR;
                }
                rotationAngle += (180 - rotationAngle) * ROTATIONFACTOR;
            }
            if (up) {
                if (vy > -SPEED) {
                    vy -= ACCELFACTOR;
                }
                rotationAngle += (90 - rotationAngle) * ROTATIONFACTOR;
            }
            if (down) {
                if (vy < SPEED) {
                    vy += ACCELFACTOR;
                }
                if (rotationAngle == 0) {
                    rotationAngle = 357;
                } else if (rotationAngle < 90) {
                    // subtract towards 0
                    rotationAngle += -rotationAngle * ROTATIONFACTOR;
                } else if (rotationAngle > 270) {
                    // subtract towards 270 from 360
                    rotationAngle += (270 - rotationAngle) * ROTATIONFACTOR;
                } else { // left hemisphere; we don't need to worry
                    rotationAngle += (270 - rotationAngle) * ROTATIONFACTOR;
                }
            }
            if (rotationAngle > 360)
                rotationAngle = rotationAngle % 360;
            vx *= FRICTIONFACTOR;
            vy *= FRICTIONFACTOR;
        }

        @Override
        public void move() {
            if (x - RADIUS < 0 || x > GameCourt.COURT_WIDTH - RADIUS
                || y - RADIUS < 0 || y > GameCourt.COURT_HEIGHT - RADIUS) {
                // x = GameCourt.COURT_WIDTH + RADIUS;
                bounce();
            }

            x += vx;
            y += vy;
        }

        public void bounce() {
            // reverse velocity
            // determine what direction pacman was moving in before bounce
            // and adjust x/y pos accordingly
            if (vx != 0) {
                vx = -vx;
                x += (Math.abs(vx) / vx) * THRESHOLD;
            }
            if (vy != 0) {
                vy = -vy;
                y += (Math.abs(vy) / vy) * THRESHOLD;
            }
        }

        public void draw(Graphics g) {
            int startAngle = rotationAngle + startAngles[pacmanPhaseIndex];
            if (dying) {
                startAngle = rotationAngle
                    + startAngles[pacmanPhaseAtTimeOfDeath];
                if (addAngle <= 0) {
                    shouldReset = true;
                } else {
                    addAngle -= 5;
                    if (addAngle < 0)
                        addAngle = 0;
                }
            } else {
                // change location of "mouth" (angles of arc) based on
                // what direction pacman is going in
                addAngle = addAngles[pacmanPhaseIndex];
            }
            g.setColor(Color.YELLOW);
            g.fillArc((int) (x - RADIUS), (int) (y - RADIUS),
                (int) (RADIUS * 2), (int) (RADIUS * 2), startAngle, addAngle);
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
            return (int) RADIUS;
        }
    }
}
