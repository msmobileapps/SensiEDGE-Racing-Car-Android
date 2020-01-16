package com.msapps.carracing.bluetooth;

import android.app.Activity;
import android.widget.TextView;

import com.st.BlueSTSDK.Feature;

public class GenericFragmentUpdate implements Feature.FeatureListener {

    /**
     * text view that will contain the data/name
     */
    final private TextView mTextView;
    final private Activity mActivity;

    /**
     * @param text text view that will show the name/values
     */
    public GenericFragmentUpdate(Activity activity, TextView text) {
        mTextView = text;
        mActivity = activity;
    }//GenericFragmentUpdate

    /**
     * set the text view text with the feature toString value
     *
     * @param f      feature that has received an update
     * @param sample new data received from the feature
     */
    @Override
    public void onUpdate(Feature f, Feature.Sample sample) {
        final String featureDump = f.toString();
        mActivity.runOnUiThread(() -> mTextView.setText(featureDump));
    }

}//GenericFragmentUpdate