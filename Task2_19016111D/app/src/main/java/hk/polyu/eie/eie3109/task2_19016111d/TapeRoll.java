package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class TapeRoll extends GameObject{
    private static Floor floor;
    private static Bitmap bitmap;

    private static int bmpWidth, bmpHeight;
    private static final int ROLL_WIDTH = 60, ROLL_HEIGHT = ROLL_WIDTH * 186 / 188;
    private static final int BMP_ROWS = 1, BMP_COLUMNS = 8;
    private static int updateRate = 8; //per second
    private static int updateCount = 0;
    private static int currentFrameCol = 0;
    private static int currentFrameRow = 0;

    private static final double RANDOM_RANGE_MULTIPIER = 0.4;
    private static final int LEFT = 0, RIGHT = 1;

    private static final long TAPE_RESPAWN_TIME_MS = 800;


    public TapeRoll(Bitmap bitmap, Floor floor, int leftOrRight) {  //left == 0, right == 1
        super();
        this.bitmap = bitmap;
        this.bmpWidth = bitmap.getWidth() / BMP_COLUMNS;
        this.bmpHeight = bitmap.getHeight() / BMP_ROWS;
        super.setSize(ROLL_WIDTH, ROLL_HEIGHT);
        this.floor = floor;
        setCoordinate(leftOrRight);
    }

    private void setCoordinate(int leftOrRight){
        int floorLevel = (int) floor.getCoordinates().getY();
        Random rand = new Random();
        if (leftOrRight == LEFT){
            getCoordinates().setX(floor.getCoordinates().getX() + 50);
            getCoordinates().setY(floorLevel - 3 * ROLL_HEIGHT - rand.nextInt((int)(floorLevel * RANDOM_RANGE_MULTIPIER)));
        } else if (leftOrRight == RIGHT){
            getCoordinates().setX(floor.getCoordinates().getX() + floor.getWidth() - ROLL_WIDTH - 50);
            getCoordinates().setY(floorLevel - 3 * ROLL_HEIGHT - rand.nextInt((int)(floorLevel * RANDOM_RANGE_MULTIPIER)));
        }
    }

    public Bitmap getGraphic(){
        return bitmap;
    }

    public Coordinates getCoordinates(){
        return super.getCoordinates();
    }

    public static long getTapeRespawnTimeMs() {
        return TAPE_RESPAWN_TIME_MS;
    }

    private void updateSprite(){
        updateCount++;
        if (updateCount < (60 / updateRate)){
            return;
        }
        currentFrameCol = (currentFrameCol + 1) % BMP_COLUMNS;
        updateCount = 0;
    }


    public void onDraw(Canvas c){
        updateSprite();
        //box
        int scrX = currentFrameCol * bmpWidth, scrY = currentFrameRow * bmpHeight;
        Rect scr = new Rect(scrX, scrY, scrX + bmpWidth, scrY + bmpHeight);
        double x = getCoordinates().getX(), y = getCoordinates().getY();
        Rect dst = new Rect((int)x, (int)y, (int)x + ROLL_WIDTH, (int)y + ROLL_HEIGHT);
        super.set(x, y, ROLL_WIDTH, ROLL_HEIGHT);
        c.drawBitmap(bitmap, scr, dst, null);
    }

}
