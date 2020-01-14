package com.ashtray.carracinggame2d;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ashtray.carracinggame2d.database.DatabaseManager;
import com.ashtray.carracinggame2d.log.LogHandler;

public class CarGame2DApplication extends Application {
    private static final String DEBUG_TAG = "[App]";

    private static CarGame2DApplication application;

    public static CarGame2DApplication getInstance() {
        return application;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        LogHandler.d(DEBUG_TAG, "onCreate called");
        application = this;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean ret = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        LogHandler.d(DEBUG_TAG, "isInternetAvailable : " + ret);
        return ret;
    }

    public Bitmap getSelectedCarImage() {
        return getCarAccordingToNumber(DatabaseManager.getInstance().getSelectedCarNumber());
    }

    public Bitmap getSelectedCarBoom() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.car_boom);
    }

    public Bitmap getCarAccordingToNumber(int number) {
        switch (number) {
            case 1:
                return BitmapFactory.decodeResource(getResources(), R.drawable.car1);
            case 2:
                return BitmapFactory.decodeResource(getResources(), R.drawable.car2);
            case 3:
                return BitmapFactory.decodeResource(getResources(), R.drawable.car3);
            case 4:
                return BitmapFactory.decodeResource(getResources(), R.drawable.car4);
            case 5:
                return BitmapFactory.decodeResource(getResources(), R.drawable.car5);
            case 6:
                return BitmapFactory.decodeResource(getResources(), R.drawable.car6);
            default:
                return BitmapFactory.decodeResource(getResources(), R.drawable.car7);
        }
    }
}
