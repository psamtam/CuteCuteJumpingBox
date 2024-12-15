package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Button extends GameObject{
    private Bitmap bitmap;
    private int bmpWidth, bmpHeight;
    private static final int BMP_ROWS = 1, BMP_COLUMNS = 2;
    private int currentFrameCol = 1;
    private int currentFrameRow = 0;

    private boolean state = true;


    private final int BUTTON_WIDTH = 120, BUTTON_HEIGHT = BUTTON_WIDTH;
    private int index;

    public Button(Bitmap bitmap, int index) {
        super();
        this.bitmap = bitmap;
        this.bmpWidth = bitmap.getWidth() / BMP_COLUMNS;
        this.bmpHeight = bitmap.getHeight() / BMP_ROWS;
        this.index = index;
        super.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    public void setState(boolean state){
        this.state = state;
        if (state){
            currentFrameCol = 1;
        } else {
            currentFrameCol = 0;
        }
    }

    public boolean getState(){
        return state;
    }

    public void toggleState(){
        state = !state;
        if (state){
            currentFrameCol = 1;
        } else {
            currentFrameCol = 0;
        }
    }

    public boolean onClick(int x, int y){
        if (getCoordinates().getX() < x && x < getCoordinates().getX() + getWidth() &&
        getCoordinates().getY() < y && y < getCoordinates().getY() + getHeight()){
            return true;
        }
        return false;
    }

    public void onDraw(Canvas c){
        int x = getPanelWidth() - index * 20 - (index + 1) * BUTTON_WIDTH;
        int y = 20;
        super.set(x, y, getWidth(), getHeight());
        int scrX = currentFrameCol * bmpWidth, scrY = currentFrameRow * bmpHeight;
        Rect scr = new Rect(scrX, scrY, scrX + bmpWidth, scrY + bmpHeight);
        Rect dst = new Rect((int)x, (int)y, (int)x + getWidth(), (int)y + getHeight());
        c.drawBitmap(bitmap, scr, dst, null);

    }
}
