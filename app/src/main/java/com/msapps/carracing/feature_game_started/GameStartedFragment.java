package com.msapps.carracing.feature_game_started;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msapps.carracing.entities.MyFragment;
import com.msapps.carracing.interfaces.CarDataListener;

public class GameStartedFragment extends MyFragment {

    private CarDataListener carDataListener;
    private GameBoardView gameBoardView;


    public GameStartedFragment(MyFragmentCallBacks callBacks, CarDataListener carDataListener) {
        super(callBacks);
        this.carDataListener = carDataListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gameBoardView = new GameBoardView(getContext(), carDataListener);
        return gameBoardView;
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }

    public GameBoardView getGameBoardView(){
        return gameBoardView;
    }
}
