package hk.polyu.eie.eie3109.task2_19016111d;

import android.graphics.Bitmap;

public class GameObject {
    private Coordinates coordinates;
    private GameObjectSize size;

    private int panelHeight = 0, panelWidth = 0;

    public GameObject(){
        coordinates = new Coordinates();
        size = new GameObjectSize();
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public void setSize(int width, int height){
        this.size.setWidth(width);
        this.size.setHeight(height);
    }

    public int getWidth(){
        return this.size.getWidth();
    }

    public int getHeight(){
        return this.size.getHeight();
    }

    public void set(double x, double y, int width, int height){
        this.coordinates.setX(x);
        this.coordinates.setY(y);
        setSize(width, height);
    }

    public void setPanelWidthHeight(int width, int height){
        this.panelHeight = height;
        this.panelWidth = width;
    }

    public int getPanelHeight(){
        return panelHeight;
    }

    public int getPanelWidth(){
        return panelWidth;
    }
}
