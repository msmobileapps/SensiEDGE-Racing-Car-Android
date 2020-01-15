package com.ashtray.carracinggame2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.ashtray.carracinggame2d.bluetooth.ScanActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.button).setOnClickListener(view -> next());
        findViewById(R.id.imageView2).setOnClickListener(view -> next());
    }

    private void next() {
        Intent i = new Intent(SplashActivity.this, ScanActivity.class);
        startActivity(i);
        finish();
    }
}
