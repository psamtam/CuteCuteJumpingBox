package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Floor extends GameObject{
    private static Paint paint = new Paint();

    public Floor() {
        super();
        paint.setColor(Color.GRAY);
    }

    @Override
    public Coordinates getCoordinates() {
        return super.getCoordinates();
    }

    public void onDraw(Canvas c){
        //floor
        int x = 0, y = (int)(getPanelHeight() - (getPanelHeight() * 0.10f));
        super.set(x, y, getPanelWidth(), getPanelHeight());
        c.drawRect(x, y, getPanelWidth(), getPanelHeight(), paint);
    }
}
