package com.toocms.dink5.mylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/10/30.
 */

public class TopScrollView extends ScrollView {

    private ScrollowListen topScrollViewListen;

    private boolean canScroll;
    private GestureDetector mGestureDetector;
    OnTouchListener mGestureListener;

    public TopScrollView(Context context) {
        super(context);

    }

    public TopScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new TopScrollView.YScrollDetector());
        canScroll = true;
    }

    public TopScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TopScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollY = getScrollY();
                int height = getHeight();
                int scrollViewMeasuredHeight = getChildAt(0).getMeasuredHeight();
                if (scrollY == 0) {
                    topScrollViewListen.top();
                } else {
                    topScrollViewListen.center();
                }
                break;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setTopScrollViewListen(ScrollowListen topScrollViewListen) {
        this.topScrollViewListen = topScrollViewListen;
    }

    public interface ScrollowListen {
        void top();
        void center();
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
