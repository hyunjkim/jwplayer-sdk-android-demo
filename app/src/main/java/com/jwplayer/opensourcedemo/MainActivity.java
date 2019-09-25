package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.FrameLayout;

import com.google.android.gms.cast.framework.CastButtonFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(fragmentContainer.getId(), new JWPlayerFragmentView())
                .disallowAddToBackStack()
                .commit();

        fm.executePendingTransactions();

    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_fragment, menu);

        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(),
                menu,
                R.id.media_route_menu_item);

        return true;
    }

}
