package entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected   float x;
    protected float y;
    protected int width;
    protected int heigth;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y,int width, int heigth){
        this.x = x;
        this.y = y;
        this.heigth = heigth;
        this.width = width;
    }

    protected void initialiseHitbox(float x, float y, float width, float heigth) {
        hitbox = new Rectangle2D.Float(x, y,width,heigth);
    }

    protected void drawHitbox(Graphics g){
        //For Debugging hitbox
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void updateHitbox(){
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }


}
