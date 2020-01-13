package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.ashtray.carracinggame2d.CarGame2DApplication;
import com.ashtray.carracinggame2d.R;
import com.ashtray.carracinggame2d.database.DatabaseManager;
import com.ashtray.carracinggame2d.helpers.GameScreenInfo;
import com.ashtray.carracinggame2d.log.LogHandler;

public class ComponentRoad implements GameComponent {

    final static int perRoadLeanWidth = (int) (GameScreenInfo.getInstance().getScreenWidth()/5);

    private int total_road_lean;
    private int road_width, road_height;
    private int road_x, road_y;
    private int road_boundary_width, road_boundary_height;
    private int left_road_boundary_x, left_road_boundary_y, right_road_boundary_x, right_road_boundary_y;
    private int road_divider_width, road_divider_height;

    @Override
    public void initializeGameComponent() {
        total_road_lean = DatabaseManager.getInstance().getSelectedRoadLean();
        LogHandler.d("total_road_elan", String.valueOf(total_road_lean));

        road_width = perRoadLeanWidth * total_road_lean;
        road_height = GameScreenInfo.getInstance().getScreenHeight();

        road_x = (GameScreenInfo.getInstance().getScreenWidth() - road_width) / 2;
        road_y = 0;

        road_boundary_width = 15;
        road_boundary_height = road_height;

        left_road_boundary_x = road_x - road_boundary_width;
        left_road_boundary_y = road_y;
        right_road_boundary_x = road_x + road_width;
        right_road_boundary_y = road_y;

        road_divider_width = 2;
        road_divider_height = GameScreenInfo.getInstance().getScreenHeight();
    }

    @Override
    public void drawComponent(Canvas canvas) {
//
//        Drawable d = CarGame2DApplication.getInstance().getResources().getDrawable(R.drawable.road, null);
//        d.setBounds(road_x, road_y, road_x+road_width, road_y+road_height);
//        d.draw(canvas);
    }


    @Override
    public void selfUpdatePosition() {
        //no update for roads
    }
}