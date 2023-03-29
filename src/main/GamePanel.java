package main;

import input.KeyBoardInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {
    private float xDelta = 0;
    private float yDelta = 0;
    private BufferedImage backgroundImage;

    public GamePanel(){
        addKeyListener(new KeyBoardInputs(this));
        setPanelSize();
        importBackGroundImage();
    }


    public void setPanelSize(){
        Dimension panelSize = new Dimension(1280, 800);
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
        //TODO remove later
        g.drawImage(backgroundImage, 0, 0, null);
        g.setColor(Color.BLUE);
        g.fillRect(50+(int)xDelta,598+(int)yDelta,64,64);
    }

    public void changeYDelta(int change){
        yDelta+=change;
    }

    public void changeXDelta(int change){
        xDelta+=change;
    }
}
