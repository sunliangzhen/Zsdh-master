package com.shuxiangbaima.task.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.config.UpdateManager;
import com.shuxiangbaima.task.ui.Mon.MonFrg;
import com.shuxiangbaima.task.ui.lar.LoginAty;
import com.shuxiangbaima.task.ui.main.bean.TabEntity;
import com.shuxiangbaima.task.ui.main.fragment.PageFrg;
import com.shuxiangbaima.task.ui.main.fragment.TaskFrg;
import com.shuxiangbaima.task.ui.mine.MineFrg;
import com.toocms.dink5.mylibrary.app.AppConstant;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.base.BaseActivity;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import rx.functions.Action1;


/**
 * Created by Administrator on 2016/8/8.
 */
public class MainAty extends BaseActivity {

    @ViewInject(R.id.bottom_navigation_bar)
    public CommonTabLayout tabLayout;

    private PageFrg pageFrg;
    private TaskFrg taskFrg;
    private MonFrg monFrg2;
    private MineFrg mineFrg;

    private String[] mTitles = {"首页", "任务", "收益", "个人"};
    private int[] mIconUnselectIds = {
            R.drawable.ic_home_normal, R.drawable.ic_girl_normal, R.drawable.ic_video_normal, R.drawable.ic_care_normal};
    private int[] mIconSelectIds = {
            R.drawable.ic_home_selected, R.drawable.ic_girl_selected, R.drawable.ic_video_selected, R.drawable.ic_care_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private static int tabLayoutHeight;

    @Override
    public int getLayoutId() {
        return R.layout.aty_main;

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
        UpdateManager.checkUpdate(AppConfig.UPDATE_APPURL, false);
        setBackTwo(true);
        initTab();
        initFragment(savedInstanceState);
        tabLayout.setTextsize(AutoUtils.getPercentWidthSizeBigger(12));
        tabLayout.setIconHeight(AutoUtils.getPercentHeightSizeBigger(20));
        tabLayout.setIconWidth(AutoUtils.getPercentHeightSizeBigger(20));
        tabLayout.measure(0, 0);
        tabLayoutHeight = tabLayout.getMeasuredHeight();
        mRxManager.on(AppConstant.MENU_SHOW_HIDE, new Action1<Boolean>() {
            @Override
            public void call(Boolean hideOrShow) {
                startAnimation(hideOrShow);
            }
        });
        mRxManager.on(AppConstant.LOG_OUT, new Action1<Boolean>() {
            @Override
            public void call(Boolean hideOrShow) {
                logOut();
            }
        });
    }

    private void logOut() {
        preposition = 0;
        SwitchTo(0);
        tabLayout.setCurrentTab(preposition);
    }

    /**
     * 菜单显示隐藏动画
     *
     * @param showOrHide
     */
    private void startAnimation(boolean showOrHide) {
        final ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
        ValueAnimator valueAnimator;
        ObjectAnimator alpha;
        if (!showOrHide) {
            valueAnimator = ValueAnimator.ofInt(tabLayoutHeight, 0);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
        } else {
            valueAnimator = ValueAnimator.ofInt(0, tabLayoutHeight);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                tabLayout.setLayoutParams(layoutParams);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(valueAnimator, alpha);
        animatorSet.start();
    }

    /**
     * 初始化tab
     */
    private int preposition = 0;

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (Config.isLogin()) {
                    SwitchTo(position);
                } else {
                    if (position == 2) {
                        Intent intent = new Intent(MainAty.this, LoginAty.class);
                        intent.putExtra("type", AppConfig.FRG_MON);
                        startActivityForResult(intent, AppConfig.FRG_MON);
                    } else if (position == 3) {
                        Intent intent = new Intent(MainAty.this, LoginAty.class);
                        intent.putExtra("type", AppConfig.FRG_MINE);
                        startActivityForResult(intent, AppConfig.FRG_MINE);
                    } else {
                        preposition = position;
                        SwitchTo(position);
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            pageFrg = (PageFrg) getSupportFragmentManager().findFragmentByTag("pageFrg");
            taskFrg = (TaskFrg) getSupportFragmentManager().findFragmentByTag("taskFrg");
            monFrg2 = (MonFrg) getSupportFragmentManager().findFragmentByTag("monFrg2");
            mineFrg = (MineFrg) getSupportFragmentManager().findFragmentByTag("mineFrg");
            currentTabPosition = savedInstanceState.getInt(AppConfig.HOME_CURRENT_TAB_POSITION);
        } else {
            pageFrg = new PageFrg();
            taskFrg = new TaskFrg();
            monFrg2 = new MonFrg();
            mineFrg = new MineFrg();
            transaction.add(R.id.fl_body, pageFrg, "pageFrg");
            transaction.add(R.id.fl_body, taskFrg, "taskFrg");
            transaction.add(R.id.fl_body, monFrg2, "monFrg2");
            transaction.add(R.id.fl_body, mineFrg, "mineFrg");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    public void permo(int position) {
        SwitchTo(position);
        tabLayout.setCurrentTab(position);
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(taskFrg);
                transaction.hide(monFrg2);
                transaction.hide(mineFrg);
                transaction.show(pageFrg);
//                pageFrg.
                transaction.commitAllowingStateLoss();
                break;
            //任务
            case 1:
                transaction.hide(pageFrg);
                transaction.hide(monFrg2);
                transaction.hide(mineFrg);
                transaction.show(taskFrg);
                transaction.commitAllowingStateLoss();
                break;
            //收益
            case 2:
                transaction.hide(pageFrg);
                transaction.hide(taskFrg);
                transaction.hide(mineFrg);
                transaction.show(monFrg2);
                transaction.commitAllowingStateLoss();
                break;
            //个人
            case 3:
                transaction.hide(pageFrg);
                transaction.hide(taskFrg);
                transaction.hide(monFrg2);
                transaction.show(mineFrg);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case AppConfig.FRG_MON:
                    preposition = 2;
                    SwitchTo(2);
                    break;
                case AppConfig.FRG_MINE:
                    preposition = 3;
                    SwitchTo(3);
                    break;
            }
        } else if (resultCode == 1) {
            tabLayout.setCurrentTab(preposition);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tabLayout != null) {
            outState.putInt(AppConfig.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }
}
