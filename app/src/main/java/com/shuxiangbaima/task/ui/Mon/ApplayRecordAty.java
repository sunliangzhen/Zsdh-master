package com.shuxiangbaima.task.ui.Mon;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Cash;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.ire.IRecyclerView;
import com.toocms.dink5.mylibrary.ire.OnLoadMoreListener;
import com.toocms.dink5.mylibrary.ire.OnRefreshListener;
import com.toocms.dink5.mylibrary.ire.widget.ClassicRefreshHeaderView;
import com.toocms.dink5.mylibrary.ire.widget.LoadMoreFooterView;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ApplayRecordAty extends BasAty implements OnRefreshListener, OnLoadMoreListener, LoadingTip.onReloadListener {


    @ViewInject(R.id.applaymon_ptr_frame)
    private IRecyclerView frg_refush;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;

    private int next_offset = 0;
    private Cash cash;
    private ArrayList<Map<String, String>> cash_list;
    private MyAdapter myAdapter;
    private LoadMoreFooterView loadMoreFooterView;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_applay_record;
    }

    @Override
    protected void initialized() {
        cash = new Cash();
        cash_list = new ArrayList<>();
        myAdapter = new MyAdapter();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        cash.cash_list(next_offset, this, this);
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
        frg_refush.setAdapter(myAdapter);
        loadedTip.setOnReloadListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.applayre_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.applayre_imgv_back:
                finish();
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (var1.getUri().contains("cash_list") && (JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200"))) {
            Map<String, String> map = JSONUtils.parseDataToMap(var2);
            if (next_offset == 0) {
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
                cash_list = JSONUtils.parseKeyAndValueToMapList(map.get("cash_list"));
                if (cash_list.size() < 1) {
                    loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
                } else {
                    loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
                }
            } else {
                cash_list.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("cash_list")));
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
    public void onRefresh() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        next_offset = 0;
        cash.cash_list(next_offset, this, this);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        if (cash_list.size() > 0) {
            cash.cash_list(next_offset, this, this);
        }
    }

    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        next_offset = 0;
        cash.cash_list(next_offset, this, this);
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        frg_refush.setRefreshing(false);
        showNetError();
        if (cash_list.size() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ApplayRecordAty.this).inflate(R.layout.item_applay_record, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.tv_mon.setText("—" + cash_list.get(position).get("cash"));
            viewHolder.tv_time.setText(cash_list.get(position).get("add_time"));
            switch (cash_list.get(position).get("current_status")) {
                case "1":
                    viewHolder.tv_state.setText("打款中");
                    break;
                case "2":
                    viewHolder.tv_state.setText("打款中");
                    break;
                case "3":
                    viewHolder.tv_state.setText("提现失败");
                    break;
                case "4":
                    viewHolder.tv_state.setText("提现成功");
                    break;
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return cash_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_applayre_tv_state)
            private TextView tv_state;
            @ViewInject(R.id.item_applayre_tv_time)
            private TextView tv_time;
            @ViewInject(R.id.item_applayre_tv_mon)
            private TextView tv_mon;

            public ViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }

    }

}
