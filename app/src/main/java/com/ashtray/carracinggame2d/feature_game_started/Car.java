package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;


import com.ashtray.carracinggame2d.CarGame2DApplication;


public class Car {

    static final int carWidth = 50;
    static final int carHeight = 100;

    private Bitmap carImage;
    private int xPosition;
    private int yPosition;
    private int carLean;
    private CRect rect;
    private int[] sourceLocation;
    private Canvas canvas;
    private Paint paint;


    Car(int xPosition, int yPosition, int carLean, Bitmap carImage) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.carLean = carLean;
        this.carImage = carImage;


    }

    void drawCar(Canvas canvas) {
        this.canvas = canvas;
        paint = new Paint();
        canvas.drawBitmap(carImage, xPosition, yPosition, paint);

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

    public Bitmap getCarImage() {
        return carImage;
    }

    public void animateBoom() {
        final Handler handler = new Handler();
        final int[] count = {0};

        final Runnable runnable = new Runnable() {
            public void run() {
                // need to do tasks on the UI thread
                if ((count[0] % 2) == 0) {
                    carImage = CarGame2DApplication.getInstance().getSelectedCarBoom();
                } else {
                    carImage = CarGame2DApplication.getInstance().getSelectedCarImage();
                }
                if (count[0]++ < 5) {
                    handler.postDelayed(this, 500);
                }
            }
        };

        handler.post(runnable);


    }


}
