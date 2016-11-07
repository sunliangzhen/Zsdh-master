package com.toocms.dink5.mylibrary.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.toocms.dink5.mylibrary.R;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.image.ImageOptions;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/24.
 */
public class DisallowParentTouchViewPager2 extends ViewPager {
    private ArrayList<String> list;
    private Context context;
    private ImageOptions imageOptions;
    private int previousSelectedPosition = 0;
    private LinearLayout ll_point_container;
    private int margin = 30;
    private int time = 3000;
    private Handler mHandler;
    private MyAdapter2 myAdapter2;
    private ViewpagerListener mviewpagerListener;
    private float p;

    public DisallowParentTouchViewPager2(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DisallowParentTouchViewPager2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        myAdapter2 = new MyAdapter2();
        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mviewpagerListener.viewpagetitemclick(getCurrentItem() % list.size());
            }
        });
    }

    public void setData(ArrayList<String> list, LinearLayout ll_point_container, int margin, int time, ViewpagerListener mviewpagerListener) {
        this.mviewpagerListener = mviewpagerListener;
        this.list = list;
        this.ll_point_container = ll_point_container;
        this.margin = margin;
        this.time = time;
        if (list.size() > 0) {
            initView();
            initAdapter();
        }
    }


    private void initView() {
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < list.size(); i++) {
            pointView = new View(context);
            pointView.setBackgroundResource(R.drawable.selector_page_point);
            layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(15), AutoUtils.getPercentWidthSize(15));
            if (i != 0)
                layoutParams.leftMargin = AutoUtils.getPercentWidthSize(20);
            pointView.setEnabled(false);
            ll_point_container.addView(pointView, layoutParams);
        }
    }

    private void initAdapter() {
        if (list.size() > 0) {
            ll_point_container.getChildAt(0).setEnabled(true);
        }
        previousSelectedPosition = 0;
        setAdapter(myAdapter2);
        setOffscreenPageLimit(3);
        setPageMargin(AutoUtils.getPercentWidthSize(margin));
        addOnPageChangeListener(new PageListener());
        int i = 3000000 % list.size();
        if (i == 0) {
            setCurrentItem(3000000);
        } else {
            setCurrentItem(3000000 - i);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                stopPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
//                mHandler.sendEmptyMessageDelayed(0, time);
                break;
            case MotionEvent.ACTION_UP:
                if (p == 0) {
                    mviewpagerListener.viewpagetitemclick(getCurrentItem() % list.size());
                }
//                mHandler.sendEmptyMessageDelayed(0, time);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void startPaly() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                setCurrentItem(getCurrentItem() + 1);
                mHandler.sendEmptyMessageDelayed(0, time);

            }
        };
        mHandler.sendEmptyMessageDelayed(0, time);
    }


    public void stopPlay() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            p = positionOffset;

        }

        @Override
        public void onPageSelected(int position) {
            if (list.size() > 0) {
                int newPosition = position % list.size();
                ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_point_container.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    }

    private class MyAdapter2 extends PagerAdapter {


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = null;
            if (list.size() > 0) {
                int newPosition = position % list.size();
                imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                x.image().bind(imageView, list.get(newPosition));
                Glide.with(context)
                        .load(list.get(newPosition))
                        .into(imageView);
                container.addView(imageView);
            }
//            imageView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    stopPlay();
//                    mviewpagerListener.viewpagetitemclick(position % list.size());
//                }
//            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public interface ViewpagerListener {
        void viewpagetitemclick(int position);
    }

}
