package hk.polyu.eie.eie3109.task2_19016111d;

public class Coordinates {
    private double x = 200;
    private double y = 200;

    public Coordinates(){

    }

    public void setX(double x){
        //this.x = x - bitmap.getWidth()/2;
        this.x = x;
    }

    public void setY(double y){
        //this.y = y - bitmap.getHeight()/2;
        this.y = y;
    }

    public double getX(){
        //return x + bitmap.getWidth()/2;
        return x;
    }

    public double getY(){
        //return y + bitmap.getHeight()/2;
        return y;
    }
}
