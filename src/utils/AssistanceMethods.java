/**
 * This class has Assistance methods for the checks in the game
 * @author Linus Magnusson
 */

package utils;

import main.Game;
import java.awt.geom.Rectangle2D;

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
        if(!isSolid(x,y, levelData)){
            if(!isSolid(x + width,y + heigth, levelData)){
               if(!isSolid(x + width, y, levelData)){
                   if(!isSolid(x, y + heigth, levelData)){
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
    private static boolean isSolid(float x, float y, int [][] levelData){
        if(x < 0 || x >= Game.GAME_WIDTH){
            return true;
        }
        if(y < 0 || y >= Game.GAME_HEIGHT){
            return true;
        }

        float xIndex = x/Game.TILES_SIZE;
        float yIndex = y/Game.TILES_SIZE;
        int value = levelData[(int)yIndex][(int) xIndex];

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
     * @param xSpeed
     * @return
     */
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile = (int) hitbox.x/Game.TILES_SIZE;
        if(xSpeed > 0){
            //Rigth
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            //Left
            return currentTile * Game.TILES_SIZE;
        }

    }

    /**
     * This method is used to get the entitys position closest to the roof/floor to make
     * gaps between the entity and objects/tiles to disapear
     * @param hitbox
     * @param airSpeed
     * @return
     */
    public static float GetEntityYPosUnderOrAboveTile(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int) hitbox.y/Game.TILES_SIZE;
        if(airSpeed > 0){
            //Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            //Jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * This method checks if the entity is currently standing on the floor
     * @param hitbox
     * @param lvlData
     * @return
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        //Check the pixel below bottom left and bottom right
        if(!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)){
            if(!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)){
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
        if(isSolid(hitbox.x + xSpeed + hitbox.width, hitbox.y + hitbox.height + 1, lvlData) && isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData)){
            return true;
        }
        return false;
    }
}
