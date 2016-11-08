package com.shuxiangbaima.task.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Profit;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.ire.IRecyclerView;
import com.toocms.dink5.mylibrary.ire.OnLoadMoreListener;
import com.toocms.dink5.mylibrary.ire.OnRefreshListener;
import com.toocms.dink5.mylibrary.ire.widget.ClassicRefreshHeaderView;
import com.toocms.dink5.mylibrary.ire.widget.LoadMoreFooterView;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import it.gmariotti.recyclerview.adapter.SlideInLeftAnimatorAdapter;

/**
 * Created by Administrator on 2016/8/10.
 */
public class MonAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener, LoadingTip.onReloadListener {

    @ViewInject(R.id.mytask_ptr_frame)
    private IRecyclerView frg_refush;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
//    @ViewInject(R.id.mon_01)
//    private TextView tv_01;
//    @ViewInject(R.id.mon_02)
//    private TextView tv_02;
    private Profit profit;
    private MyAdapter myAdapter;
    private int next_offset = 0;
    private LoadMoreFooterView loadMoreFooterView;


    private ArrayList<Map<String, String>> friends_list;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_mon;
    }

    @Override
    public void requestData() {
        showProgressContent();
        profit.friends(next_offset, this, this);
    }

    @Override
    protected void initialized() {
        profit = new Profit();
        friends_list = new ArrayList<>();
        myAdapter = new MyAdapter();
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        if (var1.getUri().contains("friends")) {
            if (map.get("status").equals("200")) {
                Map<String, String> map1 = JSONUtils.parseDataToMap(var2);

                if (next_offset == 0) {
                    friends_list = JSONUtils.parseKeyAndValueToMapList(map1.get("friends"));
                } else {
                    friends_list.addAll(JSONUtils.parseKeyAndValueToMapList(map1.get("friends")));
                    if (Integer.parseInt(map1.get("next_offset")) == -1) {
                        frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                    } else {
                        frg_refush.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    }
                }
                next_offset = Integer.parseInt(map1.get("next_offset"));
                frg_refush.setRefreshing(false);
                myAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        frg_refush.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        showNetError();
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

    @Override
    public void onRefresh() {
        next_offset = 0;
        profit.friends(next_offset, MonAty.this, MonAty.this);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        profit.friends(next_offset, MonAty.this, MonAty.this);
    }

    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        profit.friends(next_offset, MonAty.this, MonAty.this);
    }

    @Event(value = {R.id.mon_imgv_back, R.id.mon_tv_more})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.mon_imgv_back:
                finish();
                break;
            case R.id.mon_tv_more:
                startActivity(YqAty.class, null);
                break;
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MonAty.this).inflate(R.layout.item_mon, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.tv_nickname.setText(friends_list.get(position).get("nickname"));
            viewHolder.tv_phone.setText(friends_list.get(position).get("username"));
            viewHolder.tv_money.setText(friends_list.get(position).get("profit") + "元");
        }

        @Override
        public int getItemCount() {
            return friends_list.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_mon_tv_nickname)
            private TextView tv_nickname;
            @ViewInject(R.id.item_mon_tv_phone)
            private TextView tv_phone;
            @ViewInject(R.id.item_mon_tv_money)
            private TextView tv_money;

            public ViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }

    }
}
