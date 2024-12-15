package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameText extends GameObject{
    private static Paint paint, paint_score, paint_highest;
    private static boolean visible = true;
    private static String text;
    private static int score;
    private static int highestScore;

    public GameText() {
        super();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(60);
        paint_score = new Paint();
        paint_score.setColor(Color.WHITE);
        paint_score.setStyle(Paint.Style.FILL);
        paint_score.setTextSize(50);
        paint_highest = new Paint();
        paint_highest.setColor(Color.WHITE);
        paint_highest.setStyle(Paint.Style.FILL);
        paint_highest.setTextSize(30);
        text = "Touch to Jump! Hold to Jump HIGHER!!\nShake to hop!\nTap to start game!!!";
        score = 0;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public void setHighestScore(int score){
        this.highestScore = score;
    }

    public boolean isVisible(){
        return visible;
    }

    public void onDraw(Canvas c){
        c.drawText("Score: " + score, 30, 70, paint_score);
        c.drawText("Previous Best: " + highestScore, 30, 110, paint_highest);
        if (visible){
            String[] texts = text.split("\n");
            for (int i = 0; i < texts.length; i++){
                c.drawText(texts[i], getPanelWidth()/2,
                        getPanelHeight()/2 - (texts.length-1) * paint.getTextSize() * 0.5f
                                + i * paint.getTextSize() + (i-1) * paint.getTextSize() * 0.3f
                        , paint);
            }
        }
    }

}
