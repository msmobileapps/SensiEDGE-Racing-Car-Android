package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Canvas;
import android.util.Log;

import com.ashtray.carracinggame2d.CarGame2DApplication;
import com.ashtray.carracinggame2d.LazySingleton;
import com.ashtray.carracinggame2d.database.DatabaseManager;
import com.ashtray.carracinggame2d.helpers.GameScreenInfo;

public class ComponentMyCar implements GameComponent {

    private Car myCar;

    private int[] carX;
    private static final int carY = GameScreenInfo.getInstance().getScreenHeight() - Car.carHeight - 5; //my car's y pos is fixed

    @Override
    public void initializeGameComponent() {
        int totalRoadLean = 3;
        int roadWidth = ComponentRoad.perRoadLeanWidth * totalRoadLean;
        carX = new int[totalRoadLean];

        int roadX = (GameScreenInfo.getInstance().getScreenWidth() - roadWidth) / 2;
        carX[0] = roadX + (ComponentRoad.perRoadLeanWidth / 2) - (Car.carWidth / 2);
        for(int i=1;i<totalRoadLean;i++){
            carX[i] = carX[i-1] + ComponentRoad.perRoadLeanWidth;
        }

        int currentLen = 1;
        myCar = new Car(carX[currentLen], (carY - 150) , currentLen, CarGame2DApplication.getInstance().getSelectedCarImage());
    }

    @Override
    public void drawComponent(Canvas canvas) {
        myCar.drawCar(canvas);
    }

    @Override
    public void selfUpdatePosition() {
        float pos = LazySingleton.Companion.getINSTANCE().position / 200.0f;
        int position = (int) (ComponentRoad.perRoadLeanWidth*2 - pos * (ComponentRoad.perRoadLeanWidth / 4));
        Log.e("barak",LazySingleton.Companion.getINSTANCE().position+" lazy" + "   |  pos:"+pos+ "   |  position:"+position);
        myCar.setxPosition(position);
    }

    public Car getMyCar(){
        return myCar;
    }

    void moveCarToLeft(){
        myCar.setxPosition(carX[0]);
    }
    void moveCarToCenter(){
        myCar.setxPosition(carX[1]);
    }

    void moveCarToRight(){
        myCar.setxPosition(carX[2]);
    }

    public void moveCarTo(float x) {
//        int position = (int) (ComponentRoad.perRoadLeanWidth*2 - x * (ComponentRoad.perRoadLeanWidth / 4));
//        myCar.setxPosition(position);
    }


}
