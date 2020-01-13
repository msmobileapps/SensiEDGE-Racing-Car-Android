package com.ashtray.carracinggame2d.feature_game_started;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashtray.carracinggame2d.entities.MyFragment;

public class GameStartedFragment extends MyFragment {

    public GameStartedFragment(MyFragmentCallBacks callBacks) {
        super(callBacks);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new GameBoardView(getContext());
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }
}
