package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

public class PlayerOnFire {
    private static Bitmap bitmap;
    private static Player player;
    private static boolean onFire = false;
    private static ArrayList<Fire> fires = new ArrayList<Fire>();
    private static double playerX, playerY;
    private static final int NUM_OF_FIRE = 15, NUM_OF_FIRE_RAND = 10;

    public PlayerOnFire(Player player, Bitmap fireBmp) {
        super();
        this.player = player;
        this.bitmap = fireBmp;
    }

    public void setOnFire(boolean setOnFire){
        onFire = setOnFire;
        if (setOnFire) {
            renderFire(NUM_OF_FIRE + new Random().nextInt(NUM_OF_FIRE_RAND));
        }
    }

    public boolean getOnFire(){
        return onFire;
    }

    private void renderFire(int num){
        Random rand = new Random();
        playerX = player.getCoordinates().getX();
        playerY = player.getCoordinates().getY();
        fires.clear();
        for (int i = 0; i < num; i++){
            Fire fire = new Fire(bitmap);
            fire.setSize(15 + rand.nextInt(25));
            fire.getCoordinates().setX(playerX + rand.nextInt(player.getWidth() - fire.getWidth()));
            fire.getCoordinates().setY(playerY - (int)(fire.getHeight()*0.8) +
                    rand.nextInt(player.getHeight() - fire.getHeight() + (int)(fire.getHeight()*0.8)));
            fires.add(fire);
        }
    }

    private void updateFirePosition(){
        double playerX = player.getCoordinates().getX();
        double playerY = player.getCoordinates().getY();
        double playerDeltaX = playerX - this.playerX;
        double playerDeltaY = playerY - this.playerY;
        for (Fire fire : fires){
            fire.getCoordinates().setX(fire.getCoordinates().getX() + playerDeltaX);
            fire.getCoordinates().setY(fire.getCoordinates().getY() + playerDeltaY);
        }
        this.playerX = playerX;
        this.playerY = playerY;
    }



    public void onDraw(Canvas c){
        if (onFire){
            updateFirePosition();
            for (Fire fire : fires){
                fire.onDraw(c);
            }
        }
    }

}
