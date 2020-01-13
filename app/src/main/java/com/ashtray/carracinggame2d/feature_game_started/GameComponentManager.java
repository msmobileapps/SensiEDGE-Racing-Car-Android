package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Canvas;

import java.util.ArrayList;

class GameComponentManager {

    private static final int GC_POS_GAME_SCREEN = 0;
    private static final int GC_POS_MY_CAR = 2;
    private static final int GC_POS_ENEMY_CAR = 3;
    private static final int GC_POS_GAME_ROAD = 1;

    private ArrayList<GameComponent> componentArrayList;

    GameComponentManager(){
        componentArrayList = new ArrayList<>();

        componentArrayList.add(GC_POS_GAME_SCREEN, new ComponentGameScreen());
        componentArrayList.add(GC_POS_GAME_ROAD, new ComponentRoad());
        componentArrayList.add(GC_POS_MY_CAR, new ComponentMyCar());
        componentArrayList.add(GC_POS_ENEMY_CAR, new ComponentEnemyCars());

        for(int i=0;i<componentArrayList.size();i++){
            componentArrayList.get(i).initializeGameComponent();
        }
    }

    void drawAllTheComponents(Canvas canvas){
        for(int i=0;i<componentArrayList.size();i++){
            componentArrayList.get(i).drawComponent(canvas);
        }
    }

    void selfUpdateComponents(){
        for(int i=0;i<componentArrayList.size();i++){
            componentArrayList.get(i).selfUpdatePosition();
        }
    }

    void moveCarToLeft(){
        ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).moveCarToLeft();
    }

    void moveCarToRight(){
        ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).moveCarToRight();
    }

}
