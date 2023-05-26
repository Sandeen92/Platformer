package items;

import gamestates.Gamestate;
import gamestates.Playing;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LevelFinish {

    BufferedImage finishImage;
    Rectangle2D.Float hitbox;
    Playing playing;
    String imageString;

    public LevelFinish(Playing playing, String imageString) {
        this.playing = playing;
        this.imageString = imageString;
        loadFinishLevelImage();
        initialiseHitbox();
    }

    private void initialiseHitbox() {
        hitbox = new Rectangle2D.Float(250,580,10,200);
    }


    public boolean checkIfPlayerCollides(){
        if(hitbox.intersects(playing.getPlayer().getHitbox())){
            Gamestate.state = Gamestate.LEVELCOMPLETED;
            return true;
        }
        return false;
    }

    private void loadFinishLevelImage() {
        InputStream inputStream = getClass().getResourceAsStream(imageString);
        try{
            finishImage = ImageIO.read(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
