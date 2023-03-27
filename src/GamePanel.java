

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private float xDelta = 0;
    private float yDelta = 0;
    private float xDir = 0.1f;
    private float yDir = 0.1f;
    public GamePanel(){
        addKeyListener(new KeyBoardInputs(this));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //updateRectangle();
        g.setColor(Color.BLUE);
        g.fillRect(50+(int)xDelta,50+(int)yDelta,60,60);
    }

    private void updateRectangle() {
        xDelta += xDir;
        if(xDelta > 400 || xDelta < 0){
            xDir *= -1;
        }

        yDelta += yDir;
        if(yDelta > 400 || yDelta < 0){
            yDir *= -1;
        }

    }

    public void changeYDelta(int change){
        yDelta+=change;
    }

    public void changeXDelta(int change){
        xDelta+=change;
    }
}
