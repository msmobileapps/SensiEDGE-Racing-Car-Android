package com.ashtray.carracinggame2d.feature_game_started;

import android.graphics.Canvas;

public interface GameComponent {
    void initializeGameComponent();
    void drawComponent(Canvas canvas);
    void selfUpdatePosition();
}
