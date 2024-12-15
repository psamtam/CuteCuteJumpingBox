package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class FireWall extends GameObject{
    private static Bitmap bitmap;
    private static Floor floor;
    private static final int MAX_NUM_OF_FIRE = 20;
    private static ArrayList<Fire> fires = new ArrayList<Fire>();
    private static int randHeight;
    private static double gapWidth;
    private static final int MAX_RAND_HEIGHT = 6;
    private static final double MIN_GAP_WIDTH = 1.3;

    public FireWall(Bitmap bitmap, Floor floor) {
        super();
        this.bitmap = bitmap;
        this.floor = floor;
    }

    public void updateWall(int score){
        Random rand = new Random();
        gapWidth = 3 - (score * 0.03);
        gapWidth = Math.max(gapWidth, MIN_GAP_WIDTH);
        setWall(rand.nextInt(MAX_RAND_HEIGHT), gapWidth);
    }

    public void setWall(int randHeight, double gapWidth){
        fires.clear();
        double x = floor.getWidth() / 2, y = floor.getCoordinates().getY();
        for (int i = 0; i < randHeight; i++){
            Fire fire = new Fire(bitmap);
            fire.getCoordinates().setX(x - fire.getWidth() / 2);
            fire.getCoordinates().setY(y - fire.getHeight() * (i+1) + fire.getHeight() * i * 0.1);
            fires.add(fire);
        }
        for (int i = randHeight; i < MAX_NUM_OF_FIRE; i++){
            Fire fire = new Fire(bitmap);
            double sety = y - fire.getHeight() * (i+1) + fire.getHeight() * i * 0.1 - gapWidth * fire.getHeight();
            if (sety < -fire.getHeight()){
                break;
            }
            fire.getCoordinates().setX(x - fire.getWidth() / 2);
            fire.getCoordinates().setY(sety);
            fires.add(fire);
        }
    }

    public boolean collision(Player player){
        double[] playerX = {player.getCoordinates().getX(), player.getCoordinates().getX() + player.getWidth()};
        double[] playerY = {player.getCoordinates().getY(), player.getCoordinates().getY() + player.getHeight()};
        double[] fireX = new double[2], fireY = new double[2];
        for (Fire fire : fires){
            double fireXCentre = fire.getCoordinates().getX() + fire.getWidth() / 2;
            int fireWidth = (int)(fire.getWidth() * 0.3);
            fireX[0] = fireXCentre - fireWidth / 2;
            fireX[1] = fireXCentre + fireWidth / 2;
            fireY[0] = fire.getCoordinates().getY();
            fireY[1] = fireY[0] + fire.getHeight();

            if ( (playerX[0] < fireX[1]) && (playerX[1] > fireX[0]) &&
                    (playerY[0] < fireY[1]) && (playerY[1] > fireY[0]) ){
                return true;
            }
        }
        return false;
    }


    public void onDraw(Canvas c) {
        for (Fire fire : fires){
            fire.onDraw(c);
        }
    }
}
