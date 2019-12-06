# [UNOFFICIAL] Additional samples: Using RecyclerView Manual / Trending Playlist Example for Android Open Source Demo

This application contains an example implementation of the JW Player SDK for Android.

## Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:hyunjkim/jwplayer-sdk-android-demo.git`
-	Open the `AndroidManifest.xml` file and replace `{YOUR_LICENSE_KEY}` with your license key, 
-   Get your license key from https://www.jwplayer.com/pricing

# For more information on how to use our SDK refer to our developer guide:

* **Developer Guide:** [https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)
* **JWPlayer Java Docs:** https://developer.jwplayer.com/sdk/android/reference/
* **JWPlayer Demo** https://github.com/jwplayer/jwplayer-sdk-android-demo

## Demo
![](https://s3.amazonaws.com/hyunjoo.success.jwplayer.com/android/github/v390-trending-manual.gif)

* **NOTE** Make sure to have a Manual/Trending Playlist Player ID ready, you can find that from your JW Dashboard 

1. **MainActivity** 
    * **New Playlist Item**
    ```
        /*
         * Loading a new Player ID
         * */
        public void getNewPlaylist(View enterbtn) {
    
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(enterbtn.getWindowToken(), 0);
            }
    
            if (enterbtn.getId() == R.id.firstrow_btn) {
                manual.cancel(true);
                manual = new JWManualAsyncTask(this);
                manual.execute(firstrow_playlistid.getText().toString());
                firstrow_playlistid.setText("");
            } else {
                trend.cancel(true);
                trend = new JWTrendingAsyncTask(this);
                trend.execute(secondrow_playlistid.getText().toString());
                secondrow_playlistid.setText("");
            }
        }
    
    ```
2. **RecyclerView**
    * **MyRecyclerAdapter**
    ```
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {

        PlaylistItem item = list.get(position);

        TextView tv = holder.itemView.findViewById(R.id.title_tv);
        ImageView iv = holder.itemView.findViewById(R.id.image_view);

        // Show image
        Glide.with(iv)
                .load(item.getImage())
                .into(iv);

        tv.setText(item.getTitle());

        // When clicked, show the video
        iv.setOnClickListener(v -> mListener.reload(playlistName, position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    void updateData(String playlistName) {
        list = SamplePlaylist.getPlaylistItem(playlistName);
        notifyDataSetChanged();
    }
    ```
3. **Listeners** 
    * **CallBackListener**
    ```public interface CallBackListener {
           void onShowProgressBar(String name);
           void onHideProgressBar(String name);
       }
    ```     
4. **jwasynctask folder**
    * **JWManualAsyncTask**
    ```
        public JWManualAsyncTask(CallBackListener listener) {
            mListener = listener;
        }
    
        @Override
        protected List<PlaylistItem> doInBackground(String... strings) {
            if (!isCancelled()) {
    
                String playlistid = strings[0];
    
                if (playlistid.length() <= 0) {
                    playlistid = DEFAULT_PLAYLIST_ID;
                }
                String manual = JW_HOST_API + playlistid;
    
                byte[] response = new byte[0];
    
                try {
                    response = Util.executePost(manual);
                } catch (IOException e) {
                    e.printStackTrace();
                }
    
                if (response.length <= 0) {
                    return SamplePlaylist.getDefaultPlaylist();
                }
    
                String strResponse = new String(response);
    
                return PlaylistItem.listFromJson(strResponse);
            }
            return null;
        }
    
        @Override
        protected void onPreExecute() {
            mListener.onShowProgressBar("Manual");
        }
    
        @Override
        protected void onPostExecute(List<PlaylistItem> playlistItems) {
            super.onPostExecute(playlistItems);
            SamplePlaylist.setPlaylist("Manual", playlistItems);
            mListener.onHideProgressBar("Manual");
        }
    ```
    * **JWTrendingAsyncTask**
    ```Same as above```
5. **Layout - activity_main**
```
<?xml version="1.0" encoding="utf-8"?>

<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.longtailvideo.jwplayer.JWPlayerView
        android:id="@+id/jwplayer"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintDimensionRatio="H,16:9"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/firstrow_playlistid"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:gravity="center"
        android:hint="Enter Manual/Trend Playlist ID"
        android:imeOptions="actionDone"
        android:maxLength="8"
        android:maxLines="1"
        android:textStyle="bold"
        app:layout_constraintEnd_toStartOf="@id/firstrow_btn"
        app:layout_constraintHeight_percent=".065"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/jwplayer"
        app:layout_constraintWidth_percent=".8" />

    <Button
        android:id="@+id/firstrow_btn"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:onClick="getNewPlaylist"
        android:text="Enter"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHeight_percent=".065"
        app:layout_constraintTop_toBottomOf="@id/jwplayer"
        app:layout_constraintWidth_percent=".2" />

    <FrameLayout
        android:id="@+id/firstrow_rv"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintHeight_percent=".26"
        app:layout_constraintTop_toBottomOf="@id/firstrow_playlistid">

        <ProgressBar
            android:id="@+id/progressbar1"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:visibility="visible" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/manual_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:visibility="visible" />
    </FrameLayout>

    <EditText
        android:id="@+id/secondrow_playlistid"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:gravity="center"
        android:hint="Enter Manual/Trend Playlist ID"
        android:imeOptions="actionDone"
        android:maxLength="8"
        android:maxLines="1"
        android:textStyle="bold"
        app:layout_constraintEnd_toStartOf="@id/secondrow_btn"
        app:layout_constraintHeight_percent=".065"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/firstrow_rv"
        app:layout_constraintWidth_percent=".8" />

    <Button
        android:id="@+id/secondrow_btn"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:onClick="getNewPlaylist"
        android:text="Enter"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHeight_percent=".065"
        app:layout_constraintTop_toBottomOf="@id/firstrow_rv"
        app:layout_constraintWidth_percent=".2" />

    <FrameLayout
        android:id="@+id/secondrow_rv"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintHeight_percent=".26"
        app:layout_constraintTop_toBottomOf="@id/secondrow_playlistid">

        <ProgressBar
            android:id="@+id/progressbar2"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:visibility="visible" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/trending_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:visibility="visible" />
    </FrameLayout>
</androidx.constraintlayout.widget.ConstraintLayout>
```

