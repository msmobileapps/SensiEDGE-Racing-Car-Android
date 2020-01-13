package com.ashtray.carracinggame2d.feature_game_started;

import androidx.constraintlayout.solver.widgets.Rectangle;

public class CRect extends Rectangle {

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }

    public boolean intersects(Rectangle bounds) {
        return this.x >= bounds.x && this.x < bounds.x + bounds.width && this.y >= bounds.y && this.y < bounds.y + bounds.height;
    }



}
