package com.msapps.carracing.feature_game_started;

import android.graphics.Canvas;
import android.util.Log;

import com.msapps.carracing.interfaces.CarDataListener;
import com.msapps.carracing.interfaces.OnCarPositionChanged;

import java.util.ArrayList;

class GameComponentManager implements OnCarPositionChanged {

    private static final int GC_POS_GAME_SCREEN = 0;
    private static final int GC_POS_MY_CAR = 2;
    private static final int GC_POS_ENEMY_CAR = 3;
    private static final int GC_POS_GAME_ROAD = 1;


    private ArrayList<GameComponent> componentArrayList;
    private Car myCar;
    private Car enemyHitedCar;
    private CarDataListener carDataListener;

    GameComponentManager(CarDataListener carDataListener) {
        this.carDataListener = carDataListener;
        componentArrayList = new ArrayList<>();

        componentArrayList.add(GC_POS_GAME_SCREEN, new ComponentGameScreen());
        componentArrayList.add(GC_POS_GAME_ROAD, new ComponentRoad());
        componentArrayList.add(GC_POS_MY_CAR, new ComponentMyCar());
        componentArrayList.add(GC_POS_ENEMY_CAR, new ComponentEnemyCars(this));

        for (int i = 0; i < componentArrayList.size(); i++) {
            componentArrayList.get(i).initializeGameComponent();
        }
    }

    void drawAllTheComponents(Canvas canvas) {
        for (int i = 0; i < componentArrayList.size(); i++) {
            componentArrayList.get(i).drawComponent(canvas);
        }
    }

    void selfUpdateComponents() {
        for (int i = 0; i < componentArrayList.size(); i++) {
            componentArrayList.get(i).selfUpdatePosition();

            if (componentArrayList.get(i) instanceof ComponentEnemyCars) {

                myCar = ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).getMyCar();

            }

        }

        int xPOs = ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).getMyCar().getxPosition();
        int yPOs = ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).getMyCar().getyPosition();

        Log.d("IgorTest", "My car X position is = " + xPOs + "My car Y position is = " + yPOs);

    }

    void moveCarToLeft() {
        ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).moveCarToLeft();
    }

    void moveCarToCenter() {
        ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).moveCarToCenter();
    }

    void moveCarToRight() {
        ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).moveCarToRight();
    }


    @Override
    public void enemyCarPositionChanged(Car enemyCar) {

        if (myCar != null && enemyCar != null) {

            if (enemyHitedCar != null && enemyHitedCar != enemyCar) {
                if (isCarCrash(enemyCar)) {
                    if(carDataListener != null){
                        carDataListener.carHited();
                    }
                    myCar.animateBoom();
                }
            } else if (enemyHitedCar == null) {
                if (isCarCrash(enemyCar)) {
                    if(carDataListener != null){
                        carDataListener.carHited();
                    }
                    myCar.animateBoom();
                }
            }
        }
    }


    private boolean isCarCrash(Car enemyCar) {
        if (isCarinXRange(myCar.getRect().x, enemyCar.getRect().x)) {
            if (((myCar.getRect().y - myCar.carHeight) <= (enemyCar.getRect().y + enemyCar.carHeight)) || myCar.getRect().intersects(enemyCar.getRect())) {
                enemyHitedCar = enemyCar;
                return true;
            }
        }
        return false;

    }

    private boolean isCarinXRange(int oneX, int secInt) {
        if (oneX > secInt) {
            if ((oneX - secInt) <= 101) {
                return true;
            }
        } else {
            if ((secInt - oneX) <= 101) {
                return true;
            }
        }
        return false;
    }

    public void moveCarTo(float x) {
        ((ComponentMyCar) componentArrayList.get(GC_POS_MY_CAR)).moveCarTo(x);
    }


}

