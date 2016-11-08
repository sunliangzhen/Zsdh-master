package com.shuxiangbaima.task.ui.main.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Task;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.ImageUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.ire.IRecyclerView;
import com.toocms.dink5.mylibrary.ire.OnLoadMoreListener;
import com.toocms.dink5.mylibrary.ire.OnRefreshListener;
import com.toocms.dink5.mylibrary.ire.widget.ClassicRefreshHeaderView;
import com.toocms.dink5.mylibrary.ire.widget.LoadMoreFooterView;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import it.gmariotti.recyclerview.adapter.SlideInLeftAnimatorAdapter;

/**
 * Created by Administrator on 2016/9/14.
 */
public class MyTaskAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener, LoadingTip.onReloadListener {

    @ViewInject(R.id.mytask_ptr_frame)
    private IRecyclerView frg_refush;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;

    private Task task;
    //    private ImageOptions imageOptions;
    private int next_offset = 0;
    private ArrayList<Map<String, String>> task_list;
    private MyAdapter myAdapter;
    private LoadMoreFooterView loadMoreFooterView;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_mytask;
    }

    @Override
    public void requestData() {
        showProgressContent();
        task.my_task(next_offset, this, this);
    }

    @Override
    protected void initialized() {
        task_list = new ArrayList<>();
        task = new Task();
        myAdapter = new MyAdapter();
//        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMoreFooterView = (LoadMoreFooterView) frg_refush.getLoadMoreFooterView();
        frg_refush.setLayoutManager(new LinearLayoutManager(this));
        ClassicRefreshHeaderView classicRefreshHeaderView = new ClassicRefreshHeaderView(this);
        classicRefreshHeaderView.setBackgroundColor(getResources().getColor(R.color.refresh_head));
        classicRefreshHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(150)));
        frg_refush.setRefreshHeaderView(classicRefreshHeaderView);
        frg_refush.setOnRefreshListener(this);
        frg_refush.setOnLoadMoreListener(this);
        SlideInLeftAnimatorAdapter slideInLeftAnimatorAdapter = new SlideInLeftAnimatorAdapter(myAdapter, frg_refush);
        slideInLeftAnimatorAdapter.getViewAnimator().setInitialDelayMillis(300);
        frg_refush.setAdapter(slideInLeftAnimatorAdapter);
        loadedTip.setOnReloadListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.mytask__imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.mytask__imgv_back:
                finish();
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (var1.getUri().contains("my_task") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            Map<String, String> map = JSONUtils.parseDataToMap(var2);
            if (next_offset == 0) {
                task_list = JSONUtils.parseKeyAndValueToMapList(map.get("task_list"));
            } else {
                task_list.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("task_list")));
                if (Integer.parseInt(map.get("next_offset")) == -1) {
                    frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                } else {
                    frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                }
            }
            next_offset = Integer.parseInt(map.get("next_offset"));
            frg_refush.setRefreshing(false);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        frg_refush.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (task_list.size() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        }
        showNetError();
    }

    @Override
    public void onRefresh() {
        next_offset = 0;
        task.my_task(next_offset, MyTaskAty.this, MyTaskAty.this);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        task.my_task(next_offset, MyTaskAty.this, MyTaskAty.this);
    }

    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        task.my_task(next_offset, this, this);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyTaskAty.this).inflate(R.layout.item_page_lv, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            switch (task_list.get(position).get("task_type")) {
                case "1":
                    viewHolder.tv_num.setText("有效点击：" + task_list.get(position).get("click_num"));
                    viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share);
                    viewHolder.tv_type.setTextColor(0xffff0000);
                    viewHolder.tv_type.setText("分享");
                    break;
                case "2":
                    viewHolder.tv_num.setVisibility(View.GONE);
                    viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share2);
                    viewHolder.tv_type.setTextColor(0xffEBC581);
                    viewHolder.tv_type.setText("试用");
                    break;
                case "3":
                    viewHolder.tv_num.setVisibility(View.GONE);
                    viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share3);
                    viewHolder.tv_type.setTextColor(0xff2693EE);
                    viewHolder.tv_type.setText("游戏");
                    break;
                case "4":
                    viewHolder.tv_num.setVisibility(View.GONE);
                    viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share4);
                    viewHolder.tv_type.setTextColor(0xff2EEE26);
                    viewHolder.tv_type.setText("充值");
                    break;
            }
            viewHolder.tv_title.setText(task_list.get(position).get("task_name"));
            viewHolder.tv_mon.setText("￥" + task_list.get(position).get("profit"));
            viewHolder.tv_title.setText(task_list.get(position).get("task_name"));
            switch (task_list.get(position).get("current_status")) {
                case "1":
                    viewHolder.tv_state.setText("进行中");
                    break;
                case "2":
                    viewHolder.tv_state.setText("进行中");
                    break;
                case "66":
                    viewHolder.tv_state.setText("已抢光");
                    break;
                case "3":
                    viewHolder.tv_state.setText("审核中");
                    break;
                case "4":
                    viewHolder.tv_state.setText("已完成");
                    break;
            }
            ImageUtils.display(MyTaskAty.this, task_list.get(position).get("figure"), viewHolder.imav_cover);
            viewHolder.page_linlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("task_id", task_list.get(position).get("task_id"));
                    bundle.putString("task_type", task_list.get(position).get("task_type"));
                    startActivity(DetailsAty.class, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return task_list.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_pagelv_imgv_cover)
            private ImageView imav_cover;
            @ViewInject(R.id.item_pagelv_tv_title)
            private TextView tv_title;
            @ViewInject(R.id.item_pagelv_tv_mon)
            private TextView tv_mon;
            @ViewInject(R.id.item_pagelv_tv_num)
            private TextView tv_num;
            @ViewInject(R.id.item_pagelv_tv_state)
            private TextView tv_state;
            @ViewInject(R.id.item_pagelv_tv_share)
            private TextView tv_type;
            @ViewInject(R.id.page_linlay)
            private LinearLayout page_linlay;

            public ViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }

    }


}
