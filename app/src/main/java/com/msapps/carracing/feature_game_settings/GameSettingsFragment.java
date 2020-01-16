package com.msapps.carracing.feature_game_settings;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import com.msapps.carracing.CarGame2DApplication;
import com.msapps.carracing.R;
import com.msapps.carracing.database.DatabaseManager;
import com.msapps.carracing.entities.MyFragment;
import com.msapps.carracing.log.LogHandler;

public class GameSettingsFragment extends MyFragment {
    private static final String DEBUG = "GameSettingsFragment";

    private Switch aSwitchForGameSound;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    private Button buttonForDifficultyLevelEasy;
    private Button buttonForDifficultyLevelMedium;
    private Button buttonForDifficultyLevelHard;

    public GameSettingsFragment(MyFragmentCallBacks callBacks) {
        super(callBacks);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_settings, container, false);
        handleCarSelection(v);
        handleGameSoundOnOff(v);
        handleDifficultyLevelSelection(v);
        return v;
    }

    private void handleCarSelection(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter();
        RecyclerView recyclerView = v.findViewById(R.id.RecyclerViewForShowingCarImages);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void handleGameSoundOnOff(View v) {
        aSwitchForGameSound = v.findViewById(R.id.SwitchForGameSoundOnOFF);
        aSwitchForGameSound.setChecked(DatabaseManager.getInstance().getGameSoundStatus());
        aSwitchForGameSound.setOnClickListener(vSwitch -> {
            LogHandler.d(DEBUG, "setting game sound " + aSwitchForGameSound.isChecked());
            DatabaseManager.getInstance().setGameSoundStatus(aSwitchForGameSound.isChecked());
        });
    }

    private void handleDifficultyLevelSelection(View v) {
        buttonForDifficultyLevelEasy = v.findViewById(R.id.ButtonForDifficultyLevelEasy);
        buttonForDifficultyLevelMedium = v.findViewById(R.id.ButtonForDifficultyLevelMedium);
        buttonForDifficultyLevelHard = v.findViewById(R.id.ButtonForDifficultyLevelHard);

        switch (DatabaseManager.getInstance().getSelectedRoadLean()){
            case 3:
                buttonForDifficultyLevelEasy.setBackgroundColor(Color.GREEN);
                buttonForDifficultyLevelMedium.setBackgroundColor(Color.WHITE);
                buttonForDifficultyLevelHard.setBackgroundColor(Color.WHITE);
                break;
            case 4:
                buttonForDifficultyLevelEasy.setBackgroundColor(Color.WHITE);
                buttonForDifficultyLevelMedium.setBackgroundColor(Color.GREEN);
                buttonForDifficultyLevelHard.setBackgroundColor(Color.WHITE);
                break;
            case 5:
                buttonForDifficultyLevelEasy.setBackgroundColor(Color.WHITE);
                buttonForDifficultyLevelMedium.setBackgroundColor(Color.WHITE);
                buttonForDifficultyLevelHard.setBackgroundColor(Color.GREEN);
                break;
            default:
                buttonForDifficultyLevelEasy.setBackgroundColor(Color.GREEN);
                buttonForDifficultyLevelMedium.setBackgroundColor(Color.WHITE);
                buttonForDifficultyLevelHard.setBackgroundColor(Color.WHITE);
        }

        buttonForDifficultyLevelEasy.setOnClickListener(v1 -> {
            DatabaseManager.getInstance().setSelectedRoadLean(3);
            buttonForDifficultyLevelEasy.setBackgroundColor(Color.GREEN);
            buttonForDifficultyLevelMedium.setBackgroundColor(Color.WHITE);
            buttonForDifficultyLevelHard.setBackgroundColor(Color.WHITE);
        });
        buttonForDifficultyLevelMedium.setOnClickListener(v1 -> {
            DatabaseManager.getInstance().setSelectedRoadLean(4);
            buttonForDifficultyLevelEasy.setBackgroundColor(Color.WHITE);
            buttonForDifficultyLevelMedium.setBackgroundColor(Color.GREEN);
            buttonForDifficultyLevelHard.setBackgroundColor(Color.WHITE);
        });
        buttonForDifficultyLevelHard.setOnClickListener(v1 -> {
            DatabaseManager.getInstance().setSelectedRoadLean(5);
            buttonForDifficultyLevelEasy.setBackgroundColor(Color.WHITE);
            buttonForDifficultyLevelMedium.setBackgroundColor(Color.WHITE);
            buttonForDifficultyLevelHard.setBackgroundColor(Color.GREEN);
        });
    }

    @Override
    public boolean handleBackButtonPressed() {
        callBacks.showFragment(MyFragmentNames.GAME_HOME_FRAGMENT);
        return true;
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CarImageViewHolder> {

        private static final int TOTAL_CAR_IMAGE_HAVE = 12;

        @Override
        public int getItemCount() {
            return TOTAL_CAR_IMAGE_HAVE;
        }

        @NonNull
        @Override
        public CarImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_single_car, viewGroup, false);
            return new CarImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final CarImageViewHolder carImageViewHolder, int position) {
            carImageViewHolder.showCar(position + 1);
        }

        class CarImageViewHolder extends RecyclerView.ViewHolder{

            private CardView parentOfImageView;
            private ImageView carImageView;
            private int carNumber;

            private CarImageViewHolder(View itemView) {
                super(itemView);
                parentOfImageView = itemView.findViewById(R.id.CardViewForShowingCarImage);
                carImageView = itemView.findViewById(R.id.ImageViewForShowingCarOnly);

                parentOfImageView.setBackgroundColor(Color.WHITE);
                carImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                carImageView.setOnClickListener(v -> {
                    DatabaseManager.getInstance().setSelectedCarNumber(carNumber);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                });
            }

            private void showCar(int carNumber){
                this.carNumber = carNumber;
                carImageView.setImageBitmap(CarGame2DApplication.getInstance().getCarAccordingToNumber(carNumber));
                if(carNumber == DatabaseManager.getInstance().getSelectedCarNumber()){
                    parentOfImageView.setBackgroundColor(Color.GREEN);
                }else {
                    parentOfImageView.setBackground(null);
                }
            }
        }

    }
}
