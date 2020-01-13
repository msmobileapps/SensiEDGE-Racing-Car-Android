package com.ashtray.carracinggame2d.feature_game_home;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashtray.carracinggame2d.R;
import com.ashtray.carracinggame2d.entities.MyFragment;

public class GameHomeFragment extends MyFragment {

    public GameHomeFragment(MyFragmentCallBacks callBacks) {
        super(callBacks);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_home, container, false);
        v.findViewById(R.id.ButtonPlay).setOnClickListener(v1 -> callBacks.showFragment(MyFragmentNames.GAME_STARTED_FRAGMENT));
        v.findViewById(R.id.ButtonSettings).setOnClickListener(v1 -> callBacks.showFragment(MyFragmentNames.GAME_SETTINGS_FRAGMENT));
        return v;
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }
}
