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
import com.google.android.gms.ads.AdRequest;


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

        //since app is in pause state so close ads but in different thread so that user don't see anything
        new Handler().postDelayed(this::closeAds, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogHandler.d(DEBUG_TAG, "on resume called");

        //Initialize mobile ads after 1 second
        new Handler().postDelayed(this::loadAds, 100);
    }

    public void loadAds(){
        new Handler(Looper.getMainLooper()).post(() -> {
            if(!CarGame2DApplication.getInstance().isInternetAvailable()){
                LogHandler.d(DEBUG_TAG, "Internet is not available so no need to show ads.[return]");
                return;
            }


            LogHandler.d(DEBUG_TAG, "loading banner ads");
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("F8E6FE3BFDB10141A96AE737E9E9DBF1").build();
            //AdRequest adRequest = new AdRequest.Builder().build();;
        });
    }

    private void closeAds(){
        LogHandler.d(DEBUG_TAG, "closing ads");

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
