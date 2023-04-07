package entity;

import gamestates.Playing;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImg();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.getCrabbs();
        System.out.println("Amount of enemies" + crabbies.size());
    }

    public void update(int[][] lvldata){
        for(Crabby c : crabbies){
            c.update(lvldata);
        }
    }

    public void draw(Graphics g){
        drawCrabs(g);
    }

    private void drawCrabs(Graphics g) {
        for(Crabby c : crabbies){
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAnimationIndex()], (int) c.getHitbox().x - CRABBY_DRAW_OFFSET_X, (int) c.getHitbox().y - CRABBY_DRAW_OFFSET_Y, CRABBY_WIDTH, CRABBY_HEIGHT, null );
            c.drawHitbox(g);
        }
    }

    private void loadEnemyImg() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.TESTENEMY);
        for(int i = 0; i < crabbyArr.length; i++)  {
            for (int j = 0; j < crabbyArr[i].length; j++){
                crabbyArr[i][j] = temp.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }


}
