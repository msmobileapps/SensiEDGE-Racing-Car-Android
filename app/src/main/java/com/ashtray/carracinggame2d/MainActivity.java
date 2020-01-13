package com.ashtray.carracinggame2d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.ashtray.carracinggame2d.entities.MyFragment;
import com.ashtray.carracinggame2d.feature_game_home.GameHomeFragment;
import com.ashtray.carracinggame2d.feature_game_settings.GameSettingsFragment;
import com.ashtray.carracinggame2d.feature_game_started.GameStartedFragment;
import com.ashtray.carracinggame2d.log.LogHandler;



public class MainActivity extends AppCompatActivity implements MyFragment.MyFragmentCallBacks {
    private static final String DEBUG_TAG = "[MainActivity]";



    private MyFragment currentShowingFragmentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        showFragment(MyFragment.MyFragmentNames.GAME_HOME_FRAGMENT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogHandler.d(DEBUG_TAG, "on pause called");
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogHandler.d(DEBUG_TAG, "on resume called");

    }



    @Override
    public void onBackPressed() {
        if (!currentShowingFragmentObject.handleBackButtonPressed()) {
            if (!currentShowingFragmentObject.handleBackButtonPressed()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void showFragment(MyFragment.MyFragmentNames currentShowFragmentName) {
        LogHandler.d(DEBUG_TAG, "fragment changing request received, removing all the previous fragments");

        FragmentManager fragmentManager = getSupportFragmentManager();
        LogHandler.d(DEBUG_TAG, "previous fragments found total = " + fragmentManager.getBackStackEntryCount());
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }

        LogHandler.d(DEBUG_TAG, "Showing Fragment :"+ currentShowFragmentName);

        switch (currentShowFragmentName){
            case GAME_HOME_FRAGMENT:
                currentShowingFragmentObject = new GameHomeFragment(this);
                break;
            case GAME_STARTED_FRAGMENT:
                currentShowingFragmentObject = new GameStartedFragment(this);
                break;
            case GAME_SETTINGS_FRAGMENT:
                currentShowingFragmentObject = new GameSettingsFragment(this);
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, currentShowingFragmentObject);
        fragmentTransaction.show(currentShowingFragmentObject);
        fragmentTransaction.commit();
        LogHandler.d(DEBUG_TAG, "showing fragment done!");
    }
}
