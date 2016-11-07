package com.toocms.dink5.mylibrary.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/11/1.
 */

public class MySwiperefreshlayout extends SwipeRefreshLayout {

    private boolean canScroll;
    private GestureDetector mGestureDetector;
    private TopScrollView scrollview;
    private boolean isResh = false;

    public MySwiperefreshlayout(Context context) {
        super(context);
    }

    public MySwiperefreshlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new MySwiperefreshlayout.YScrollDetector());
        canScroll = true;
    }

    public void setScrollview(TopScrollView scrollview) {
        this.scrollview = scrollview;
//        init();
    }

    private void init() {
        scrollview.setTopScrollViewListen(new TopScrollView.ScrollowListen() {
            @Override
            public void top() {
                setEnabled(true);
                isResh = true;
            }

            @Override
            public void center() {
                setEnabled(false);
                isResh = false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP)
            canScroll = true;
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (canScroll)
                if (Math.abs(distanceY) >= Math.abs(distanceX))
                    canScroll = true;
                else
                    canScroll = false;
            return canScroll;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }
}
