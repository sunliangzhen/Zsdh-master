package com.shuxiangbaima.task.ui.main.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.Task;
import com.shuxiangbaima.task.ui.lar.LoginAty;
import com.shuxiangbaima.task.ui.main.activity.MyTaskAty;
import com.shuxiangbaima.task.ui.main.adapter.TaskListAdapter;
import com.shuxiangbaima.task.ui.main.bean.TaskBean;
import com.shuxiangbaima.task.ui.main.contract.TaskListContract;
import com.shuxiangbaima.task.ui.main.model.TaskListModel;
import com.shuxiangbaima.task.ui.main.presenter.TaskListPresenter;
import com.toocms.dink5.mylibrary.app.AppConstant;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.commonutils.Settings;
import com.toocms.dink5.mylibrary.base.BaseFragment;
import com.toocms.dink5.mylibrary.baserx.RxBus;
import com.toocms.dink5.mylibrary.commonutils.StatusBarUtil2;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.ire.IRecyclerView;
import com.toocms.dink5.mylibrary.ire.OnLoadMoreListener;
import com.toocms.dink5.mylibrary.ire.OnRefreshListener;
import com.toocms.dink5.mylibrary.ire.widget.ClassicRefreshHeaderView;
import com.toocms.dink5.mylibrary.ire.widget.LoadMoreFooterView;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.adapter.SlideInLeftAnimatorAdapter;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/8/8.
 */
public class TaskFrg extends BaseFragment<TaskListPresenter, TaskListModel> implements TaskListContract.View, OnRefreshListener, OnLoadMoreListener, RadioGroup.OnCheckedChangeListener, LoadingTip.onReloadListener {


    @ViewInject(R.id.task_ptr_frame)
    private IRecyclerView frg_refush;

    @ViewInject(R.id.task_linlay_all)
    private RadioGroup linlay_all;
    @ViewInject(R.id.task_linlay_order)
    private RadioGroup linlay_order;

    @ViewInject(R.id.task_v_line)
    private View sortFlag; // 排序标识

    @ViewInject(R.id.task_tv_order)
    private TextView tv_order;
    @ViewInject(R.id.imgv_order)
    private ImageView imgv_order;
    @ViewInject(R.id.task_tv_all)
    private TextView tv_all;
    @ViewInject(R.id.imgv_all)
    private ImageView imgv_all;

    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
    @ViewInject(R.id.fab)
    public FloatingActionButton fab;
    @ViewInject(R.id.task_toolbar)
    private Toolbar toolbar;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition = 0; // 排序标识位置
    private TextView[] ttvv;
    private ImageView[] imgv;

    private TaskListAdapter myAdapter;
    private int next_offset = 0;
    private Task task;
    private String task_type;
    private String task_type_content;
    private String order_by;
    private String order_by_content;
    private List<TaskBean.TaskListEntity> datas = new ArrayList<>();
    private LoadMoreFooterView loadMoreFooterView;

