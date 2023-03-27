

import javax.swing.*;

public class GameWindow {
    private JFrame jFrame;

    public GameWindow(int width, int heigth, GamePanel gamePanel){
        jFrame = new JFrame();
        jFrame.setSize(width,heigth);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(gamePanel);
        //Always setVisible last
        jFrame.setVisible(true);

    }

}
