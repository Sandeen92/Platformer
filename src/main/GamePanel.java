package main;

import input.KeyBoardInputs;
import input.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Directions.*;

public class GamePanel extends JPanel {
    private Game game;


    public GamePanel(Game game){
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(new MouseInputs(this));
        setPanelSize();
        this.game = game;
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
