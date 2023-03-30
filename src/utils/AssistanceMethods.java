package utils;

import main.Game;

public class AssistanceMethods {
    //Change box for level later
    public static boolean canMoveHere(float x, float y, int width, int heigth){
        if(!isSolid(x,y)){
            if(!isSolid(x + width,y + heigth)){
               if(!isSolid(x + width, y)){
                   if(!isSolid(x, y + heigth)){
                       return true;
                   }
               }
            }
        }
        return false;
    }
    //Implement later
    private static boolean isSolid(float x, float y){
        // TODO Change 1280 later for an variable
        if(x < 0 || x >= 1280){
            return true;
        }
        // TODO Change 800 later for an variable
        if(y < 0 || y >= 800){
            return true;
        }
        //TODO CHANGE LATER
        if(y > 800){
            return true;
        }
        return false;
    }
}
