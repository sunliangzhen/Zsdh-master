package com.shuxiangbaima.task.ui.main.fragment;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Ad;
import com.shuxiangbaima.task.interfaces.Notice;
import com.shuxiangbaima.task.interfaces.Task;
import com.shuxiangbaima.task.ui.MainAty;
import com.shuxiangbaima.task.ui.lar.LoginAty;
import com.shuxiangbaima.task.ui.main.adapter.PageListAdapter;
import com.shuxiangbaima.task.ui.main.bean.PageBean;
import com.shuxiangbaima.task.ui.main.contract.PageListContract;
import com.shuxiangbaima.task.ui.main.model.PageListModel;
import com.shuxiangbaima.task.ui.main.presenter.PageListPresenter;
import com.shuxiangbaima.task.ui.mine.SunpaiAty;
import com.shuxiangbaima.task.ui.notice.NoticeAty;
import com.shuxiangbaima.task.ui.page.AdDetailsAty;
import com.shuxiangbaima.task.ui.page.Cashervice;
import com.toocms.dink5.mylibrary.app.AppConstant;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.base.BaseActivity;
import com.toocms.dink5.mylibrary.base.BaseFragment;
import com.toocms.dink5.mylibrary.baserx.RxBus;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.StatusBarUtil2;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.ire.IRecyclerView;
import com.toocms.dink5.mylibrary.net.ApiListener;
import com.toocms.dink5.mylibrary.view.FlipTextView;
import com.toocms.dink5.mylibrary.viewpager.BannerBean;
import com.toocms.dink5.mylibrary.viewpager.DisallowParentTouchViewPager;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.gmariotti.recyclerview.adapter.SlideInLeftAnimatorAdapter;


/**
 * Created by Administrator on 2016/8/8.
 */
public class PageFrg extends BaseFragment<PageListPresenter, PageListModel> implements PageListContract.View, Cashervice.CashListener,
        DisallowParentTouchViewPager.ViewpagerListener,
        ApiListener, LoadingTip.onReloadListener {

    @ViewInject(R.id.page_ptr_frame)
    private IRecyclerView frg_refush;
    @ViewInject(R.id.page_imgv_read)
    private ImageView imgv_red;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
    @ViewInject(R.id.fab)
    public FloatingActionButton fab;
    @ViewInject(R.id.appbar)
    public AppBarLayout appbar;
    @ViewInject(R.id.viewpager)
    private DisallowParentTouchViewPager viewPager;
    @ViewInject(R.id.ll_point_container)
    private LinearLayout ll_point_container;
    @ViewInject(R.id.toolbar)
    private Toolbar top;
    @ViewInject(R.id.imgv)
    private ImageView imgv;
    @ViewInject(R.id.swipeLayout)
    private SwipeRefreshLayout swipeLayout;
    @ViewInject(R.id.page_imgv_notice)
    private ImageView imgv_notice;

    private TextView tv_more;
    private TextView tv_total_profit;
    private TextView tv_time;
    private View in_foot;
    private FlipTextView tv_01;
    private int noticeNum = 0;
    private int noticeTime = 0;
    private Notice notice;
    private Ad ad;
    private Task task;
    private PageListAdapter myAdapter;
    private List<PageBean.TaskListEntity> datas = new ArrayList<>();
    private List<PageBean.TaskListEntity> taskList = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.frg_page;
    }

    @Override
    protected void initView() {
        notice = new Notice();
        ad = new Ad();
        task = new Task();
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    public void onResume() {
        super.onResume();
        viewPager.startPaly();
        tv_01.startPlay();
        if (myAdapter.getDataCount() == 0) {
            appbar.setExpanded(false);
            adImg();
            task.recommend_task(getActivity(), mPresenter);
            if (Config.isLogin()) {
                notice();
            }
        }
        fab.show();

//        String str = "adcdf4541212";
//        char[] ch = str.toCharArray();
//        for (int p = 0; p < str.length(); p++) {
//            String b = str.substring(p, str.length());
//            ch = b.toCharArray();
//            for (int i = 0; i < ch.length; i++) {
//                for (int k = i; k < ch.length - i; k++) {
//                    char c = ch[k];
//                    char[] ss = new char[i];
//                    for (int l = 0; l < i; l++) {
//                        ss[l] = ch[l];
//                    }
//                    System.out.println("[Object Init] mInt=" + String.valueOf(ss) + ch[k]);
//                }
//            }
//        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myAdapter = new PageListAdapter(getActivity(), datas, frg_refush);
        View in_head = LayoutInflater.from(getContext()).inflate(R.layout.in_head, null, false);
        in_foot = LayoutInflater.from(getContext()).inflate(R.layout.in_foot, null, false);
        tv_more = (TextView) in_foot.findViewById(R.id.page_tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainAty activity = (MainAty) getActivity();
                activity.permo(1);
                fab.show();
                RxBus.getInstance().post(AppConstant.MENU_SHOW_HIDE, true);
            }
        });
        tv_01 = (FlipTextView) in_head.findViewById(R.id.pagehead_tv_01);
        tv_total_profit = (TextView) in_head.findViewById(R.id.pagehead_total_profit);
        tv_time = (TextView) in_head.findViewById(R.id.pagehead_tiem);

        String cash_list = PreferencesUtils.getString(getActivity(), "cash_list", "");
        if (!TextUtils.isEmpty(cash_list)) {
            tv_01.setData(JSONUtils.parseKeyAndValueToMapList(cash_list), tv_time, false);
            tv_total_profit.setText(PreferencesUtils.getString(getActivity(), "total_profit", "") + "元");
        }
        cashRecent();
