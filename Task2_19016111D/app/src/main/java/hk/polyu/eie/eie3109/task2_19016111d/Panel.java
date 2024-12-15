package hk.polyu.eie.eie3109.task2_19016111d;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.Random;

public class Panel extends SurfaceView implements SurfaceHolder.Callback{
    private SharedPreferences sharedPreferences;
    private Vibrator vibrator;

    private static Bitmap bmpPlayerSpriteSheet;
    private static Bitmap bmpTapeRollSpriteSheet;
    private static Bitmap bmpFireSpriteSheet;
    private static Bitmap bmpSoundButton;
    private static Bitmap bmpShakeButton;
    private static GameThread thread;

    private static Player player;
    private static Floor floor;
    private static TapeRoll[] tapeRolls = {null, null};
    private static GameText gameText;
    private static SoundEffect sound;
    private static FireWall fireWall;
    private static PlayerOnFire playerOnFire;
    private static Button muteButton, shakeButton;

    private static long time_action_down = 0;

    public Panel(Context context, SharedPreferences sharedPreferences, Vibrator v) {
        super(context);
        this.sharedPreferences = sharedPreferences;
        this.vibrator = v;
        getHolder().addCallback(this);

        bmpPlayerSpriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet);
        bmpTapeRollSpriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet_tape_roll);
        bmpFireSpriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet_fire);
        bmpSoundButton = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet_sound_button);
        bmpShakeButton = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet_shake_button);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        floor = new Floor();
        player = new Player(bmpPlayerSpriteSheet, floor);
        gameText = new GameText();
        sound = new SoundEffect(context);
        fireWall = new FireWall(bmpFireSpriteSheet, floor);
        playerOnFire = new PlayerOnFire(player, bmpFireSpriteSheet);
        gameText.setHighestScore(this.sharedPreferences.getInt("highestScore", 0));
        muteButton = new Button(bmpSoundButton, 0);
        muteButton.setState(this.sharedPreferences.getBoolean("soundOn", true));
        sound.setSoundOn(muteButton.getState());
        shakeButton = new Button(bmpShakeButton, 1);
        shakeButton.setState(this.sharedPreferences.getBoolean("shakeOn", true));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (thread.getSurfaceHolder()) {
            //buttons
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int)event.getX();
                int y = (int)event.getY();
                if (muteButton.onClick(x, y)) {
                    muteButton.toggleState();
                    sound.setSoundOn(muteButton.getState());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("soundOn", muteButton.getState());
                    editor.apply();
                    return true;
                } else if (shakeButton.onClick(x, y)){
                    shakeButton.toggleState();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("shakeOn", shakeButton.getState());
                    editor.apply();
                    return true;
                }
            }
            //control the player
            if (event.getAction() == MotionEvent.ACTION_DOWN && player.onFloor() && !gameText.isVisible()) {
                time_action_down = System.currentTimeMillis();
            } else if (event.getAction() == MotionEvent.ACTION_UP && time_action_down != 0) {
                sound.boundSound();
                player.jump(System.currentTimeMillis() - time_action_down);
                time_action_down = 0;
            }
            //start new game
            if (event.getAction() == MotionEvent.ACTION_DOWN && gameText.isVisible()){
                gameText.setVisible(false);
                gameText.setScore(player.respawn());
                for (int i = 0; i < 2; i++){
                    tapeRolls[i] = new TapeRoll(bmpTapeRollSpriteSheet, floor, i);
                }
                fireWall.updateWall(player.getScore());
                playerOnFire.setOnFire(false);
            }

            return true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        player.onDraw(canvas);
        floor.onDraw(canvas);
        fireWall.onDraw(canvas);
        playerOnFire.onDraw(canvas);
        gameText.onDraw(canvas);
        for (int i = 0; i < 2; i++){
            if (tapeRolls[i] != null){
                tapeRolls[i].onDraw(canvas);
            }
        }
        muteButton.onDraw(canvas);
        shakeButton.onDraw(canvas);
    }

    public void update(){
        player.update();
        if (time_action_down != 0 && player.onFloor()){
            player.compress(System.currentTimeMillis() - time_action_down);
        }
        //tape
        if (tapeRolls[0] != null && tapeRolls[1] != null){
            player.setSpawnTape(false);
        }
        for (int i = 0; i < 2; i++) {
            if (tapeRolls[i] != null) {
                if (player.collision(tapeRolls[i]) && !gameText.isVisible()) {
                    tapeRolls[i] = null;
                    sound.scoreSound();
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    gameText.setScore(player.setScore(player.getScore() + 1));
                    fireWall.updateWall(player.getScore());
                }
            } else if (!player.near_wall()) {
                if (player.spawnTape()) {
                    for (int j = 0; j < 2; j++){
                        if (tapeRolls[j] == null){
                            tapeRolls[j] = new TapeRoll(bmpTapeRollSpriteSheet, floor, j);
                        }
                    }
                }
            }
        }
        //firewall
        if (fireWall.collision(player)){
            if (!playerOnFire.getOnFire()) {
                sound.onFireSound();
                playerOnFire.setOnFire(true);
                int score = gameText.getScore();
                if (score > this.sharedPreferences.getInt("highestScore", 0)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("highestScore", score);
                    editor.apply();
                }
                gameText.setHighestScore(this.sharedPreferences.getInt("highestScore", 0));
                gameText.setText("Game Over :( Add oil la next time!\nTap to Restart :)");
                gameText.setVisible(true);
            }
        }
    }

    public void phoneIsShaking(){
        if (!gameText.isVisible() && shakeButton.getState() && player.onFloor()){
            sound.hopSound();
            player.jump(100);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(true);
        thread.start();
        int height = getHeight(), width = getWidth();
        floor.setPanelWidthHeight(width, height);
        player.setPanelWidthHeight(width, height);
        gameText.setPanelWidthHeight(width, height);
        muteButton.setPanelWidthHeight(width, height);
        shakeButton.setPanelWidthHeight(width, height);
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
}
