package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Fire extends GameObject{
    private static Bitmap bitmap;
    private static int bmpWidth, bmpHeight;
    private static final int BMP_ROWS = 1, BMP_COLUMNS = 5;
    private int FIRE_WIDTH = 50, FIRE_HEIGHT = FIRE_WIDTH * 170 / 84;
    private static int updateRate = 8; //per second
    private int updateCount = 0;
    private int currentFrameCol = 0;
    private int currentFrameRow = 0;


    public Fire(Bitmap bitmap) {
        super();
        this.bitmap = bitmap;
        this.bmpWidth = bitmap.getWidth() / BMP_COLUMNS;
        this.bmpHeight = bitmap.getHeight() / BMP_ROWS;
        super.setSize(FIRE_WIDTH, FIRE_HEIGHT);
        Random rand = new Random();
        this.currentFrameCol = rand.nextInt(BMP_COLUMNS);
        this.updateCount = rand.nextInt((60 / updateRate));
    }

    private void updateSprite(){
        updateCount++;
        if (updateCount < (60 / updateRate)){
            return;
        }
        currentFrameCol = (currentFrameCol + 1) % BMP_COLUMNS;
        updateCount = 0;
    }

    public void setSize(int width) {
        super.setSize(width, width * 170 / 84);
    }

    public void onDraw(Canvas c){
        updateSprite();
        //fire
        int scrX = currentFrameCol * bmpWidth, scrY = currentFrameRow * bmpHeight;
        Rect scr = new Rect(scrX, scrY, scrX + bmpWidth, scrY + bmpHeight);
        double x = getCoordinates().getX(), y = getCoordinates().getY();
        super.set(x, y, getWidth(), getHeight());
        Rect dst = new Rect((int)x, (int)y, (int)x + getWidth(), (int)y + getHeight());
        c.drawBitmap(bitmap, scr, dst, null);
    }
}
