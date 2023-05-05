package items;

import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import static utils.Constants.GunManConstants.GUN_MAN_CHANGER;
import static utils.Constants.ObjectConstants.*;


public class ItemManager {

    private Playing playing;
    private ArrayList<ClassChanger> classChangers;


    public ItemManager(Playing playing){
        this.playing = playing;
        classChangers = new ArrayList<>();
        classChangers.add(new ClassChanger(playing,GUN_MAN_CHANGER));
    }


    public void update(){
        for(Iterator<ClassChanger> iterator = classChangers.iterator(); iterator.hasNext();){
            ClassChanger classChanger = iterator.next();
            if(classChanger.checkIfPlayerCollides()){
                iterator.remove();
            }
        }
    }

    public void draw(Graphics g, int xLevelOffset){
        drawClassChanger(g, xLevelOffset);
    }


    private void drawClassChanger(Graphics g, int xLevelOffset) {
        for(ClassChanger classChanger: classChangers){
            g.drawImage(classChanger.changerImage, (int) classChanger.hitbox.x - xLevelOffset, (int) classChanger.hitbox.y, (int)  classChanger.hitbox.width, (int) classChanger.hitbox.height, null);
        }
    }
}

