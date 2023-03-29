package main;

import entity.Player;

import java.awt.*;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private int frames = 0;
    private Player player;

    public Game(){
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        player = new Player(200,200, 128,80);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void updateEverything(){
        gamePanel.updateGame();
        player.updatePlayer();
    }
    public void renderEverything(Graphics g){
        player.renderPlayer(g);
    }


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
                System.out.println("FPS: " + frames + " UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }

    }
    public void windowFocusLost(){
        player.allMovingBooleansFalse();
    }
    public Player getPlayer(){
        return player;
    }
}
