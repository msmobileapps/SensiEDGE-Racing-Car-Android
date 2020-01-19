package com.msapps.carracing;

import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import com.msapps.carracing.bluetooth.FeatureListActivity;
import com.msapps.carracing.bluetooth.NodeContainerFragment;
import com.msapps.carracing.entities.MyFragment;
import com.msapps.carracing.feature_game_home.GameHomeFragment;
import com.msapps.carracing.feature_game_settings.GameSettingsFragment;
import com.msapps.carracing.feature_game_started.GameStartedFragment;
import com.msapps.carracing.interfaces.CarDataListener;
import com.msapps.carracing.log.LogHandler;
import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MyFragment.MyFragmentCallBacks, CarDataListener {
    private static final String DEBUG_TAG = "[MainActivity]";
    private static final float DELTA = -100f;
    private static int MAX_SAMPLE_SIZE = 7;
    public static boolean enableDeviceAcc = true;
    private int end = 1;
    private int start = 0;
    private final static String NODE_TAG = FeatureListActivity.class.getCanonicalName() + "" +
            ".NODE_TAG";
    ArrayList<Short> rollingAverage = new ArrayList<>();
    private static int ANIMATION_DURATION = 1000;
    public static final int GAME_LIVES = 3;

    private MyFragment currentShowingFragmentObject;
    private ImageView mImageView;
    private int[] sourceLocation;
    private ObjectAnimator mAnimationSet;
    private TranslateAnimation mAnimation;
    private float ff = 1000f;
    private int distance;
    private Feature feature;
    private Node mNode;
    private NodeContainerFragment mNodeContainer;
    private TextView scoreTv;
    private LinearLayout cars_container;
    private Handler accelerateHandler;
    private int counter;
    private SensorManager mSensorManager;

    public static Intent getStartIntent(Context c, @NonNull Node node) {
        Intent i = new Intent(c, MainActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        return i;
    }//getStartIntent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        distance = getResources().getDimensionPixelSize(R.dimen.distance);
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
        mImageView = findViewById(R.id.road);
        animateRoad();
        mNode = Manager.getSharedInstance().getNodeWithTag(getIntent().getStringExtra(NODE_TAG));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (enableDeviceAcc && mSensorManager != null){
            mSensorManager.unregisterListener(mSensorListener);
        }

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
        scoreTv = findViewById(R.id.score_tv);
        cars_container = findViewById(R.id.cars_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogHandler.d(DEBUG_TAG, "on resume called");
        if (mNode == null) {
            mNode = Manager.getSharedInstance().getNodeWithTag(getIntent().getStringExtra(NODE_TAG));
        }
        if (mNode != null) {
            feature = mNode.getFeatures().get(2);
            feature.addFeatureListener((f, sample) -> {
                LazySingleton.Companion.getINSTANCE().position = averageList((Short) sample.data[0]);
            });
            mNode.enableNotification(feature);
        }else if (enableDeviceAcc){
            mSensorManager = (SensorManager) CarGame2DApplication.getInstance().getSystemService(Context.SENSOR_SERVICE);
            mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
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
        mAnimation.setDuration(700);
        mAnimation.setRepeatCount(-1);
        mAnimation.setInterpolator(new LinearInterpolator());
        mImageView.setAnimation(mAnimation);
        accelarete(false);
    }

    private void accelarete(boolean stop) {
        if (!stop) {
            accelerateHandler = new Handler();
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
        ANIMATION_DURATION = 1000;
        counter = 0;
        accelerateHandler.removeCallbacks(accelerateRunnble);
        addCars();
        mAnimation.cancel();
        accelarete(true);
        ((GameStartedFragment) currentShowingFragmentObject).getGameBoardView().stopGame();
        showGameOverDialog();
    }


    private void showGameOverDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Game Over");
        alertDialog.setMessage( scoreTv.getText());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "play again",
                (dialog, which) -> {
                    dialog.dismiss();
                    showFragment(MyFragment.MyFragmentNames.GAME_STARTED_FRAGMENT);
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit",
                (dialog, which) -> {
                    finish();
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public int averageList(Short tallyUp) {
        if (rollingAverage.size() == MAX_SAMPLE_SIZE) {
            rollingAverage.remove(0);
        }
        rollingAverage.add(tallyUp);
        int total = 0;
        for (Short item : rollingAverage) {
            total = (total + item);
        }
        total = total / rollingAverage.size();

        return total;
    }
    private Runnable accelerateRunnble = new Runnable() {
        public void run() {
            ANIMATION_DURATION -= counter;
            counter++;
            scoreTv.setText(String.format(getString(R.string.your_score),counter));
            mAnimation.setDuration((ANIMATION_DURATION <= 500) ? 500 : ANIMATION_DURATION);
            accelerateHandler.postDelayed(this, 1000);
        }
    };

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            LazySingleton.Companion.getINSTANCE().position = averageList((short) (se.values[0]*170));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
