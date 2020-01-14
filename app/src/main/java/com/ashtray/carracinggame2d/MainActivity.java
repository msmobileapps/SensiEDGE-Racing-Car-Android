package com.ashtray.carracinggame2d;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashtray.carracinggame2d.entities.MyFragment;
import com.ashtray.carracinggame2d.feature_game_home.GameHomeFragment;
import com.ashtray.carracinggame2d.feature_game_settings.GameSettingsFragment;
import com.ashtray.carracinggame2d.feature_game_started.GameStartedFragment;
import com.ashtray.carracinggame2d.interfaces.CarDataListener;
import com.ashtray.carracinggame2d.log.LogHandler;

import static com.ashtray.carracinggame2d.entities.MyFragment.MyFragmentNames.GAME_HOME_FRAGMENT;


public class MainActivity extends AppCompatActivity implements MyFragment.MyFragmentCallBacks, CarDataListener {
    private static final String DEBUG_TAG = "[MainActivity]";
    private static final float DELTA = -100f;
    private static int ANIMATION_DURATION = 1000;
    public static final int GAME_LIVES = 3;

    private MyFragment currentShowingFragmentObject;
    private ImageView mImageView;
    private TranslateAnimation mAnimation;
    private int distance;
    private TextView score_tv;
    private LinearLayout cars_container;
    private Handler accelerateHandler;
    private Runnable accelerateRunnble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewInit();
        showFragment(MyFragment.MyFragmentNames.GAME_STARTED_FRAGMENT);
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

    private void addCars() {
        if (cars_container != null) {
            cars_container.removeAllViews();
            for (int i = 0; i < GAME_LIVES; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.car5);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(60, 65);
                cars_container.addView(imageView, lp);
            }
        }
    }

    private void viewInit() {
        distance = getResources().getDimensionPixelSize(R.dimen.distance);
        mImageView = findViewById(R.id.road);
        score_tv = findViewById(R.id.score_tv);
        cars_container = findViewById(R.id.cars_container);
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

        LogHandler.d(DEBUG_TAG, "Showing Fragment :" + currentShowFragmentName);

        switch (currentShowFragmentName) {
            case GAME_HOME_FRAGMENT:
                currentShowingFragmentObject = new GameHomeFragment(this);
                break;
            case GAME_STARTED_FRAGMENT:
                currentShowingFragmentObject = new GameStartedFragment(this, this);
                addCars();
                animateRoad();
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

    public void animateRoad() {
        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, distance);

        mAnimation.setDuration(ANIMATION_DURATION);
        mAnimation.setRepeatCount(-1);
        mAnimation.setInterpolator(new LinearInterpolator());
        mImageView.setAnimation(mAnimation);
        accelarete(false);

    }

    private void accelarete(boolean stop) {


        if (!stop) {
            accelerateHandler = new Handler();
            final int[] counter = {0};

            accelerateRunnble = new Runnable() {
                public void run() {

                    ANIMATION_DURATION -= counter[0];
                    counter[0]++;
                    score_tv.setText(String.valueOf(counter[0]));

                    mAnimation.setDuration((ANIMATION_DURATION <= 0) ? 1 : ANIMATION_DURATION);
                    accelerateHandler.postDelayed(this, 1000);


                }
            };

            accelerateHandler.post(accelerateRunnble);
        } else {
            accelerateHandler.removeCallbacks(accelerateRunnble);
        }

    }


    @Override
    public void carHited() {
        if (cars_container.getChildCount() != 0) {
            cars_container.removeViewAt(cars_container.getChildCount() - 1);
        }

        if (cars_container.getChildCount() == 0) {
            gameOver();
        }
    }


    private void gameOver() {

        addCars();
        mAnimation.cancel();
        accelarete(true);
        ((GameStartedFragment)currentShowingFragmentObject).getGameBoardView().stopGame();
        showGameOverDialog();
    }


    private void showGameOverDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("GameOver");
        alertDialog.setMessage("Your score is " + score_tv.getText());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> {
                    showFragment(GAME_HOME_FRAGMENT);
                    dialog.dismiss();
                });
        alertDialog.show();
    }
}