    @Override
    protected void initView() {
        task_type_content = "全部任务";
        order_by_content = "默认排序";
        task_type = "0";
        order_by = "0";
        task = new Task();
        sortFlagWidth = AutoUtils.getPercentWidthSize(150);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 1)) / 2);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myAdapter.getDataCount() == 0) {
            next_offset = 0;
            task.task_list(task_type, order_by, next_offset, getActivity(), mPresenter);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.frg_task;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myAdapter = new TaskListAdapter(getActivity(), datas, frg_refush);
        ttvv = new TextView[]{tv_all, tv_order};
        imgv = new ImageView[]{imgv_all, imgv_order};
        sortFlag.setBackgroundColor(getResources().getColor(R.color.top));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(2));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));

        loadMoreFooterView = (LoadMoreFooterView) frg_refush.getLoadMoreFooterView();
        frg_refush.setLayoutManager(new LinearLayoutManager(getContext()));
        ClassicRefreshHeaderView classicRefreshHeaderView = new ClassicRefreshHeaderView(getActivity());
        classicRefreshHeaderView.setBackgroundColor(getResources().getColor(R.color.refresh_head));
        classicRefreshHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(150)));
        frg_refush.setRefreshHeaderView(classicRefreshHeaderView);
        frg_refush.setOnRefreshListener(this);
        frg_refush.setOnLoadMoreListener(this);

        linlay_all.setOnCheckedChangeListener(this);
        linlay_order.setOnCheckedChangeListener(this);

        SlideInLeftAnimatorAdapter slideInLeftAnimatorAdapter = new SlideInLeftAnimatorAdapter(myAdapter, frg_refush);
        slideInLeftAnimatorAdapter.getViewAnimator().setInitialDelayMillis(300);
        frg_refush.setAdapter(slideInLeftAnimatorAdapter);
        loadedTip.setOnReloadListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeight = StatusBarUtil2.getStatusBarHeight(getActivity());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.topMargin = statusBarHeight;
            toolbar.setLayoutParams(layoutParams);
        }

        frg_refush.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (myAdapter.getScrolledPosition() >= 4) {
                    fab.setImageResource(R.drawable.ic_top);
                    fab.setClickable(true);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                } else if (myAdapter.getScrolledPosition() < 3) {
                    setFabTransparent();
                }
            }
        });
    }

    public void scrolltoTop() {
        frg_refush.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        next_offset = 0;
        frg_refush.setRefreshing(true);
        frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        task.task_list(task_type, order_by, next_offset, getActivity(), mPresenter);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        if (loadMoreFooterView.canLoadMore() && myAdapter.getItemCount() > 0) {
            frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
            task.task_list(task_type, order_by, next_offset, getActivity(), mPresenter);
        }
    }

    @Event(value = {R.id.linlay_all, R.id.linlay_order, R.id.task_linlay_all, R.id.task_linlay_order, R.id.task_mytask, R.id.fab})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.linlay_all:
                sortFlag.setVisibility(View.VISIBLE);
                sortFlagPosition = 0;
                linlay_all.setVisibility(View.VISIBLE);
                linlay_order.setVisibility(View.GONE);
                break;
            case R.id.linlay_order:
                sortFlag.setVisibility(View.VISIBLE);
                sortFlagPosition = 1;
                linlay_all.setVisibility(View.GONE);
                linlay_order.setVisibility(View.VISIBLE);
                break;
            case R.id.task_linlay_all:
                linlay_all.setVisibility(View.GONE);
                linlay_order.setVisibility(View.GONE);
                break;
            case R.id.task_linlay_order:
                linlay_all.setVisibility(View.GONE);
                linlay_order.setVisibility(View.GONE);
                break;
            case R.id.task_mytask:
                if (Config.isLogin()) {
                    startActivity(MyTaskAty.class, null);
                } else {
                    Intent intent = new Intent(getActivity(), LoginAty.class);
                    intent.putExtra("type", AppConfig.FRG_TASK);
                    startActivityForResult(intent, AppConfig.FRG_TASK);
                }
                break;
            case R.id.fab:
                scrolltoTop();
                break;
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));

    }

    private void setTextviewColor(int index) {
        for (int i = 0; i < ttvv.length; i++) {
            if (index == i) {
                ttvv[i].setTextColor(getResources().getColor(R.color.top));
                imgv[i].setImageResource(R.drawable.expand2);
            } else {
                ttvv[i].setTextColor(0xff000000);
                imgv[i].setImageResource(R.drawable.expand);
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup.getId() == R.id.task_linlay_all) {
            switch (i) {
                case R.id.task_rbtn_01:
                    task_type_content = "全部任务";
                    task_type = "0";
                    break;
                case R.id.task_rbtn_02:
                    task_type_content = "分享文章任务";
                    task_type = "1";
                    break;
                case R.id.task_rbtn_03:
                    task_type_content = "注册试用任务";
                    task_type = "2";
                    break;
                case R.id.task_rbtn_04:
                    task_type_content = "游戏测试任务";
                    task_type = "3";
                    break;
                case R.id.task_rbtn_05:
                    task_type_content = "游戏充值任务";
                    task_type = "4";
                    break;
            }
        } else {
            switch (i) {
                case R.id.task_rbgn_m01:
                    order_by_content = "默认排序";
                    order_by = "0";
                    break;
                case R.id.task_rbgn_m02:
                    order_by_content = "最新上线";
                    order_by = "1";
                    break;
                case R.id.task_rbgn_m03:
                    order_by_content = "奖励最高";
                    order_by = "2";
                    break;
            }
        }
        if (linlay_all.getVisibility() == View.VISIBLE || linlay_order.getVisibility() == View.VISIBLE) {
            next_offset = 0;
            task.task_list(task_type, order_by, next_offset, getActivity(), mPresenter);
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnTaskListData(TaskBean taskBean) {
        if (next_offset == 0) {
            if (taskBean != null) {
                datas = taskBean.getTaskList();
                myAdapter.replaceAll(datas);
                next_offset = taskBean.getNext_offset();
                if (datas.size() < 1) {
                    loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
                }
            }
            frg_refush.setRefreshing(false);
            frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
            linlay_all.setVisibility(View.GONE);
            linlay_order.setVisibility(View.GONE);
            tv_all.setText(task_type_content);
            tv_order.setText(order_by_content);
            setFabTransparent();
            scrolltoTop();
            fab.show();
            RxBus.getInstance().post(AppConstant.MENU_SHOW_HIDE, true);
        } else {
            if (taskBean != null) {
                List<TaskBean.TaskListEntity> taskList = taskBean.getTaskList();
                if (taskList.size() > 0) {
                    frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    myAdapter.addAll(taskList);
                    next_offset = taskBean.getNext_offset();
                } else {
                    frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
            linlay_all.setVisibility(View.GONE);
            linlay_order.setVisibility(View.GONE);
            tv_all.setText(task_type_content);
            tv_order.setText(order_by_content);
        }
    }

    @Override
    public void showLoading(String title) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public int getDataCount() {
        return datas.size();
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        showNetError();
        frg_refush.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (getDataCount() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            setFabTransparent();
        }
        frg_refush.setRefreshing(false);
        frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        linlay_all.setVisibility(View.GONE);
        linlay_order.setVisibility(View.GONE);
    }

    public void setFabTransparent() {
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        fab.setImageResource(R.color.transparent);
        fab.setClickable(false);
    }

    @Override
    public void reload() {
        next_offset = 0;
        task.task_list(task_type, order_by, next_offset, getActivity(), mPresenter);
    }
}
