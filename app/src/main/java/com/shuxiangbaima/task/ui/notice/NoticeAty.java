package com.shuxiangbaima.task.ui.notice;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Notice;
import com.toocms.dink5.mylibrary.base.BaseActivity;
import com.toocms.dink5.mylibrary.commonutils.Settings;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.DbManager;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
public class NoticeAty extends BaseActivity {

    @ViewInject(R.id.task_v_line)
    private View sortFlag; // 排序标识
    @ViewInject(R.id.tv_00)
    private TextView tv_00;
    @ViewInject(R.id.tv_01)
    private TextView tv_01;
    @ViewInject(R.id.tv_02)
    private TextView tv_02;


    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition = 0; // 排序标识位置
    private TextView[] ttvv;

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;
    private CustomAdapter customAdapter;
    private ArrayList<Fragment> list;
    private ArrayList<Map<String, String>> notice_list;
    private Notice notice;

    @Override
    public int getLayoutId() {
        return R.layout.aty_notice;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        customAdapter = new CustomAdapter(getSupportFragmentManager());
        list = new ArrayList<>();
        notice_list = new ArrayList<>();
        list.add(new NoticeFragment1().newInstance("1"));
        list.add(new NoticeFragment1().newInstance("2"));
        list.add(new NoticeFragment1().newInstance("3"));
        notice = new Notice();
//        db = x.getDb(((Myappcation) getApplicationContext()).getDaoConfig());
        sortFlagWidth = AutoUtils.getPercentWidthSize(150);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 1)) /3);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttvv = new TextView[]{tv_00, tv_01, tv_02};
        sortFlag.setBackgroundColor(getResources().getColor(R.color.top));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(5));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        viewPager.setAdapter(customAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sortFlagPosition = position;
                setTextviewColor(sortFlagPosition);
                startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Event(value = {R.id.notice_imgv_back, R.id.tv_00, R.id.tv_01, R.id.tv_02})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.notice_imgv_back:
                finish();
                break;
            case R.id.tv_00:
                sortFlagPosition = 0;
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_01:
                sortFlagPosition = 1;
                viewPager.setCurrentItem(1);
                break;
            case R.id.tv_02:
                sortFlagPosition = 2;
                viewPager.setCurrentItem(2);
                break;
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
    }

    private void setTextviewColor(int index) {
        for (int i = 0; i < ttvv.length; i++) {
            if (index == i) {
                ttvv[i].setTextColor(getResources().getColor(R.color.top));
            } else {
                ttvv[i].setTextColor(0xff000000);
            }
        }
    }

    private void startTranslate(final View view, float endX) {
        float startx = view.getX();
        ValueAnimator animator = ValueAnimator.ofFloat(startx, endX);
        animator.setTarget(view);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
//    @Override
//    public void onComplete(RequestParams var1, String var2) {
//        super.onComplete(var1, var2);
//        if (var1.getUri().contains("notice_list") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
//            Map<String, String> map = JSONUtils.parseDataToMap(var2);
//            if (!map.get("count").equals("0")) {
//                Notice2 notice2;
//                notice_list = JSONUtils.parseKeyAndValueToMapList(map.get("notice_list"));
//                for (int i = 0; i < notice_list.size(); i++) {
//                    notice2 = new Notice2();
//                    notice2.setNotice_id(notice_list.get(i).get("notice_id"));
//                    notice2.setNotice_type(notice_list.get(i).get("notice_type"));
//                    notice2.setSend_time(notice_list.get(i).get("send_time"));
//                    notice2.setTitle(notice_list.get(i).get("title"));
//                    notice2.setLink(notice_list.get(i).get("link"));
//                    notice2.setIsRead("false");
//                    try {
//                        db.save(notice2);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                }
//                viewPager.setAdapter(customAdapter);
//            } else {
//                viewPager.setAdapter(customAdapter);
//            }
//        }
//    }

//    private void seletorDateBase() {
//        String isRead = "true";
//        List<Notice2> list_notice2 = null;
//        try {
//            list_notice2 = db.selector(Notice2.class).orderBy("id", true).findAll();
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        if (list_notice2 != null) {
//            for (int i = 0; i < list_notice2.size(); i++) {
//                if (list_notice2.get(i).getIsRead().equals("false")) {
//                    isRead = "false";
//                }
//            }
//            if (isRead.equals("true")) {
//                notice.notice_list(this, this);
//            } else {
//                viewPager.setAdapter(customAdapter);
//            }
//        } else {
//            notice.notice_list(this, this);
//        }
//    }

    private class CustomAdapter extends FragmentPagerAdapter {


        public CustomAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }

}
