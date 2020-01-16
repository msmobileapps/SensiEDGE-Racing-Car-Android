package com.msapps.carracing.helpers;

import android.content.res.Resources;
public class GameScreenInfo {

    private static GameScreenInfo gameScreenInfo;
    public static GameScreenInfo getInstance(){
        if(gameScreenInfo == null){
            gameScreenInfo = new GameScreenInfo();
        }
        return gameScreenInfo;
    }

    private int screenWidth;
    private int screenHeight;

    private GameScreenInfo(){
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
