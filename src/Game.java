public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private long lastCheck = System.currentTimeMillis();
    private int frames = 0;
    public Game(){
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(400,400, gamePanel);
        gamePanel.requestFocus();
        startGameLoop();

    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0/ FPS_SET;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();

        while (true){
            now = System.nanoTime();

            if(now - lastFrame >= timePerFrame){
                gamePanel.repaint();
                lastFrame = now;
                frames++;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }

        }

    }
}
