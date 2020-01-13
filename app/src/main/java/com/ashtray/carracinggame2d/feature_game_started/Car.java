package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Car{

    static final int carWidth = 50;
    static final int carHeight = 100;

    private Bitmap carImage;
    private int xPosition;
    private int yPosition;
    private int carLean;
    private CRect rect;

    Car(int xPosition, int yPosition, int carLean, Bitmap carImage){
        this.xPosition = xPosition;
        this.yPosition = yPosition-100;
        this.carLean = carLean;
        this.carImage = carImage;
    }

    void drawCar(Canvas canvas){
        canvas.drawBitmap(carImage, xPosition, yPosition, new Paint());

    }

    int getxPosition() {
        return xPosition;
    }



    int getyPosition() {
        return yPosition;
    }

    public CRect getRect() {
        CRect rect = new CRect();
        rect.setBounds(xPosition, yPosition, carWidth, carHeight);
        return rect;
    }

    int getCarLean() {
        return carLean;
    }

    void setxPosition(int carX) {
        this.xPosition = carX;
    }

    void setyPosition(int carY) {
        this.yPosition = carY;
    }

    void setCarLean(int carLean) {
        this.carLean = carLean;
    }

    public Bitmap getCarImage(){
        return carImage;
    }
}
