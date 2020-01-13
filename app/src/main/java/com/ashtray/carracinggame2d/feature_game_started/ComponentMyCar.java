package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Canvas;

import com.ashtray.carracinggame2d.CarGame2DApplication;
import com.ashtray.carracinggame2d.database.DatabaseManager;
import com.ashtray.carracinggame2d.helpers.GameScreenInfo;

public class ComponentMyCar implements GameComponent {

    private Car myCar;

    private int[] carX;
    private static final int carY = GameScreenInfo.getInstance().getScreenHeight() - Car.carHeight - 5; //my car's y pos is fixed

    @Override
    public void initializeGameComponent() {
        int totalRoadLean = DatabaseManager.getInstance().getSelectedRoadLean();
        int roadWidth = ComponentRoad.perRoadLeanWidth * totalRoadLean;
        carX = new int[totalRoadLean];

        int roadX = (GameScreenInfo.getInstance().getScreenWidth() - roadWidth) / 2;
        carX[0] = roadX + (ComponentRoad.perRoadLeanWidth / 2) - (Car.carWidth / 2);
        for(int i=1;i<totalRoadLean;i++){
            carX[i] = carX[i-1] + ComponentRoad.perRoadLeanWidth;
        }

        int currentLen = 1;
        myCar = new Car(carX[currentLen], carY, currentLen, CarGame2DApplication.getInstance().getSelectedCarImage());
    }

    @Override
    public void drawComponent(Canvas canvas) {
        myCar.drawCar(canvas);
    }

    @Override
    public void selfUpdatePosition() {
        //no self update for this
    }

    void moveCarToLeft(){
        if(myCar.getCarLean() != 0){
            myCar.setCarLean(myCar.getCarLean()-1);
            myCar.setxPosition(carX[myCar.getCarLean()]);
        }
    }

    void moveCarToRight(){
        if(myCar.getCarLean() != carX.length-1){
            myCar.setCarLean(myCar.getCarLean()+1);
            myCar.setxPosition(carX[myCar.getCarLean()]);
        }
    }
}
