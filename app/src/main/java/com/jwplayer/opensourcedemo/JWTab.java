package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class JWTab extends Fragment {

    private DemoCollectionPagerAdapter demoCollectionPagerAdapter;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        demoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(demoCollectionPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        DemoCollectionPagerAdapter(FragmentManager fm) {
            this(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        DemoCollectionPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "JWPlayer " + (position + 1);
        }

        /**
         * Copied from Google Android Developers Guide
         * {@link - https://developer.android.com/guide/navigation/navigation-swipe-view}
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new JWPlayerViewSample();
            Bundle args = new Bundle();
            args.putInt("JWPlayer", position + 1);
            fragment.setArguments(args);

            return fragment;
        }
    }

}
