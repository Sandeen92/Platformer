package main;

import input.KeyBoardInputs;
import input.MouseInputs;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Game game;
    public static JLabel LBL_FPS_COUNTER = null; // Temporär FPS-counter

    public GamePanel(Game game){
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(new MouseInputs(this));

        setPanelSize();
        initFpsCounter();
        this.game = game;
    }

    private void initFpsCounter() {
        LBL_FPS_COUNTER = new JLabel();
        LBL_FPS_COUNTER.setFont(new Font("DialogInput", Font.BOLD, 25));
        LBL_FPS_COUNTER.setForeground(Color.yellow);
        add(LBL_FPS_COUNTER);
    }

    public void setPanelSize(){
        Dimension panelSize = new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT);
        setPreferredSize(panelSize);
    }


    public void updateGame(){
        //Everything to update the game goes here

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.renderEverything(g);
    }

    public Game getGame(){
        return game;
    }
}
