package userinterface;


import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.GameConstants.GAME_WIDTH;

public class HealthBar {

    private int xPos;
    private int yPos;
    private int imgWidth;
    private int imgHeight;
    private int rowIndex;
    private BufferedImage[] images;


    public HealthBar(){
        initVariables();
        loadImages();
    }

    private void initVariables(){
        rowIndex = 0;
        xPos = 15;
        yPos = 15;
        imgWidth = 408;
        imgHeight = 102;
    }


    public void loadImages(){
        images = new BufferedImage[11];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.HEALTHBAR_ATLAS);

        for (int i = 0; i < 11; i++){
            int y = i * 180;
            if (y + 180 > img.getHeight()){
                break;
            }
            images[i] = img.getSubimage(0, y, 720, 180);
        }
    }

    public void updateCurrentHealth(int health){

        //Finns säkert massvis med bättre sätt att sköta detta på, just nu hårdkodar jag skiten bara

        switch (health){
            case 10:
                rowIndex = 0;
                break;
            case 9:
                rowIndex = 1;
                break;
            case 8:
                rowIndex = 2;
                break;
            case 7:
                rowIndex = 3;
                break;
            case 6:
                rowIndex = 4;
                break;
            case 5:
                rowIndex = 5;
                break;
            case 4:
                rowIndex = 6;
                break;
            case 3:
                rowIndex = 7;
                break;
            case 2:
                rowIndex = 8;
                break;
            case 1:
                rowIndex = 9;
                break;
            case 0:
                rowIndex = 10;
                break;
        }
    }

    public void draw(Graphics g){
        g.drawImage(images[rowIndex], xPos, yPos, imgWidth, imgHeight, null);
    }


    public void setRowIndex(int rowIndex){
        this.rowIndex = rowIndex;
    }
}
