package items;

import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.Constants.ObjectConstants.*;

public class ItemManager {

    private Playing playing;
    private BufferedImage[][] consumableImages;
    private BufferedImage[][] containerImages;
    private ArrayList<Consumable> consumables;
    private ArrayList<Container> containers;


    public ItemManager(Playing playing){
        this.playing = playing;
        loadImages();

        consumables = new ArrayList<>();
        containers = new ArrayList<>();

        consumables.add(new Consumable(100, 720, BLUE_POTION));
        consumables.add(new Consumable(200, 720, RED_POTION));

        containers.add(new Container(300, 720, BARREL));
        containers.add(new Container(400, 733, BOX));
    }

    private void loadImages() {
        BufferedImage consumableSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        consumableImages = new BufferedImage[2][7];

        for(int j = 0; j < consumableImages.length; j++){
            for(int i = 0; i < consumableImages[j].length; i++){
                consumableImages[j][i] = consumableSprite.getSubimage(12*i, 16*j, 12, 16);
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8];

        for(int j = 0; j < containerImages.length; j++){
            for(int i = 0; i < containerImages[j].length; i++){
                containerImages[j][i] = containerSprite.getSubimage(40*i, 30*j, 40, 30);
            }
        }
    }

    public void update(){

        for(Consumable cons : consumables){
            if(cons.isActive()){
                cons.update();
            }
        }

        for(Container cont : containers){
            if(cont.isActive()){
                cont.update();
            }
        }
    }

    public void draw(Graphics g, int xLevelOffset){
        drawConsumable(g, xLevelOffset);
        drawContainer(g, xLevelOffset);
    }

    private void drawContainer(Graphics g, int xLevelOffset) {
        for(Container c : containers){
            if(c.isActive()){
                int containerType = 0;

                if(c.getObjectType() == BOX){
                    containerType = 0;
                }
                else if(c.getObjectType() == BARREL){
                    containerType = 1;
                }


                g.drawImage(containerImages[containerType][c.getAniIndex()],
                        (int) c.getHitbox().x - c.getxDrawOffset() - xLevelOffset,
                        (int) c.getHitbox().y - c.getyDrawOffset(),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);

            }
        }
    }

    private void drawConsumable(Graphics g, int xLevelOffset) {
        for(Consumable c : consumables){
            if(c.isActive()){
                int consumableType = 0;

                if(c.getObjectType() == BLUE_POTION){
                    consumableType = 0;
                }
                else if(c.getObjectType() == RED_POTION){
                    consumableType = 1;
                }

                g.drawImage(consumableImages[consumableType][c.getAniIndex()],
                        (int) c.getHitbox().x - c.getxDrawOffset() - xLevelOffset,
                        (int) c.getHitbox().y - c.getyDrawOffset(),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
        }
    }
}
