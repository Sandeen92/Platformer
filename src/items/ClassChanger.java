package items;

import gamestates.Playing;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is the representation of the item that changes classes
 * @author Linus Magnusson
 */
public class ClassChanger {
    BufferedImage changerImage;
    Rectangle2D.Float hitbox;
    Playing playing;
    String imageString;

    public ClassChanger(Playing playing, String imageString){
        this.playing = playing;
        this.imageString = imageString;
        loadClassChangerImage();
        initialiseHitbox();
    }

    /**
     * Initializes the hitbox of the object.
     */
    private void initialiseHitbox(){
        hitbox = new Rectangle2D.Float(4700,450,50,50);
    }

    /**
     * Checks if the player collides with the object.
     *
     * @return true if the player collides, false otherwise
     */
    public boolean checkIfPlayerCollides(){
        if(hitbox.intersects(playing.getPlayer().getHitbox())){
            playing.changeClass("GunMan", playing.getPlayer().getHitbox().x, playing.getPlayer().getHitbox().y, playing.getPlayer().getFacingDirection());
            playing.getEnemyManager().initlalisePlayerToseagulls();
            return true;
        }
        return false;
    }

    /**
     * This method loads the image of the ClassChanger
     */
    private void loadClassChangerImage() {
        InputStream inputStream = getClass().getResourceAsStream(imageString);
        try{
            changerImage = ImageIO.read(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }



}
