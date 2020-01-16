package com.msapps.carracing.feature_game_started;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.View;

import com.msapps.carracing.CarGame2DApplication;
import com.msapps.carracing.interfaces.CarDataListener;
import com.st.BlueSTSDK.Manager;

public class GameBoardView extends View {
    private static final String DEBUG_TAG = "GameBoardView";

    private GameComponentManager gameComponentManager;
    private Handler handler;
    private SensorManager mSensorManager;
    int counter = 0;
    private boolean isGameStopped;


    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    public GameBoardView(Context context, CarDataListener listener) {
        super(context);
        gameComponentManager = new GameComponentManager(listener);
        handler = new Handler();




    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //long lStartTime = System.nanoTime();
        gameComponentManager.drawAllTheComponents(canvas);
        gameComponentManager.selfUpdateComponents();
        //long lEndTime = System.nanoTime();
        counter++;
        //long output = lEndTime - lStartTime;
        //LogHandler.d("GameBoardView", "timing difference " + output);
        handler.postDelayed(this::drawAgain, 10);



    }

    public void stopGame(){
        isGameStopped = true;
    }

    private void drawAgain() {
        //LogHandler.d(DEBUG_TAG, "drawing again");
        if(!isGameStopped) {
            invalidate();
        }

    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int currentX = (int) event.getX();
//        if((GameScreenInfo.getINSTANCE().getScreenWidth()/2) <= currentX){
//            gameComponentManager.moveCarToRight(intX);
//        } else {
//            gameComponentManager.moveCarToLeft(intX);
//        }
//        return super.onTouchEvent(event);
//    }

}
