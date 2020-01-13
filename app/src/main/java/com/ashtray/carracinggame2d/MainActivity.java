package com.ashtray.carracinggame2d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        showFragment(MyFragment.MyFragmentNames.GAME_HOME_FRAGMENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
