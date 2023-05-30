/**
 * This class has Assistance methods for the checks in the game
 * @author Linus Magnusson
 */

package utils;
//Imports from within project
import entity.enemy.Seagull;
import entity.enemy.StaticEnemy;
import entity.interactable.Box;
import entity.enemy.EnemyRat;

//Imports from Javas library
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//Imports of static variables and methods
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.*;
import static utils.Constants.InteractableConstants.*;

/**
 * This class is responsible for providing assisting methods for every other class.
 * @author Linus Magnusson
 * @author Casper Johannesson
 * @author Simon Sandén
 */
public class AssistanceMethods {

    private static float y1;
    private static float y2;
    private static float y3;
    private static float x1;
    private static float x2;
    private static float x3;
    private static float x4;
    private static float x5;
    private static float x6;


    /**
     * Determines whether an object can move to a specified position within a level.
     *
     * @param x The x-coordinate of the object's position.
     * @param y The y-coordinate of the object's position.
     * @param width   The width of the object.
     * @param height  The height of the object.
     * @param levelData The 2D array representing the level.
     * @return  true if the object can move to the specified position, false otherwise.
     */
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] levelData){
       calcXAndYVariables(x, y, width, height);
        if(checkUpperPoints(levelData) == true){
            if(checkBottomPoints(levelData) == true){
                if(checkMiddlePoints(levelData) == true){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates and assigns the x and y variables used for point checks.
     *
     * @param x The x-coordinate of the object's position.
     * @param y The y-coordinate of the object's position.
     * @param width  The width of the object.
     * @param height The height of the object.
     */
    private static void calcXAndYVariables(float x, float y, float width, float height){
        y1 = y;
        y2 = y + height;
        y3 = y + (height / 2);
        x1 = x;
        x2 = x + width;
        x3 = x + (width/2);
    }

    /**
     * Checks the solidity of the upper points for the object.
     *
     * @param levelData The 2D array representing the level.
     * @return true if the upper points are not solid, false otherwise.
     */
    private static boolean checkUpperPoints(int[][] levelData){
        if(isTileSolid(x1,y1,levelData) == false){
            if(isTileSolid(x2, y1, levelData) == false){
                if(isTileSolid(x3,y1,levelData) == false){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks the solidity of the bottom points for the object.
     *
     * @param levelData The 2D array representing the level.
     * @return true if the bottom points are not solid, false otherwise.
     */
    private static boolean checkBottomPoints(int[][] levelData){
        if(isTileSolid(x1,y2,levelData) == false){
            if(isTileSolid(x2, y2, levelData) == false){
                if(isTileSolid(x3,y2,levelData) == false){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks the solidity of the middle points for the object.
     *
     * @param levelData The 2D array representing the level.
     * @return true if the middle points are not solid, false otherwise.
     */
    private static boolean checkMiddlePoints(int[][] levelData){
        if(isTileSolid(x1,y3,levelData) == false){
            if(isTileSolid(x2, y3, levelData) == false){
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the tile at the specified position is solid.
     *
     * @param x The x-coordinate of the tile position.
     * @param y The y-coordinate of the tile position.
     * @param levelData The 2D array representing the level.
     * @return true if the tile is solid, false otherwise.
     */
    private static boolean isTileSolid(float x, float y, int [][] levelData){
        int maxLevelWidth = levelData[0].length * TILES_SIZE;
        // Check if the position is inside the level's borders
        if(checkIfInsideBorder(x,y,maxLevelWidth) == true){
            return true;
        }
        // Calculate the indices and retrieve the tile value
        float xIndex = x/TILES_SIZE;
        float yIndex = y/TILES_SIZE;
        int value = levelData[(int)yIndex][(int)xIndex];

        // Check if the tile value represents a solid color
        return checkIfValidColor(value);
    }

    /**
     * Checks if the specified position is inside the level's borders.
     *
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @param maxLevelWidth  The maximum width of the level.
     * @return true if the position is outside the level's borders, false otherwise.
     */
    private static boolean checkIfInsideBorder(float x, float y, int maxLevelWidth) {
        if(isXWithinMaxLevelWidth(x, y, maxLevelWidth)){
            return true;
        }
        return y < 0 || y >= GAME_HEIGHT;
    }

    /**
     * Checks if the x-coordinate is within the maximum level width.
     *
     * @param x The x-coordinate to be checked.
     * @param y The y-coordinate (unused in the method).
     * @param maxLevelWidth The maximum width of the level.
     * @return true if the x-coordinate is outside the valid range, false otherwise.
     */
    private static boolean isXWithinMaxLevelWidth(float x, float y, int maxLevelWidth){
        if(x < 0 || x >= maxLevelWidth){
            return true;
        }
        return false;
    }

    /**
     * Checks if the specified value represents a valid color.
     *
     * @param value The value to be checked.
     * @return true if the value represents a valid color, false otherwise.
     */
    private static boolean checkIfValidColor(int value){
        if(value >= 48 || value <0 || value != 11){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the X position of an entity next to a wall.
     *
     * @param hitbox The hitbox of the entity.
     * @return The X position next to the wall.
     */
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox) {
        int currentTile = (int) hitbox.x / TILES_SIZE;
        return calculatePosNextToWall(currentTile, hitbox);
    }

    /**
     * Calculates the X position next to a wall based on the current tile and hitbox.
     *
     * @param currentTile The index of the current tile.
     * @param hitbox The hitbox of the entity.
     * @return The calculated X position next to the wall.
     */
    private static float calculatePosNextToWall(int currentTile, Rectangle2D.Float hitbox){
        int tileXPosition = currentTile * TILES_SIZE;
        int xOffset = (int)(TILES_SIZE - hitbox.width/2);
        return tileXPosition + (xOffset - 9);
    }

    /**
     * Retrieves the Y position of an entity under or above a tile based on its hitbox and airspeed.
     *
     * @param hitbox The hitbox of the entity.
     * @param airSpeed The airspeed of the entity.
     * @return The Y position under or above the tile.
     */
    public static float GetEntityYPosUnderOrAboveTile(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int) hitbox.y/TILES_SIZE;
        if(airSpeed > 0){
            //Falling
            return calculatePosUnderOrAboveTile(currentTile, hitbox);
        } else{
            //Jumping
            return currentTile * TILES_SIZE + 1;
        }
    }

    /**
     * Calculates the Y position under or above a tile based on the current tile and hitbox.
     *
     * @param currentTile The index of the current tile.
     * @param hitbox The hitbox of the entity.
     * @return The calculated Y position under or above the tile.
     */
    public static float calculatePosUnderOrAboveTile(int currentTile, Rectangle2D.Float hitbox){
        int tileYPosition = (currentTile * TILES_SIZE);
        int yOffset = (int)(TILES_SIZE - hitbox.height/2);
        return tileYPosition + yOffset + 1;
    }

    /**
     * Checks if the entity is currently on the floor.
     *
     * @param hitbox   The hitbox of the entity.
     * @param lvlData  The 2D array representing the level data.
     * @return true if the entity is on the floor, false otherwise.
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        calculateBottomHitboxChecks(hitbox);
        //Check the pixel below bottom left and bottom right
        if(isTileSolid(hitbox.x, x4, lvlData) == false && isTileSolid(x5, x4, lvlData) == false && isTileSolid(x6, x4, lvlData) == false){
            return false;
        }
        return true;
    }


    /**
     * Assists IsEntityOnFloor by calculating and assigning values to variables.
     * @param hitbox Rectangle2D.Float object to perform calculations on.
     */
    private static void calculateBottomHitboxChecks(Rectangle2D.Float hitbox){
        x4 = hitbox.y + hitbox.height + 1;   // 1 is to make sure character doesn't clip a pixel through the floor
        x5 = hitbox.x + hitbox.width;
        x6 = hitbox.x + (hitbox.width/2);
    }

    /**
     * Checks if there is a floor in front of the entity based on its hitbox, horizontal speed, and level data.
     *
     * @param hitbox The hitbox of the entity.
     * @param xSpeed The horizontal speed of the entity.
     * @param lvlData The 2D array representing the level data.
     * @return true if there is a floor in front of the entity, false otherwise.
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        calculateBottomHitboxChecks(hitbox);
        if(isTileSolid(x5 + xSpeed, x4, lvlData)
                && isTileSolid(hitbox.x + xSpeed, x4, lvlData)){
            return true;
        }
        return false;
    }


    /**
     * Retrieves the player spawn point from the given image.
     *
     * @param img the image to search for the player spawn point
     * @return the player spawn point as a Point object, or null if not found
     */
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

    /**
     * Calculates the player spawn coordinates based on the given point coordinates.
     *
     * @param pointX the x-coordinate of the point
     * @param pointY the y-coordinate of the point
     * @return the player spawn coordinates as a Point object
     */
    public static Point calculatePlayerSpawn(int pointX, int pointY){
        return new Point(pointX * TILES_SIZE, pointY * TILES_SIZE);
    }

    /**
     * Retrieves the level data from the given image.
     *
     * @param img The BufferedImage representing the level image.
     * @return The 2D array containing the level data.
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
     * Retrieves a list of EnemyRat objects from the given image.
     *
     * @param img The BufferedImage representing the image containing rats.
     * @return  An ArrayList of EnemyRat objects.
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

    public static ArrayList<Seagull> GetSeagulls(BufferedImage img) {
        ArrayList<Seagull> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == SEAGULL) {
                    list.add(new Seagull(i * TILES_SIZE, j * TILES_SIZE));
                }
            }
        }
        return list;
    }

    public static ArrayList<StaticEnemy> GetStaticEnemies(BufferedImage img){
        ArrayList<StaticEnemy> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == STEAM) {
                    list.add(new StaticEnemy(i * TILES_SIZE, j * TILES_SIZE, STEAM));
                } else if(value == FIRE){
                    list.add(new StaticEnemy(i*TILES_SIZE, j * TILES_SIZE, FIRE));
                } else if(value == WATER){
                    list.add(new StaticEnemy(i * TILES_SIZE, j * TILES_SIZE, WATER));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves a list of Box objects from the given image.
     *
     * @param img The BufferedImage representing the image containing boxes.
     * @return An ArrayList of Box objects.
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
