package com.ashtray.carracinggame2d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.ashtray.carracinggame2d.bluetooth.FeatureListActivity;
import com.ashtray.carracinggame2d.bluetooth.NodeContainerFragment;
import com.ashtray.carracinggame2d.entities.MyFragment;
import com.ashtray.carracinggame2d.feature_game_home.GameHomeFragment;
import com.ashtray.carracinggame2d.feature_game_settings.GameSettingsFragment;
import com.ashtray.carracinggame2d.feature_game_started.GameStartedFragment;
import com.ashtray.carracinggame2d.log.LogHandler;
import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.ArrayList;
import java.util.List;

import static com.ashtray.carracinggame2d.bluetooth.FeatureListActivity.NODE_FRAGMENT;


public class MainActivity extends AppCompatActivity implements MyFragment.MyFragmentCallBacks {
    private static final String DEBUG_TAG = "[MainActivity]";
    private static final float DELTA = -100f;
    private static final int MAX_SAMPLE_SIZE = 5;
    private int end = 1;
    private int start = 0;
    private final static String NODE_TAG = FeatureListActivity.class.getCanonicalName() + "" +
            ".NODE_TAG";
    ArrayList <Short> rollingAverage = new ArrayList<>();
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
        Intent i = getIntent();
        mNodeContainer = new NodeContainerFragment();
        mNodeContainer.setArguments(i.getExtras());

        getFragmentManager().beginTransaction()
                .add(mNodeContainer, NODE_FRAGMENT).commit();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        LogHandler.d(DEBUG_TAG, "on pause called");
        if (mNode != null) {
            mNode.removeNodeStateListener(mNodeStatusListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogHandler.d(DEBUG_TAG, "on resume called");
        if (mNode == null) {
            mNode = Manager.getSharedInstance().getNodeWithTag(getIntent().getStringExtra(NODE_TAG));
        }
        if (mNode != null) {
            mNode.addNodeStateListener(mNodeStatusListener);
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
    }

    private Node.NodeStateListener mNodeStatusListener = new Node.NodeStateListener() {
        @Override
        public void onStateChange(final Node node, Node.State newState, Node.State prevState) {
            if (newState == Node.State.Connected) {
                feature = mNode.getFeatures().get(2);
                feature.addFeatureListener((f, sample) -> {

                    LazySingleton.Companion.getINSTANCE().position = averageList((Short) sample.data[0]);
                });
                mNode.enableNotification(feature);
            }
        }
    };


    public int averageList(Short tallyUp) {
        if (rollingAverage.size() == 5) {
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


}
