package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class LoadSave {

    public static final String LEVEL_ATLAS = "level_one_sprites.png";
    public static final String RATENEMY = "Rat_brun_vers1.png";
    public static final String STARTMENU_BACKGROUND = "startmenu_background.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String DEATHSCREEN = "temp_DeathScreen.png";
    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String STARTMENU_BUTTONS = "startmenubutton_atlas.png";
    public static final String PAUSE_BACKGROUND = "pauseimage.png";
    public static final String OPTIONS_BACKGROUND = "optionsmenu.png";
    public static final String OPTIONS_BUTTONS = "optionbuttons.png";
    public static final String SOUND_BUTTONS = "soundbuttons.png";
    public static final String STARTMENU_MUSIC = "resources/ohboy.wav";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
    
    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/levelres");
        File levelData = null;

        try {
            levelData = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] levelDataList = levelData.listFiles();
        File[] levelDataListSorted = sortLevelDataList(levelDataList);

        BufferedImage[] levelDataImages = new BufferedImage[levelDataListSorted.length];

        try {
            for(int i = 0; i < levelDataImages.length; i++) {
                levelDataImages[i] = ImageIO.read(levelDataListSorted[i]);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return levelDataImages;
    }


    public static File[] sortLevelDataList(File[] levelDataList){

        File[] levelDataListSorted = new File[levelDataList.length];

        for(int i = 0; i < levelDataListSorted.length; i++){
            for(int j = 0; j < levelDataList.length; j++){
                if(levelDataList[j].getName().equals((i + 1) + ".png")){
                    levelDataListSorted[i] = levelDataList[j];
                }
            }
        }
        return levelDataListSorted;
    }


}

