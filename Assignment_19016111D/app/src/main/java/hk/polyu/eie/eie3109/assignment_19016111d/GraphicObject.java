package hk.polyu.eie.eie3109.assignment_19016111d;

import android.graphics.Bitmap;

public class GraphicObject {
    private Bitmap bitmap;
    private Coordinates coordinates;
    private Movement movement;

    public GraphicObject(Bitmap bitmap){
        this.bitmap = bitmap;
        coordinates = new Coordinates(bitmap);
        this.movement = new Movement();
    }

    public Bitmap getGraphic(){
        return bitmap;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public Movement getMovement(){
        return movement;
    }

    public boolean clickedOnObject(int x, int y){
        int xmin = this.coordinates.getX();
        int xmax = this.coordinates.getX() + bitmap.getWidth();
        int ymin = this.coordinates.getY();
        int ymax = this.coordinates.getY() + bitmap.getHeight();

        if ( ((x > xmin) && (x < xmax)) && ((y > ymin) && (y < ymax))){
            return true;
        }
        return false;
    }
}
