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
    }
}