//        HelloWorldImpl helloWorld = new HelloWorldImpl();
//        DynamicProxy dp = new DynamicProxy();
//        //在这里绑定的是HelloWorld,也就是HelloWorld是被代理接口。所以绑定关系时，需要传递一个HelloWorld的实现类的实例化对象。
//        HelloWorld helloWorld1 = (HelloWorld) dp.bindRelation(helloWorld);
//        helloWorld1.print();
//        helloWorld1.say();

        frg_refush.addHeaderView(in_head);
        frg_refush.addFooterView(in_foot);
        frg_refush.setLayoutManager(new LinearLayoutManager(getContext()));
        SlideInLeftAnimatorAdapter slideInLeftAnimatorAdapter = new SlideInLeftAnimatorAdapter(myAdapter, frg_refush);
        slideInLeftAnimatorAdapter.getViewAnimator().setInitialDelayMillis(300);
        frg_refush.setAdapter(slideInLeftAnimatorAdapter);
        frg_refush.setLoadMoreEnabled(false);
        loadedTip.setOnReloadListener(this);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    swipeLayout.setEnabled(true);
                    top.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return viewPager.dispatchTouchEvent(event);
                        }
                    });
                } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                    swipeLayout.setEnabled(false);
                    top.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                } else {
                    swipeLayout.setEnabled(false);
                    top.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return viewPager.dispatchTouchEvent(event);
                        }
                    });
                }

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setTranslucentForImageView(getActivity(), 0, imgv);
            int statusBarHeight = StatusBarUtil2.getStatusBarHeight(getActivity());
            CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) top.getLayoutParams();
            layoutParams.topMargin = statusBarHeight;
            top.setLayoutParams(layoutParams);
        }
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        layoutParams.height = AutoUtils.getPercentHeightSize(400);
        appbar.setLayoutParams(layoutParams);

        CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        layoutParams2.height = AutoUtils.getPercentHeightSize(110);
        layoutParams2.width = AutoUtils.getPercentHeightSize(110);
        fab.setLayoutParams(layoutParams2);

        CollapsingToolbarLayout.LayoutParams layoutParamstop = (CollapsingToolbarLayout.LayoutParams) top.getLayoutParams();
        layoutParamstop.height = AutoUtils.getPercentHeightSize(88);
        top.setLayoutParams(layoutParamstop);
        swipeLayout.setSize(AutoUtils.getPercentHeightSize(100));
        swipeLayout.setColorSchemeResources(R.color.red, R.color.blue, R.color.yellow, R.color.green);
        swipeLayout.setProgressViewEndTarget(true, AutoUtils.getPercentHeightSize(300));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                task.recommend_task(getActivity(), mPresenter);
            }
        });

        frg_refush.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int abs = Math.abs(myAdapter.getScollYDistance());
                if (myAdapter.getScrolledPosition() >= 4) {
                    fab.setImageResource(R.drawable.ic_top);
                    fab.setClickable(true);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                } else if (myAdapter.getScrolledPosition() < 3) {
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                    fab.setImageResource(R.color.transparent);
                    fab.setClickable(false);
                }
            }
        });
    }

    @Event(value = {R.id.page_imgv_notice, R.id.fab})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.page_imgv_notice:
                if (Config.isLogin()) {
                    startActivity(NoticeAty.class, null);
                } else {
                    startActivity(LoginAty.class, null);
                }
                break;
            case R.id.fab:
                scrolltoTop();
                appbar.setExpanded(true);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String result) {
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(result);
        if (var1.getUri().contains("new_notice") && map.get("status").equals("200")) {
            if (JSONUtils.parseDataToMap(result).get("count").equals("0")) {
                imgv_red.setVisibility(View.GONE);
            } else {
                imgv_red.setVisibility(View.VISIBLE);
            }
        }
    }

    private void notice() {
        noticeNum = 0;
        String notice_data = PreferencesUtils.getString(getActivity(), "notice_data", "");
        if (!TextUtils.isEmpty(notice_data)) {
            ArrayList<Map<String, String>> maps = JSONUtils.parseKeyAndValueToMapList(notice_data);
            for (int i = 0; i < maps.size(); i++) {
                int send_time = Integer.parseInt(maps.get(i).get("send_time"));
                if (send_time > noticeTime) {
                    noticeTime = send_time;
                }
                if (PreferencesUtils.getInt(getActivity(), maps.get(i).get("notice_id"), 0) == 0) {
                    noticeNum++;
                }
            }
            if (noticeNum > 0) {
                imgv_red.setVisibility(View.VISIBLE);
            } else {
                notice.new_notice(noticeTime + "", getActivity(), this);
            }
        } else {
            notice.new_notice("0", getActivity(), this);
        }
    }

    private void adImg() {
//        if (System.currentTimeMillis() > PreferencesUtils.getLong(getActivity(), "expire", 0)) {
//            ad.carousel(getActivity(), mPresenter);
//        } else {
//            String adImg = PreferencesUtils.getString(getActivity(), "ad_list");
//            ad_list = JSONUtils.parseKeyAndValueToMapList(adImg);
////            viewPager.setData(ad_list, ll_point_container, 0, 3000, PageFrg.this);
//        }
        ad.carousel(getActivity(), mPresenter);
    }

    public void scrolltoTop() {
        frg_refush.smoothScrollToPosition(0);
    }

    private void cashRecent() {
        Intent intent = new Intent(getActivity(), Cashervice.class);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void reload() {
        if (Config.isLogin()) {
            notice();
        }
        adImg();
        task.recommend_task(getActivity(), mPresenter);
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Cashervice service1 = ((Cashervice.MsgBinder) service).getService();
            service1.regisetCashListener(PageFrg.this);
        }
    };

    @Override
    public void cash(String data, String total_profit, boolean isFromeStart) {
        ArrayList<Map<String, String>> maps = JSONUtils.parseKeyAndValueToMapList(data);
        tv_01.setData(maps, tv_time, true);
        tv_total_profit.setText(total_profit + "元");
    }

    @Override
    public void returnTaskListData(PageBean pageBean) {
        frg_refush.setVisibility(View.VISIBLE);
        appbar.setExpanded(true);
        swipeLayout.setRefreshing(false);
        if (pageBean != null) {
            taskList = pageBean.getTaskList();
            myAdapter.replaceAll(taskList);
        }
        frg_refush.setRefreshing(false);
    }

    @Override
    public void returnBannerListData(BannerBean taskBeanList) {
        if (taskBeanList != null) {
            BannerBean.DataEntity data = taskBeanList.getData();
            if (data != null) {
                List<BannerBean.DataEntity.AdListEntity> adList = data.getAdList();
                int expire = data.getExpire();
                viewPager.setData(adList, ll_point_container, 0, 3000, PageFrg.this);
                PreferencesUtils.putLong(getActivity(), "expire", expire * 1000L);
            }
        }
    }

    @Override
    public int getDataCount() {
        return taskList.size();
    }

    @Override
    public void showLoading(String title) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (taskList.size() < 1) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            appbar.setExpanded(false);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            fab.setImageResource(R.color.transparent);
            fab.setClickable(false);
        }
        showNetError();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopPlay();
        tv_01.stopPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewPager.stopPlay();
        tv_01.stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.stopPlay();
        tv_01.stopPlay();
        getActivity().unbindService(conn);
    }

    @Override
    public void viewpagetitemclick(String link) {
        Bundle bundle = new Bundle();
        bundle.putString("link", link);
        startActivity(AdDetailsAty.class, bundle);
    }
}
