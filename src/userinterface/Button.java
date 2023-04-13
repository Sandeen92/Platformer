package userinterface;

import java.awt.*;

public class Button {

    protected int x, y, width, height;
    protected Rectangle bounds;

    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
        System.out.println("Bounds created at: " + x + " + " + y + " with width: " + width + " and height: " + height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle getBounds() {
        System.out.println("Lämnar tillbaka bounds");
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
