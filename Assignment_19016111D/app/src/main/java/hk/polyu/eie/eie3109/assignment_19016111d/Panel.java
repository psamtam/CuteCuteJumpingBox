package hk.polyu.eie.eie3109.assignment_19016111d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap bmp;
    private GameThread thread;

    private ArrayList<GraphicObject> graphics = new ArrayList<GraphicObject>();

    public Panel(Context context) {
        super(context);
        getHolder().addCallback(this);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_custom);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Coordinates coords;
        int x, y;
        canvas.drawColor(Color.BLACK);

        for(GraphicObject graphic:graphics){
            coords = graphic.getCoordinates();
            x = coords.getX();
            y = coords.getY();
            canvas.drawBitmap(bmp, x, y, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (thread.getSurfaceHolder()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                for (GraphicObject graphic:graphics) {
                    if (graphic.clickedOnObject(x, y)){
                        graphics.remove(graphic);
                        return true;
                    }
                }

                GraphicObject graphic = new GraphicObject(bmp);

                int bmpW = graphic.getGraphic().getWidth();
                int bmpH = graphic.getGraphic().getHeight();
                Random random = new Random();
                int randXSpeed = random.nextInt(50);
                int randYSpeed = random.nextInt(50);

                graphic.getCoordinates().setX(x - bmpW / 2);
                graphic.getCoordinates().setY(y - bmpH / 2);
                graphic.getMovement().setXYSpeed(randXSpeed, randYSpeed);
                graphics.add(graphic);
            }
            return true;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setRunning(false);
        while(retry){
            try{
                thread.join();
                retry = false;
            } catch (InterruptedException e){

            }
        }
    }

    public void updateMovement(){
        Coordinates coord;
        Movement movement;

        int x, y;

        for (GraphicObject graphic:graphics){
            coord = graphic.getCoordinates();
            movement = graphic.getMovement();

            x = (movement.getXDirection() == Movement.X_DIRECTION_RIGHT) ? coord.getX() + movement.getXSpeed() : coord.getX() - movement.getXSpeed();
            if (x < 0){
                movement.toggleXDirection();
                coord.setX(-x);
            } else if (x + graphic.getGraphic().getWidth() > getWidth()){
                movement.toggleXDirection();
                coord.setX(x + getWidth() - (x + graphic.getGraphic().getWidth()));
            } else {
                coord.setX(x);
            }

            y = (movement.getYDirection() == Movement.Y_DIRECTION_DOWN) ? coord.getY() + movement.getYSpeed() : coord.getY() - movement.getYSpeed();
            if (y < 0){
                movement.toggleYDirection();
                coord.setY(-y);
            } else if (y + graphic.getGraphic().getHeight() > getHeight()){
                movement.toggleYDirection();
                coord.setY(y + getHeight() - (y + graphic.getGraphic().getHeight()));
            } else {
                coord.setY(y);
            }
        }
    }
}
