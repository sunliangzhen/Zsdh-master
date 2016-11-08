package com.shuxiangbaima.task.ui.mine.below;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Profit;
import com.shuxiangbaima.task.ui.mine.PaiAty;
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

/**
 * Created by Administrator on 2016/9/14.
 */
public class IncomstateAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener, LoadingTip.onReloadListener {

    @ViewInject(R.id.iRecyclerView)
    private IRecyclerView iRecyclerView;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;

    private Profit profit;
    //    private ImageOptions imageOptions;
    private int next_offset = 0;
    private ArrayList<Map<String, String>> task_list;
    private MyAdapter myAdapter;
    private LoadMoreFooterView loadMoreFooterView;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_incoms;
    }

    @Override
    protected void initialized() {
        task_list = new ArrayList<>();
        profit = new Profit();
        myAdapter = new MyAdapter();
//        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
    }

    @Override
    public void requestData() {
        showProgressContent();
        profit.profit_detail(next_offset, this, this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClassicRefreshHeaderView classicRefreshHeaderView = new ClassicRefreshHeaderView(this);
        classicRefreshHeaderView.setBackgroundColor(0xff313131);
        classicRefreshHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(150)));
        iRecyclerView.setRefreshHeaderView(classicRefreshHeaderView);
        iRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        iRecyclerView.setAdapter(myAdapter);
        iRecyclerView.setOnRefreshListener(this);
        iRecyclerView.setOnLoadMoreListener(this);
        loadMoreFooterView = (LoadMoreFooterView) iRecyclerView.getLoadMoreFooterView();
        loadedTip.setOnReloadListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.incoms_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.incoms_imgv_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        next_offset = 0;
        iRecyclerView.setRefreshing(true);
        iRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
        profit.profit_detail(next_offset, IncomstateAty.this, IncomstateAty.this);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        if (loadMoreFooterView.canLoadMore() && myAdapter.getItemCount() > 0) {
            iRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
            profit.profit_detail(next_offset, IncomstateAty.this, IncomstateAty.this);
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        if (var1.getUri().contains("profit_detail") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            Map<String, String> map = JSONUtils.parseDataToMap(var2);
            if (next_offset == 0) {
                task_list = JSONUtils.parseKeyAndValueToMapList(map.get("profit_list"));
            } else {
                task_list.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("profit_list")));
                if (Integer.parseInt(map.get("next_offset")) == -1) {
                    iRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                } else {
                    iRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                }
            }
            next_offset = Integer.parseInt(map.get("next_offset"));
            iRecyclerView.setRefreshing(false);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        iRecyclerView.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (task_list.size() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        }
        showNetError();
    }

    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        profit.profit_detail(next_offset, this, this);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(IncomstateAty.this).inflate(R.layout.item_page_lv, parent, false);

            return new MyAdapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder viewHolder, final int position) {
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
            viewHolder.tv_01.setText("收入： ");
            viewHolder.tv_title.setText(task_list.get(position).get("task_name"));
            viewHolder.tv_mon.setText("￥" + task_list.get(position).get("profit"));
            viewHolder.tv_title.setText(task_list.get(position).get("task_name"));
            viewHolder.tv_state.setVisibility(View.GONE);
//            x.image().bind(viewHolder.imav_cover, task_list.get(position).get("figure"), imageOptions);
            ImageUtils.display(IncomstateAty.this, task_list.get(position).get("figure"), viewHolder.imav_cover);

        }

        @Override
        public int getItemCount() {
            return task_list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

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
            @ViewInject(R.id.item_pagelv_tv_01)
            private TextView tv_01;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

}
