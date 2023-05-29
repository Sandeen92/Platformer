package userinterface;


import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * The HealthBar class represents a health bar component that displays the current health
 * status of a character.
 * @author Simon Sandén
 */
public class HealthBar {

    private int xPos;
    private int yPos;
    private int imgWidth;
    private int imgHeight;
    private int rowIndex;
    private BufferedImage[] images;


    public HealthBar() {
        initVariables();
        loadImages();
    }

    /**
     * Initializes the variables for the health bar.
     * Sets the default values for position, size, and row index.
     */
    private void initVariables() {
        rowIndex = 0;
        xPos = 15;
        yPos = 15;
        imgWidth = 408;
        imgHeight = 102;
    }

    /**
     * Loads the health bar images from a sprite atlas.
     * Slices the atlas into subimages representing each health bar state.
     */
    public void loadImages() {
        images = new BufferedImage[11];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.HEALTHBAR_ATLAS);

        for (int i = 0; i < 11; i++) {
            int y = i * 180;
            if (y + 180 > img.getHeight()) {
                break;
            }
            images[i] = img.getSubimage(0, y, 720, 180);
        }
    }

    /**
     * Updates the current health state of the health bar.
     * Adjusts the row index based on the given health value.
     *
     * @param health the current health value
     */
    public void updateCurrentHealth(int health) {
        rowIndex = 10 - health;
    }

    /**
     * Draws the health bar on the specified graphics context.
     *
     * @param g the graphics context to draw on
     */
    public void draw(Graphics g) {
        g.drawImage(images[rowIndex], xPos, yPos, imgWidth, imgHeight, null);
    }


    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}