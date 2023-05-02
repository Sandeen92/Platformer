package utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class LoadSave {

    public static final String LEVEL_ATLAS = "LEVEL_ONE_TILESET_SPRITES.png";
    public static final String RAT_ENEMY = "ENEMY_RAT_BROWN.png";
    public static final String STARTMENU_BACKGROUND = "STARTMENU_BACKGROUND.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String DEATHSCREEN = "DEATHSCREEN.gif";
    public static final String DEATHSCREEN_YOUDIED = "youded.png";
    public static final String POTION_ATLAS = "TEMP_POTION_SPRITES.png";
    public static final String CONTAINER_ATLAS = "CONTAINER_SPRITES.png";
    public static final String STARTMENU_BUTTONS = "STARTMENU_BUTTONS.png";
    public static final String DEATHSCREEN_REPLAY_BUTTON = "DEATHSCREEN_BUTTON.png";
    public static final String PAUSE_BACKGROUND = "MENU_PAUSED.png";
    public static final String OPTIONS_BACKGROUND = "MENU_OPTIONS_BG.png";
    public static final String OPTIONS_BUTTONS = "MENU_OPTIONS_BUTTONS.png";
    public static final String SOUND_BUTTONS = "soundbuttons.png";
    public static final String STARTMENU_MUSIC = "resources/ohboy.wav";
    public static final String DEATHSCREEN_MUSIC = "resources/coffindance.wav";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            image = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
    
    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/leveldata");
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

    public static BufferedImage[] getDeathscreenGif() throws IOException {
        BufferedImage[] frames;
        File gifFile = new File("resources/DEATHSCREEN.gif");
        try (ImageInputStream in = ImageIO.createImageInputStream(gifFile)) {
            ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
            reader.setInput(in);
            int numFrames = reader.getNumImages(true);
            frames = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                BufferedImage frame = reader.read(i);
                frames[i] = resizeGif(frame);
            }
            reader.dispose();
        }
        return frames;
    }

    private static BufferedImage resizeGif(BufferedImage originalImage){
        BufferedImage resizedImage = new BufferedImage(300,300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage,0,0,300,300,null);
        g2d.dispose();
        return resizedImage;
    }

}

