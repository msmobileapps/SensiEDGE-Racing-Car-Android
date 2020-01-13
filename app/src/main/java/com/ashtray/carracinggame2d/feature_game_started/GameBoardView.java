package com.ashtray.carracinggame2d.feature_game_started;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.ashtray.carracinggame2d.CarGame2DApplication;
import com.ashtray.carracinggame2d.helpers.GameScreenInfo;

public class GameBoardView extends View{
    private static final String DEBUG_TAG = "GameBoardView";

    private GameComponentManager gameComponentManager;
    private Handler handler;
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    public GameBoardView(Context context) {
        super(context);
        gameComponentManager = new GameComponentManager();
        handler = new Handler();
        mSensorManager = (SensorManager) CarGame2DApplication.getInstance().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

//        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            int intX = (int) x;
            if (intX<=-1){
                gameComponentManager.moveCarToRight();
            }else if (intX>0){
                gameComponentManager.moveCarToLeft();
            }else {
                gameComponentManager.moveCarToCenter();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //long lStartTime = System.nanoTime();
        gameComponentManager.drawAllTheComponents(canvas);
        gameComponentManager.selfUpdateComponents();
        //long lEndTime = System.nanoTime();

        //long output = lEndTime - lStartTime;
        //LogHandler.d("GameBoardView", "timing difference " + output);
        handler.postDelayed(this::drawAgain, 10);
    }

    private void drawAgain(){
        //LogHandler.d(DEBUG_TAG, "drawing again");
        invalidate();
    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int currentX = (int) event.getX();
//        if((GameScreenInfo.getInstance().getScreenWidth()/2) <= currentX){
//            gameComponentManager.moveCarToRight(intX);
//        } else {
//            gameComponentManager.moveCarToLeft(intX);
//        }
//        return super.onTouchEvent(event);
//    }

}
