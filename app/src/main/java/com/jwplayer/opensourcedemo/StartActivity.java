package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.activityBtn).setOnClickListener(this);
        findViewById(R.id.fragmentBtn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch(v.getId()){
            case R.id.activityBtn:
                intent = new Intent(this,JWPlayerViewExample.class);
                break;
            case R.id.fragmentBtn:
                intent = new Intent(this,JWPlayerFragmentExample.class);
                break;
        }
        startActivity(intent);

    }
}
