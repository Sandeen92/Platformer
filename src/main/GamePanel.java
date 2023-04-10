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
    private BufferedImage backgroundImage;
    private Game game;


    public GamePanel(Game game){
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(new MouseInputs(this));
        setPanelSize();
        this.game = game;
        //importBackGroundImage();

    }

    public void setPanelSize(){
        Dimension panelSize = new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT);
        setPreferredSize(panelSize);
    }

    private void importBackGroundImage() {
        InputStream is = getClass().getResourceAsStream("/DEMO_BG.png");

        try {
            backgroundImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGame(){
        //Everything to update the game goes here

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
        game.renderEverything(g);
    }

    public Game getGame(){
        return game;
    }
}
