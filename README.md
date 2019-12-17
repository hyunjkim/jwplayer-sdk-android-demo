# [UNOFFICIAL] JW Player SDK for Android Open Source Demo

This application contains an example implementation of the JW Player SDK for Android.

## VIEW PAGER DEMO with JWPlayerView
For more information on how to use our SDK refer to our developer guide:
[https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)

## Google Developer Guide - ViewPager
[navigation-swipe-view](https://developer.android.com/guide/navigation/navigation-swipe-view)
- Best place to learn how to create it from scratch
- It was easy to understand and use

#### Usage instructions:

- Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
- Open the `AndroidManifest.xml` file and replace `{YOUR_LICENSE_KEY}` with your license key
- The demo application should now build and run. 

![](https://s3.amazonaws.com/hyunjoo.success.jwplayer.com/android/github/v390-viewpager.gif)


## SplashActivity + ![layout_container.xml](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_viewpager/app/src/main/res/layout/layout_container.xml)

```
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
```


## JWTab + ![fragment_viewpager.xml](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_viewpager/app/src/main/res/layout/fragment_viewpager.xml)

```
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
```


## JWPlayerViewSample + ![activity_jwplayer.xml](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_viewpager/app/src/main/res/layout/activity_jwplayerview.xml)

```
public class JWPlayerViewSample extends Fragment implements View.OnClickListener {

    final String TAB_JWPLAYER = "JWPlayerViewSample";

    private JWPlayerView mPlayerView;
    private boolean userClickedPause;
    private PlaylistItem pi;
    private PlayerConfig config;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_jwplayerview, container, false);

        mPlayerView = v.findViewById(R.id.jwplayer);
        v.findViewById(R.id.pausebtn).setOnClickListener(this);
        v.findViewById(R.id.loadbtn).setOnClickListener(this);
        userClickedPause = false;

        // Load a media source
        pi = new PlaylistItem.Builder()
                .file("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .title("BipBop")
                .description("A video player testing video.")
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            playlist.add(pi);
        }

        config = new PlayerConfig.Builder()
                .playlist(playlist)
                .autostart(false)
                .displayDescription(true)
                .build();

        // NOTE: Fired when the player enters the BUFFERING state.
        // https://s3.amazonaws.com/developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/events/listeners/VideoPlayerEvents.OnBufferListener.html
        mPlayerView.addOnBufferListener(new VideoPlayerEvents.OnBufferListener() {
            @Override
            public void onBuffer(BufferEvent bufferEvent) {
                if (userClickedPause) {
                    if (mPlayerView != null) {
                        Log.i(TAB_JWPLAYER, "onbufferlistener - PAUSE! ");
                        mPlayerView.pause();
                    }
                }
            }
        });

        // NOTE: Signifies when the player has been initialized and is ready for playback.
        // https://s3.amazonaws.com/developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/events/listeners/VideoPlayerEvents.OnReadyListener.html
        mPlayerView.addOnReadyListener(new VideoPlayerEvents.OnReadyListener() {
            @Override
            public void onReady(ReadyEvent readyEvent) {
                Log.i(TAB_JWPLAYER, "onReady() ");
                userClickedPause = false;
            }
        });

        // NOTE: Fired when the player enters the PLAYING state.
        // https://s3.amazonaws.com/developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/events/listeners/VideoPlayerEvents.OnPlayListener.html
        mPlayerView.addOnPlayListener(new VideoPlayerEvents.OnPlayListener() {
            @Override
            public void onPlay(PlayEvent playEvent) {
                Log.i(TAB_JWPLAYER, "onPlay() ");
                userClickedPause = false;
            }
        });

        // NOTE: These are just to see the entire log, Click Check All from App > in Android Studio Logcat filter for "callbackscreen"
        CallbackScreen callback = v.findViewById(R.id.callback_screen);
        callback.registerListeners(mPlayerView);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (config != null) {
            mPlayerView.setup(config);
        }
    }

    /*
     * Pause and Load Example
     * */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.pausebtn) {
            if (mPlayerView != null) {
                userClickedPause = true;
                Log.i(TAB_JWPLAYER, "Clicked Pause + current Player state is " + mPlayerView.getState());
                mPlayerView.pause();
            }
        } else {
            if (mPlayerView != null) {
                userClickedPause = false;
                Log.i(TAB_JWPLAYER, "Clicked Load + current Player state is " + mPlayerView.getState());
                mPlayerView.stop();
                mPlayerView.load(pi);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

}
```
