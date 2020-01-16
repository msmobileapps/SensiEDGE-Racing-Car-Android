package com.msapps.carracing.feature_game_started;

import android.graphics.Canvas;

import com.msapps.carracing.CarGame2DApplication;
import com.msapps.carracing.database.DatabaseManager;
import com.msapps.carracing.helpers.GameScreenInfo;
import com.msapps.carracing.interfaces.OnCarPositionChanged;

import java.util.ArrayList;
import java.util.Random;

public class ComponentEnemyCars implements GameComponent {

    private static final int maxSpeed = 200;
    private int minDisBetweenTwoEnemyCar = Car.carHeight * 4;
    private ArrayList<Car> enemyCars;
    private int speed;
    private OnCarPositionChanged onCarPositionChanged;

    public ComponentEnemyCars(OnCarPositionChanged listener) {
        this.onCarPositionChanged = listener;
    }


    @Override
    public void initializeGameComponent() {
        enemyCars = new ArrayList<>();
        speed = 5;
    }

    @Override
    public void drawComponent(Canvas canvas) {
        for (int i = 0; i < enemyCars.size(); i++) {
            enemyCars.get(i).drawCar(canvas);
        }
    }


    @Override
    public void selfUpdatePosition() {

        for (int i = 0; i < enemyCars.size(); i++) {
            int previousyPosition = enemyCars.get(i).getyPosition();

            Car eCar = enemyCars.get(i);
            eCar.setyPosition(previousyPosition + speed);

            if (onCarPositionChanged != null) {
                onCarPositionChanged.enemyCarPositionChanged(eCar);
            }
        }

        //removing enemy cars which one is out of screen
        ArrayList<Car> temp = new ArrayList<>();
        for (int i = 0; i < enemyCars.size(); i++) {
            int currentyPosition = enemyCars.get(i).getyPosition();
            if (currentyPosition < GameScreenInfo.getInstance().getScreenHeight() + 10) {
                temp.add(enemyCars.get(i));
            }
        }
        enemyCars = temp;

        //creating new enemy car randomly
        if (enemyCars.size() == 0 || enemyCars.get(enemyCars.size() - 1).getyPosition() > minDisBetweenTwoEnemyCar) {
            Car eCar = chooseARandomCar();
            enemyCars.add(eCar);
            minDisBetweenTwoEnemyCar = Math.max(minDisBetweenTwoEnemyCar - 5, Car.carHeight * 4);
            if (minDisBetweenTwoEnemyCar >= Car.carHeight * 2) {
                speed = Math.min(speed + 1, maxSpeed);
            }

        }
    }



    private Car chooseARandomCar() {
        Random random = new Random();
        int totalRoadLean = DatabaseManager.getInstance().getSelectedRoadLean();
        int[] carX = new int[totalRoadLean];
        int carY = 0 - Car.carHeight - 70; //initial car position is fixed

        int roadWidth = ComponentRoad.perRoadLeanWidth * totalRoadLean;
        int roadX = (GameScreenInfo.getInstance().getScreenWidth() - roadWidth) / 2;

        carX[0] = roadX+4 ;
        for (int i = 1; i < totalRoadLean; i++) {
            carX[i] = carX[i - 1] + ComponentRoad.perRoadLeanWidth;
        }

        int randomLean = random.nextInt(totalRoadLean);
        int randomCarNumber = random.nextInt(7) + 1; // +1 is for ignoring 0
        return new Car(carX[randomLean], carY, randomLean, CarGame2DApplication.getInstance().getCarAccordingToNumber(randomCarNumber));
    }
}
