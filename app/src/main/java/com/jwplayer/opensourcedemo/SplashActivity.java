package com.jwplayer.opensourcedemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_container);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new JWTab())
                .addToBackStack(null)
                .commit();
    }
}
