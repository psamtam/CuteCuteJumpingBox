package hk.polyu.eie.eie3109.assignment_19016111d;

import android.graphics.Bitmap;
import android.util.Log;

public class Coordinates {
    private int x = 100;
    private int y = 0;
    private Bitmap bitmap;

    public Coordinates(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setX(int x){
        //this.x = x - bitmap.getWidth()/2;
        this.x = x;
    }

    public void setY(int y){
        //this.y = y - bitmap.getHeight()/2;
        this.y = y;
    }

    public int getX(){
        //return x + bitmap.getWidth()/2;
        return x;
    }

    public int getY(){
        //return y + bitmap.getHeight()/2;
        return y;
    }
}
