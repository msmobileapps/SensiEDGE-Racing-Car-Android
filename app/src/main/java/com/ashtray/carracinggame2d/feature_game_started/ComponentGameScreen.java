package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ComponentGameScreen implements GameComponent {

    @Override
    public void initializeGameComponent() {

    }

    @Override
    public void drawComponent(Canvas canvas){
        Paint painter = new Paint();
        painter.setStyle(Paint.Style.FILL);
        painter.setColor(Color.WHITE);
        canvas.drawPaint(painter);
    }

    @Override
    public void selfUpdatePosition() {

    }
}
