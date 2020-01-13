package com.ashtray.carracinggame2d.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.ashtray.carracinggame2d.CarGame2DApplication;
import com.ashtray.carracinggame2d.log.LogHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DatabaseManager {
    private static final String DEBUG_TAG = "[DatabaseManager]";

    private MySharedPreferenceSupport mySharedPreferenceSupport;

    private static DatabaseManager databaseManager;
    public static DatabaseManager getInstance(){
        if(databaseManager == null){
            databaseManager = new DatabaseManager();
            databaseManager.mySharedPreferenceSupport = new MySharedPreferenceSupport();
            LogHandler.d(DEBUG_TAG, "database initialized successfully");
        }
        return databaseManager;
    }

    private static class MySharedPreferenceSupport{

        @SuppressLint("ApplySharedPref")
        private void set(String key, Object value) throws Exception{
            ByteArrayOutputStream serializedData = new ByteArrayOutputStream();
            ObjectOutputStream serializer = new ObjectOutputStream(serializedData);
            serializer.writeObject(value);

            //Insert serialized object into shared preferences
            SharedPreferences sharedPreferences = CarGame2DApplication.getInstance().getSharedPreferences(tagGenerator(key), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(key, Base64.encodeToString(serializedData.toByteArray(), Base64.DEFAULT));
            edit.commit();
        }

        private Object get(String key) throws IOException, ClassNotFoundException {
            Object ret = null;
            SharedPreferences sharedPreferences = CarGame2DApplication.getInstance().getSharedPreferences(tagGenerator(key), Context.MODE_PRIVATE);
            String serializedData = sharedPreferences.getString(key, null);
            if(serializedData != null){
                ByteArrayInputStream input = new ByteArrayInputStream(Base64.decode(serializedData, Base64.DEFAULT));
                ObjectInputStream inputStream = new ObjectInputStream(input);
                ret = inputStream.readObject();
            }
            return ret;
        }

        private void remove(String key){
            SharedPreferences sharedPreferences = CarGame2DApplication.getInstance().getSharedPreferences(tagGenerator(key), Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(key).apply();
        }

        private String tagGenerator(String key){
            return String.format("tag_%s",key);
        }
    }

    private static final String KEY_FOR_SELECTED_CAR_NUMBER = "key_for_selected_car_number";
    private static final String KEY_FOR_SELECTED_ROAD_LEAN = "key_for_selected_road_lean";
    private static final String KEY_FOR_GAME_SOUND_STATUS = "key_for_game_sound_status";


    public void setSelectedCarNumber(int carNumber){
        try { mySharedPreferenceSupport.set(KEY_FOR_SELECTED_CAR_NUMBER, String.valueOf(carNumber)); }
        catch (Exception e) { LogHandler.d(DEBUG_TAG, "setSelectedCarNumber -> Exception"); }
    }

    public int getSelectedCarNumber(){
        String number = null;
        try { number = (String) mySharedPreferenceSupport.get(KEY_FOR_SELECTED_CAR_NUMBER); }
        catch (IOException e) { e.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return (number == null)? 1 : Integer.parseInt(number); // default selected car is 1
    }

    public void setSelectedRoadLean(int roadLean){
        try { mySharedPreferenceSupport.set(KEY_FOR_SELECTED_ROAD_LEAN, String.valueOf(roadLean)); }
        catch (Exception e) { LogHandler.d(DEBUG_TAG, "setSelectedRoadLean -> Exception"); }
    }

    public int getSelectedRoadLean(){
        String number = null;
        try { number = (String) mySharedPreferenceSupport.get(KEY_FOR_SELECTED_ROAD_LEAN); }
        catch (IOException e) { e.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return (number == null)? 3 : Integer.parseInt(number); // default selected road lean is 3
    }

    public void setGameSoundStatus(boolean soundStatus){
        try { mySharedPreferenceSupport.set(KEY_FOR_GAME_SOUND_STATUS, String.valueOf(soundStatus)); }
        catch (Exception e) { LogHandler.d(DEBUG_TAG, "setGameSound -> Exception"); }
    }

    public boolean getGameSoundStatus(){
        String savedString = null;
        try { savedString = (String) mySharedPreferenceSupport.get(KEY_FOR_GAME_SOUND_STATUS); }
        catch (IOException e) { e.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return (savedString != null) && Boolean.parseBoolean(savedString); // default game sound off
    }
}
