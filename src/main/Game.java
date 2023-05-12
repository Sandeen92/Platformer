/**
 * This class is responsible for the logic through the entire game
 * @author Linus Magnusson
 * @author Simon Sandén
 * @author Casper Johannesson
 */

package main;

import gamestates.*;
import gamestates.DeathScreen;
import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.*;
import java.awt.event.KeyEvent;

import static utils.Constants.GameConstants.*;

public class Game implements Runnable{
    //GUI
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    //Variables for gameloop
    private Thread gameThread;
    private long previousTime = System.nanoTime();
    private long lastCheck = System.currentTimeMillis();
    private double timePerFrame = NANO_SECOND / FPS_SET;
    private double timePerUpdate = NANO_SECOND/ UPS_SET;
    private long currentTime = System.nanoTime();
    private int updates = 0;
    private double deltaU = 0;
    private double deltaF = 0;
    private int frames = 0;
    //Gamestates
    private Playing playing;
    private DeathScreen deathScreen;
    private Startmenu startmenu;
    private Pausemenu pausemenu;
    private Options options;


    /**
     * Constructor for Game, initializes classes and starts the gameloop
     */
    public Game(){
        initialiseClasses();
        gamePanel.requestFocus();
        startGameLoop();
    }

    /**
     * This method initializes classes
     */
    private void initialiseClasses() {
        startmenu = new Startmenu(this);
        playing = new Playing(this);
        deathScreen = new DeathScreen(this);
        pausemenu = new Pausemenu(this,playing);
        options = new Options(this);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
    }

    /**
     * This method calls the restartGame from playing
     */
    public void restartGame(){
        playing.restartGame();
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
    public void update(){

        switch (Gamestate.state){

            case PLAYING:
                playing.update();
                break;

            case STARTMENU:
                startmenu.update();
                break;

            case PAUSEMENU:
                pausemenu.update();
                break;

            case DEATHSCREEN:
                deathScreen.update();
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
    public void draw(Graphics g){

        switch (Gamestate.state){

            case PLAYING:
                playing.draw(g);
                break;

            case STARTMENU:
                startmenu.draw(g);
                break;

            case PAUSEMENU:
                pausemenu.draw(g);
                break;

            case OPTIONS:
                options.draw(g);
                break;

            case DEATHSCREEN:
                deathScreen.draw(g);
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
        while(true){
            checkDeltaValuesAndCalculate();

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                //GamePanel.LBL_FPS_COUNTER.setText("FPS: " + frames + " UPS: " + updates);
                if (Gamestate.state != Gamestate.STARTMENU) {
                    GamePanel.LBL_PLAYER_HP.setText("Player HP: " + playing.getPlayer().getCurrentHealth() + " |");
                    GamePanel.LBL_INFO.setText("Press R to reset");
                }
                frames = 0;
                updates = 0;
            }
        }
    }

    /**
     * This method calculates the time elapsed between updates and frames, and updates the game if necessary.
     * Called every frame to ensure the game runs smoothly at a consistent speed.
     */

    private void checkDeltaValuesAndCalculate(){
        calculateTimeAndAddToDeltas();

        if(deltaU >= 1){
            update();
            updates++;
            deltaU--;
        }

        if(deltaF >= 1){
            gamePanel.repaint();
            frames++;
            deltaF--;
        }
    }

    /**
     * This method assigns current time to variable and calculates time passed for each update.
     */

    private void calculateTimeAndAddToDeltas(){
        currentTime = System.nanoTime();
        deltaU += (currentTime - previousTime)/timePerUpdate;
        deltaF += (currentTime - previousTime)/timePerFrame;
        previousTime = currentTime;
    }



    /**
     * This method is responsible for stopping the game if window focus is lost
     */
    public void windowFocusLost(){
        if (Gamestate.state == Gamestate.PLAYING){
            playing.getPlayer().allMovingBooleansFalse();
        }
    }

    public static void setPreviousGamestate(){
        Gamestate.previousState = Gamestate.state;
    }
    public Startmenu getStartmenu(){
        return startmenu;
    }
    public Playing getPlaying(){
        return playing;
    }
    public Pausemenu getPausemenu(){return pausemenu;}
    public Options getOptions(){return options;}
    public DeathScreen getDeathScreen(){return deathScreen;}
}
