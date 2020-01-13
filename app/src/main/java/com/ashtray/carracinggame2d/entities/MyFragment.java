package com.ashtray.carracinggame2d.entities;

import androidx.fragment.app.Fragment;

public abstract class MyFragment extends Fragment {

    protected MyFragmentCallBacks callBacks;

    public MyFragment(MyFragmentCallBacks callBacks){
        this.callBacks = callBacks;
    }

    //currently showing fragment must have to keep an way to control back pressed
    public abstract boolean handleBackButtonPressed();

    public interface MyFragmentCallBacks {
        //fragments can replace them with another fragment by the help of the parent activity with this method
        void showFragment(MyFragmentNames fragmentName);
    }

    public enum MyFragmentNames {
        GAME_HOME_FRAGMENT,
        GAME_STARTED_FRAGMENT,
        GAME_SETTINGS_FRAGMENT
    }
} 