/**
 * This class has Assistance methods for the checks in the game
 * @author Linus Magnusson
 */

package utils;

import entity.interactable.Box;
import entity.enemy.EnemyRat;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.RAT;
import static utils.Constants.GameConstants.*;
import static utils.Constants.InteractableConstants.*;

public class AssistanceMethods {

    /**
     * This method takes in the hitbox in 4 different variables and the leveldata to make checks
     * of all the 4 corners of the hitbox. The checks is to see if the player can move there.
     * @param x
     * @param y
     * @param width
     * @param heigth
     * @param levelData
     * @return
     */
    public static boolean canMoveHere(float x, float y, float width, float heigth, int[][] levelData){
        if(isTileSolid(x,y, levelData) == false){
            if(isTileSolid(x + width,y + heigth, levelData) == false){
               if(isTileSolid(x + width, y, levelData) == false){
                   if(isTileSolid(x, y + heigth, levelData) == false){
                       return true;
                   }
               }
            }
        }
        return false;
    }

    /**
     * This method is used to check if the tile asked after is a solid block or not
     * @param x
     * @param y
     * @param levelData
     * @return
     */
    private static boolean isTileSolid(float x, float y, int [][] levelData){
        int maxLevelWidth = levelData[0].length * TILES_SIZE;
        if(checkIfInsideBorder(x,y,maxLevelWidth) == true){
            return true;
        }

        float xIndex = x/TILES_SIZE;
        float yIndex = y/TILES_SIZE;
        int value = levelData[(int)yIndex][(int) xIndex];
        return checkIfValidColor(value);
    }

    /**
     * This method checks if the player is inside of the gamewindow border
     * @param x
     * @param y
     * @param maxLevelWidth
     * @return
     */
    private static boolean checkIfInsideBorder(float x, float y, int maxLevelWidth) {
        if(x < 0 || x >= maxLevelWidth){
            return true;
        }
        return y < 0 || y >= GAME_HEIGHT;
    }

    /**
     * This method checks if the value is a valid color for a tile if not returns false
     * @param value
     * @return
     */
    private static boolean checkIfValidColor(int value){
        if(value >= 48 || value <0 || value != 11){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method is used to get the entitys position closest to the wall to make
     * gaps between the entity and objects/tiles to disapear
     * @param hitbox
     * @param horizontalSpeed
     * @return
     */
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float horizontalSpeed) {
        int currentTile = (int) hitbox.x / TILES_SIZE;
        return calculatePosNextToWall(currentTile, hitbox);
    }

    /**
     * This method returns the position closest to the wall
     * @param currentTile
     * @param hitbox
     * @return
     */
    private static float calculatePosNextToWall(int currentTile, Rectangle2D.Float hitbox){
        int tileXPosition = currentTile * TILES_SIZE;
        int xOffset = (int)(TILES_SIZE - hitbox.width/2);
        return tileXPosition + xOffset - 1;
    }

    /**
     * This method is used to get the entitys position closest to the roof/floor to make
     * gaps between the entity and objects/tiles to disapear
     * @param hitbox
     * @param airSpeed
     * @return
     */
    public static float GetEntityYPosUnderOrAboveTile(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int) hitbox.y/TILES_SIZE;
        if(airSpeed > 0){
            //Falling
            return calculatePosUnderOrAboveTile(currentTile, hitbox);
        } else{
            //Jumping
            return currentTile * TILES_SIZE;
        }
    }

    /**
     * This method returns the positions closest to the ground or roof
     * @param currentTile
     * @param hitbox
     * @return
     */
    private static float calculatePosUnderOrAboveTile(int currentTile, Rectangle2D.Float hitbox){
        int tileYPosition = currentTile * TILES_SIZE;
        int yOffset = (int)(TILES_SIZE - hitbox.height/2);
        return tileYPosition + yOffset + 1;
    }

    /**
     * This method checks if the entity is currently standing on the floor
     * @param hitbox
     * @param lvlData
     * @return
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        //Check the pixel below bottom left and bottom right
        if(isTileSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData) == false){
            if(isTileSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData) == false){
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to check if the next tile is a floor tile or air
     * @param hitbox
     * @param xSpeed
     * @param lvlData
     * @return
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        if(isTileSolid(hitbox.x + xSpeed + hitbox.width, hitbox.y + hitbox.height + 1, lvlData) == true && isTileSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData) == true){
            return true;
        }
        return false;
    }


    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == 100) {
                    return calculatePlayerSpawn(i, j);
                }
            }
        }
        return null;
    }

    public static Point calculatePlayerSpawn(int pointX, int pointY){
        return new Point(pointX * TILES_SIZE, pointY * TILES_SIZE);
    }

    /**
     * This method saves the leveldata in the leveldata array
     * @param img
     * @return
     */
    public static int[][] GetLevelData(BufferedImage img) {
            int[][] levelData = new int[img.getHeight()][img.getWidth()];
            for (int j = 0; j < img.getHeight(); j++)
                for (int i = 0; i < img.getWidth(); i++) {
                    Color color = new Color(img.getRGB(i, j));
                    int value = color.getRed();
                    if (value >= 48)
                        value = 0;
                    levelData[j][i] = value;
                }
            return levelData;
        }

    /**
     * this method gets the enemies from the leveldata and returns the arraylist
     * @param img
     * @return
     */
    public static ArrayList<EnemyRat> GetRats(BufferedImage img) {
        ArrayList<EnemyRat> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == RAT) {
                    list.add(new EnemyRat(i * TILES_SIZE, j * TILES_SIZE));
                }
            }
        }
        return list;
    }

    /**
     * This method reads the boxes from the leveldata and returns as an arraylist
     * @param img
     * @return
     */
    public static ArrayList<Box> GetBoxes(BufferedImage img){
        ArrayList<Box> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 55) {
                    list.add(new Box(i * TILES_SIZE, j * TILES_SIZE,(int) BOX_WIDTH, (int) BOX_HEIGHT));
                }
            }
        }
        return list;
    }

}
