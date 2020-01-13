package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ashtray.carracinggame2d.database.DatabaseManager;
import com.ashtray.carracinggame2d.helpers.GameScreenInfo;
import com.ashtray.carracinggame2d.log.LogHandler;

public class ComponentRoad implements GameComponent {

    final static int perRoadLeanWidth = 200;

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
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        drawWholeRoadBackground(canvas, paint);
        drawBoundariesOfTheRoad(canvas, paint);
        drawRoadDivider(canvas, paint);
    }

    private void drawRoadDivider(Canvas canvas, Paint paint) {
        paint.setColor(Color.YELLOW);
        for(int i=1;i<total_road_lean;i++){
            int roadDividerX = road_x + (road_width/total_road_lean)*i + (road_divider_width/2);
            int roadDividerY = road_y;
            canvas.drawRect(roadDividerX,roadDividerY, roadDividerX+road_divider_width, road_divider_height, paint);
        }
    }

    private void drawBoundariesOfTheRoad(Canvas canvas, Paint paint) {
        paint.setColor(Color.YELLOW);
        canvas.drawRect(left_road_boundary_x, left_road_boundary_y, left_road_boundary_x + road_boundary_width, road_boundary_height, paint);
        canvas.drawRect(right_road_boundary_x, right_road_boundary_y, right_road_boundary_x + road_boundary_width, road_boundary_height, paint);
    }

    private void drawWholeRoadBackground(Canvas canvas, Paint paint) {
        paint.setColor(Color.RED);
        canvas.drawRect(road_x, road_y, road_x+road_width, road_y+road_height, paint);
    }

    @Override
    public void selfUpdatePosition() {
        //no update for roads
    }
}