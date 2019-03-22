package com.jwplayer.opensourcedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/*
 * Credit to: https://medium.com/@harivigneshjayapalan/android-recyclerview-implementing-single-item-click-and-long-press-part-ii-b43ef8cb6ad8
 * */
class MyRecyclerItemTouchListener implements RecyclerView.OnItemTouchListener {

    private MyClickListener clicklistener;
    private GestureDetector gestureDetector;

    MyRecyclerItemTouchListener(Context context, final RecyclerView recyclerView, final MyClickListener clicklistener) {

        this.clicklistener = clicklistener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (e.getAction() == MotionEvent.ACTION_UP && clicklistener != null) {
                    clicklistener.onItemClick(child, recyclerView.getChildAdapterPosition(child));
                    Log.i("HYUNJOO", "onSingleTapUp: x - " + e.getX() + "y - " + e.getY());
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

                Log.i("HYUNJOO", "onLongPress: x - " + e.getX() + "y - " + e.getY());

                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (child != null && clicklistener != null) {
                    clicklistener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
            clicklistener.onItemClick(child, rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {
    }

    interface MyClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int position);
    }
}