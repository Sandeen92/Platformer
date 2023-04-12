/**
 * This class is responsible for the logic through the entire game
 * @author Linus Magnusson
 * @author Simon Sandén
 * @author Casper Johannesson
 */

package main;

import gamestates.*;

import java.awt.*;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private int frames = 0;
    private Playing playing;
    private Startmenu startmenu;
    private Pausemenu pausemenu;
    private Options options;
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    /**
     * Constructor for Game, initializes classes and starts the gameloop
     */
    public Game(){
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    /**
     * This method initializes classes
     */
    private void initClasses() {
        startmenu = new Startmenu(this);
        playing = new Playing(this);
        pausemenu = new Pausemenu(this,playing);
        options = new Options(this);
    }

    /**
     * This method creates the gameloop thread and starts the gameloop
     */
    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * This method is responsible for deciding what to update based on the current
     * game state and then updating it
     */
    public void updateEverything(){

        switch (Gamestate.state){

            case PLAYING:
                playing.update();
                break;

            case MENU:
                startmenu.update();
                break;

            case PAUSE:
                pausemenu.update();
                break;

            case OPTIONS:
                options.update();
                break;

            case QUIT:
            default:
                System.exit(0);
                break;

        }
    }
    /**
     * This method is responsible for deciding what to render based on the current
     * game state and then rendering it
     */
    public void renderEverything(Graphics g){

        switch (Gamestate.state){

            case PLAYING:
                playing.draw(g);
                break;

            case MENU:
                startmenu.draw(g);
                break;

            case PAUSE:
                pausemenu.draw(g);
                break;

            case OPTIONS:
                options.draw(g);
                break;

            default:
                break;

        }
    }

    /**
     * This method gets called when the thread is started and is responsible for keeping
     * the game at 120FPS and 200Ups and correcting the updates if they are lower
     * this is to prevent lag
     */
    @Override
    public void run() {
        double timePerFrame = 1000000000.0/ FPS_SET;
        double timePerUpdate = 1000000000.0/ UPS_SET;

        long previousTime = System.nanoTime();
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true){
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime)/timePerUpdate;
            deltaF += (currentTime - previousTime)/timePerFrame;
            previousTime = currentTime;
            if(deltaU >= 1){
                updateEverything();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                GamePanel.LBL_FPS_COUNTER.setText("FPS: " + frames + " UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }

    }

    /**
     * This method is responsible for stopping the game if windowsfocus is lost
     */
    public void windowFocusLost(){
        if (Gamestate.state == Gamestate.PLAYING){
            playing.getPlayer().allMovingBooleansFalse();
        }
    }

    public Startmenu getMenu(){
        return startmenu;
    }

    public Playing getPlaying(){
        return playing;
    }

    public Pausemenu getPausemenu(){return pausemenu;}
}
