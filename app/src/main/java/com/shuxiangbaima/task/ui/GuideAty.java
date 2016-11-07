package com.shuxiangbaima.task.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.shuxiangbaima.task.R;
import com.toocms.dink5.mylibrary.app.DensityUtils;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;


public class GuideAty extends BaseActivity {

    @ViewInject(R.id.vp_guide)
    private ViewPager vp_guide;
    @ViewInject(R.id.ll_point_group)
    private LinearLayout lin_group;
    @ViewInject(R.id.view_red_point)
    private View red_point;
    @ViewInject(R.id.btn_start)
    private Button btn_start;
    @ViewInject(R.id.guide_tv)
    private TextView tv;

    private static final int[] mImageIds = new int[]{R.drawable.guide_01,
            R.drawable.guide_02, R.drawable.guide_03};
    private ArrayList<ImageView> mImageViewList;
    private int mPointWidth;

    @Override
    public int getLayoutId() {
        return R.layout.aty_guide;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentForImageView(GuideAty.this, 0, tv);
        initViews();
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new GuidePageListener());
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.putBoolean(GuideAty.this, "FirstG0", true);
                Intent intent = new Intent();
                intent.setClass(GuideAty.this, MainAty.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }


    private void initViews() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(image);
        }
        for (int i = 0; i < mImageIds.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(this, 10), DensityUtils.dp2px(this, 10));
            if (i > 0) {
                params.leftMargin = DensityUtils.dp2px(this, 10);
            }
            point.setLayoutParams(params);
            lin_group.addView(point);
        }
        lin_group.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        lin_group.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mPointWidth = lin_group.getChildAt(1).getLeft()
                                - lin_group.getChildAt(0).getLeft();
                    }
                });
    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class GuidePageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int len = (int) (mPointWidth * positionOffset) + position
                    * mPointWidth;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) red_point
                    .getLayoutParams();
            params.leftMargin = len;
            red_point.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            if (position == mImageIds.length - 1) {
                btn_start.setVisibility(View.VISIBLE);
            } else {
                btn_start.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    }

}
