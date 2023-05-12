package userinterface;
//Imports from Javas library
import java.awt.*;

/**
 * This class is a superclass for every reactive button in the OPTIONS state.
 * It's responsible for giving every button a "hitbox" with same dimensions and position as the button itself, when instantiated.
 *
 * @author Simon Sandén
 */
public class Button {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Rectangle bounds;

    /**
     * Constructs a new Button object with the specified x and y coordinates, width, and height.
     *
     * @param x the x-coordinate of the button's top-left corner
     * @param y the y-coordinate of the button's top-left corner
     * @param width the width of the button
     * @param height the height of the button
     */
    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    /**
     * Creates a rectangular bounds object for the button using the current x, y, width, and height values.
     */
    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Returns the bounds of the button.
     * @return The button's bounding rectangle.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Sets the bounds of the button.
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
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

}
