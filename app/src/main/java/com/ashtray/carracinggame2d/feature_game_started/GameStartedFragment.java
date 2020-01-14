package com.ashtray.carracinggame2d.feature_game_started;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashtray.carracinggame2d.entities.MyFragment;
import com.ashtray.carracinggame2d.interfaces.CarDataListener;

public class GameStartedFragment extends MyFragment {

    private CarDataListener carDataListener;

    public GameStartedFragment(MyFragmentCallBacks callBacks, CarDataListener carDataListener) {
        super(callBacks);
        this.carDataListener = carDataListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new GameBoardView(getContext(), carDataListener);
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }
}
