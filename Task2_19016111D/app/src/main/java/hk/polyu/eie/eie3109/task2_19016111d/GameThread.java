package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private static SurfaceHolder surfaceHolder;
    private static Panel panel;
    private static boolean run = false;

    private static final int REFRESH_RATE = 60;

    public GameThread(SurfaceHolder surfaceHolder, Panel panel){
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
    }

    public void setRunning(boolean run){
        this.run = run;
    }

    @Override
    public void run() {
        super.run();
        Canvas c;
        while(run){
            c = null;
            try{
                c = surfaceHolder.lockCanvas();
                synchronized(surfaceHolder){
                    panel.update();
                    panel.onDraw(c);
                }
            } finally {
                if (c != null){
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public SurfaceHolder getSurfaceHolder(){
        return surfaceHolder;
    }
}
