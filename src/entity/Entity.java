package entity;

import java.awt.*;

public abstract class Entity {
    protected   float x;
    protected float y;
    protected int width;
    protected int heigth;
    protected Rectangle hitbox;

    public Entity(float x, float y,int width, int heigth){
        this.x = x;
        this.y = y;
        this.heigth = heigth;
        this.width = width;
        initialiseHitbox();
    }

    private void initialiseHitbox() {
        hitbox = new Rectangle((int)x, (int)y,width,heigth);
    }
    // TODO CHANGE TO PROTECTED LATER WHEN WE DONT CREATE OBJECT IN GAMEPANEL
    public void drawHitbox(Graphics g){
        //For Debugging hitbox
        g.setColor(Color.BLACK);
        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    protected void updateHitbox(){
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }


}
