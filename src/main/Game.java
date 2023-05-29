/**
 * This class is responsible for the logic through the entire game
 * @author Linus Magnusson
 * @author Simon Sandén
 * @author Casper Johannesson
 */

package main;
//Imports from within project
import gamestates.*;
import gamestates.DeathScreen;
import gamestates.Gamestate;
import gamestates.Playing;
//Imports from Javas library
import java.awt.*;
//Imports of static variables and methods
import static main.GamePanel.LBL_TIMER_TEXT;
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
    private LevelCompleted levelCompleted;
    private long timerStart;
    private long timerFinished;
    private RoundTimer roundTimer;


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
        levelCompleted = new LevelCompleted(this);
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

            case LEVELCOMPLETED:
                levelCompleted.update();
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

            case LEVELCOMPLETED:
                levelCompleted.draw(g);
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

    /**
     * This method starts the round timer.
     */
    public void startRoundTimer() {
        timerStart = System.currentTimeMillis();
        roundTimer = new RoundTimer();
        roundTimer.start();
    }

    /**
     * This method stops the round timer.
     */
    public void stopRoundTimer() {
        roundTimer.stopTimer();
        calculateRoundTime(timerStart);
    }


    /**
     * This method formats the round time from millis into a readable String, it then updates the round time label.
     * @param start
     */
    private void calculateRoundTime(long start) {
        long now = System.currentTimeMillis() - start;
        long millis = now % 1000;
        long x = now / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        LBL_TIMER_TEXT.setText(String.format("%02d:%02d:%03d", minutes, seconds, millis));
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
    public LevelCompleted getLevelCompleted() {
        return levelCompleted;
    }


    /**
     * Inner class with a thread that updates the round timer.
     */
    private class RoundTimer extends Thread {
        private boolean isRunning = true;
        public void stopTimer() {
            isRunning = false;
        }
        public void run() {
            long start = System.currentTimeMillis();
            while (isRunning) {
                calculateRoundTime(start);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
