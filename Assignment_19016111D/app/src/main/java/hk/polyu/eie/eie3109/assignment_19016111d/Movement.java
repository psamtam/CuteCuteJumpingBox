package hk.polyu.eie.eie3109.assignment_19016111d;

public class Movement {
    public static final int X_DIRECTION_RIGHT = 1;
    public static final int X_DIRECTION_LEFT = -1;
    public static final int Y_DIRECTION_DOWN = 1;
    public static final int Y_DIRECTION_UP = -1;

    private int xSpeed = 2;
    private int ySpeed = 2;

    private int xDirection = X_DIRECTION_RIGHT;
    private int yDirection = Y_DIRECTION_DOWN;

    public void setXYSpeed(int x, int y){
        this.xSpeed = x;
        this.ySpeed = y;
    }

    public void setDirections(int xDirection, int yDirection){
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public void toggleXDirection(){
        if (xDirection == X_DIRECTION_LEFT){
            xDirection = X_DIRECTION_RIGHT;
        } else {
            xDirection = X_DIRECTION_LEFT;
        }
    }

    public void toggleYDirection(){
        if (yDirection == Y_DIRECTION_DOWN){
            yDirection = Y_DIRECTION_UP;
        } else {
            yDirection = Y_DIRECTION_DOWN;
        }
    }

    public int getXSpeed(){
        return xSpeed;
    }

    public int getYSpeed(){
        return ySpeed;
    }

    public int getXDirection(){
        return xDirection;
    }

    public int getYDirection(){
        return yDirection;
    }
}
