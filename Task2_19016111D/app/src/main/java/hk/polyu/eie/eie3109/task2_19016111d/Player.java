package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class Player extends GameObject{
    private static Bitmap bitmap;
    private static Floor floor;

    // variables for sprite
    private static final int PLAYER_WIDTH = 120, PLAYER_HEIGHT = PLAYER_WIDTH * 2 / 3;
    private static final int BMP_ROWS = 2, BMP_COLUMNS = 5;
    private static int currentFrameCol = 0;
    private static int tempCurrentFrameCol = 0;
    private static int currentFrameRow = 1;
    private static int bmpWidth, bmpHeight;
    private static final int LEFT = 1, RIGHT = 2;
    private static int lastLR = LEFT;
    private static int updateCount = 0;
    private static int updateRate = 2; //per second
    private static boolean compressing = false;
    private static double compression = 0;
    private static boolean mirrored = false;

    //variables for physics
    private static final double GRAVITY = 9.80665;
    private static final double ACCELERATION_MULTIPLIER = 0.1;
    private static final double MAX_JUMP_STRENGTH = 40;
    private static final double JUMP_TIME_MULTIPLIER = 0.07;
    private static double acceleration = 0;
    private static double speed_x = 0;
    private static final double XY_SPEED_RATIO = 0.4;
    private static final double FRICTION = 1.2;

    //variables for game
    private static int direction = RIGHT;
    private static int score = 0;
    private static int max_score = 0;
    private static boolean spawnTape = false;




    public Player(Bitmap bitmap, Floor floor){
        super();
        this.bitmap = bitmap;
        this.bmpWidth = bitmap.getWidth() / BMP_COLUMNS;
        this.bmpHeight = bitmap.getHeight() / BMP_ROWS;
        super.setSize(PLAYER_WIDTH, PLAYER_HEIGHT);
        this.floor = floor;
    }

    private void mirrorBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        mirrored = !mirrored;
        this.bitmap = newBitmap;
        currentFrameCol = BMP_COLUMNS - 1 - currentFrameCol;
    }

    public Bitmap getGraphic(){
        return bitmap;
    }

    public Coordinates getCoordinates(){
        return super.getCoordinates();
    }

    public int getScore(){
        return score;
    }

    public int setScore(int score){
        this.score = score;
        return this.score;
    }

    public boolean spawnTape(){
        if (spawnTape){
            spawnTape = false;
            return true;
        }
        return false;
    }

    public int respawn(){
        getCoordinates().setX(200);
        direction = RIGHT;
        score = 0;
        speed_x = 0;
        acceleration = 0;
        if (mirrored){
            mirrorBitmap(bitmap);
        }
        return score;
    }

    public void setSpawnTape(boolean b){
        spawnTape = b;
    }

    public void update(){
        updateY();
        updateX();
    }

    private void updateSprite(){
        if (!compressing) {
            currentFrameRow = 1;
            updateCount++;
            if (onFloor()){
                updateRate = 2;
            } else {
                updateRate = 15;
            }
            if (updateCount < (60 / updateRate)) {
                return;
            }
            if (tempCurrentFrameCol == 0) {
                lastLR = (tempCurrentFrameCol = (lastLR == LEFT) ? RIGHT : LEFT);
            } else {
                tempCurrentFrameCol = 0;
            }
            updateCount = 0;
        } else if (compressing){
            currentFrameRow = 0;
            tempCurrentFrameCol = 5 - (int) Math.ceil(compression * 5);
            if (tempCurrentFrameCol > 4) tempCurrentFrameCol = 4;
            if (compression < 0.2){
                tempCurrentFrameCol = 4;
            }
        }
        if (mirrored){
            currentFrameCol = BMP_COLUMNS - 1 - tempCurrentFrameCol;
        } else {
            currentFrameCol = tempCurrentFrameCol;
        }
    }

    private void updateY(){
        if (belowFloor()) {
            acceleration = 0;
            getCoordinates().setY(floor.getCoordinates().getY() - getHeight());
        } else {
            if (flyingAboveFloor()){
                acceleration += GRAVITY * ACCELERATION_MULTIPLIER;
            }
            getCoordinates().setY(getCoordinates().getY() + (int)acceleration);
        }
    }

    private void updateX(){
        if (direction == RIGHT){
            if (getCoordinates().getX() + getWidth() >= getPanelWidth()){
                direction = LEFT;
                mirrorBitmap(bitmap);
                spawnTape = true;
            }
        }
        if (direction == LEFT) {
            if (getCoordinates().getX() <= 0) {
                direction = RIGHT;
                mirrorBitmap(bitmap);
                spawnTape = true;
            }
        }

        if (onFloor() && speed_x > 0){
            speed_x -= FRICTION;
            if (speed_x < 0) speed_x = 0;
        }

        double x = speed_x * (((double) direction - 1.5) * 2);
        getCoordinates().setX(getCoordinates().getX() + x);
    }

    private boolean belowFloor(){
        if (getCoordinates().getY() + getHeight() > floor.getCoordinates().getY()){
            return true;
        }
        return false;
    }

    private boolean flyingAboveFloor(){
        if (getCoordinates().getY() + getHeight() < floor.getCoordinates().getY()){
            return true;
        }
        return false;
    }

    public boolean onFloor(){
        if (getCoordinates().getY() + getHeight() == floor.getCoordinates().getY()){
            return true;
        }
        return false;
    }

    private double calc_jump_strength(long time_pressed_ms){
        double jump_strength = time_pressed_ms * JUMP_TIME_MULTIPLIER;
        if (jump_strength > MAX_JUMP_STRENGTH){
            jump_strength = MAX_JUMP_STRENGTH;
        }
        return jump_strength;
    }

    private void setCompressing(boolean set){
        compressing = set;
    }

    public void jump(long time_pressed_ms){
        setCompressing(false);
        double jump_strength = calc_jump_strength(time_pressed_ms);
        if (onFloor()){
            acceleration -= jump_strength;
            speed_x = jump_strength * XY_SPEED_RATIO;
        }
    }

    public void compress(long time_pressed_ms){
        if (onFloor()) {
            setCompressing(true);
            double jump_strength = calc_jump_strength(time_pressed_ms);
            compression = jump_strength / MAX_JUMP_STRENGTH;
        }
    }

    public boolean collision(GameObject obj){
        double playerx1 = getCoordinates().getX(), playerx2 = playerx1 + getWidth();
        double playery1 = getCoordinates().getY(), playery2 = playery1 + getHeight();
        double objx1 = obj.getCoordinates().getX(), objx2 = objx1 + obj.getWidth();
        double objy1 = obj.getCoordinates().getY(), objy2 = objy1 + obj.getHeight();

        if ( (playerx1 < objx2) && (playerx2 > objx1) &&
                (playery1 < objy2) && (playery2 > objy1) ){
            return true;
        }
        return false;
    }

    public boolean near_wall(){
        double NEAR_THRESHOLD = 0.1;

        double [] playerx = {getCoordinates().getX(), getCoordinates().getX() + getWidth()};

        return playerx[0] < getPanelWidth() * NEAR_THRESHOLD || playerx[1] > getPanelWidth() * (1 - NEAR_THRESHOLD);
    }

    public void onDraw(Canvas c){
        updateSprite();
        //box
        int scrX = currentFrameCol * bmpWidth, scrY = currentFrameRow * bmpHeight;
        Rect scr = new Rect(scrX, scrY, scrX + bmpWidth, scrY + bmpHeight);
        double x = getCoordinates().getX(), y = getCoordinates().getY();
        Rect dst = new Rect((int)x, (int)y, (int)x + PLAYER_WIDTH, (int)y + PLAYER_HEIGHT);
        super.set(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
        c.drawBitmap(bitmap, scr, dst, null);
    }
}
