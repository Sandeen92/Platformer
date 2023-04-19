package items;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import static utils.Constants.GameConstants.*;
import static utils.Constants.ObjectConstants.*;


/**
 *
 *
 * ------------------------------- GRANSKA EJ DENNA KLASS! --------------------------------------
 *
 *
 */

public class GameItem {

    protected int x;
    protected int y;
    protected Rectangle2D.Float hitbox;
    protected int objectType;
    protected boolean doAnimation = false;
    protected boolean active = true;
    protected int aniTick;
    protected int aniIndex;
    protected int xDrawOffset;
    protected int yDrawOffset;
    protected int animationSpeed = 30;

    public GameItem(int x, int y, int objectType){
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick >= animationSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objectType)){
                aniIndex = 0;

                if(objectType == BARREL || objectType == BOX){
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    protected void initialiseHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * SCALE), (int) (height * SCALE));
    }

    public void drawHitbox(Graphics g, int levelOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x - levelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void reset(){
        aniTick = 0;
        aniIndex = 0;
        active = true;

        if(objectType == BARREL || objectType == BOX){
            doAnimation = false;
        }
        else{
            doAnimation = true;
        }

    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle2D.Float hitbox) {
        this.hitbox = hitbox;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public void setxDrawOffset(int xDrawOffset) {
        this.xDrawOffset = xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public void setyDrawOffset(int yDrawOffset) {
        this.yDrawOffset = yDrawOffset;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }
}
