package com.example.jwplayersdk.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;

public class MainActivity extends AppCompatActivity implements
        MyRecyclerAdapter.MyFragmentCallbackListener {

    private View fragmentContainer;
    private JWFragment mJWFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.container);

        // Begin showing JWPlayerView
        setupFragment();

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MyRecyclerAdapter adapter = new MyRecyclerAdapter();
        adapter.setOnFragmentCallbackListener(this);
        recyclerView.setAdapter(adapter);

        // Get a reference to the CastContext
        CastContext.getSharedInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }

    @Override
    public void passNewFragmentInfo(int position) {

        mJWFragment = (JWFragment) getSupportFragmentManager().findFragmentById(fragmentContainer.getId());

        if(mJWFragment  != null){
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            mJWFragment.setArguments(bundle);
        }

        // Begin Fragment Transaction
        setupFragment();
    }

    /**
     * Begin replacing the containers with a different JWPlayerFragment
     */
    public void setupFragment() {
        if (mJWFragment == null) {
            mJWFragment = new JWFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), mJWFragment)
                .addToBackStack(null)
                .commit();
    }
}
