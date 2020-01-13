package com.ashtray.carracinggame2d.feature_game_started;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.ashtray.carracinggame2d.helpers.GameScreenInfo;
import com.ashtray.carracinggame2d.log.LogHandler;

public class GameBoardView extends View{
    private static final String DEBUG_TAG = "GameBoardView";

    private GameComponentManager gameComponentManager;
    private Handler handler;

    public GameBoardView(Context context) {
        super(context);
        gameComponentManager = new GameComponentManager();
        handler = new Handler();
    }

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentX = (int) event.getX();
        if((GameScreenInfo.getInstance().getScreenWidth()/2) <= currentX){
            gameComponentManager.moveCarToRight();
        } else {
            gameComponentManager.moveCarToLeft();
        }
        return super.onTouchEvent(event);
    }

}
