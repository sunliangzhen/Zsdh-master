package com.shuxiangbaima.task.ui.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Profit;
import com.shuxiangbaima.task.ui.main.activity.MyTaskAty;
import com.shuxiangbaima.task.ui.notice.DividerItemDecoration;
import com.shuxiangbaima.task.ui.notice.NoticeDetailsAty;
import com.shuxiangbaima.task.ui.notice.NoticeFragment1;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.ImageUtils;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Administrator on 2016/8/11.
 */
public class PaiAty extends BaseAty implements LoadingTip.onReloadListener {

    @ViewInject(R.id.pai_tv_rank)
    private TextView tv_rank;
    @ViewInject(R.id.pai_tv_total_profit)
    private TextView tv_total_profit;
    @ViewInject(R.id.pai_imgv)
    private ImageView imgv_head;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.swiprefresh)
    private SwipeRefreshLayout swiprefresh;
    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
    @ViewInject(R.id.top)
    private RelativeLayout top_relay;

    private MyAdapter myAdapter;
    private Profit profit;
    private ArrayList<Map<String, String>> top;
    private ImageLoader imageLoader;
    //    private ImageOptions imageOptions;
    private String total_profit;
    private String rank;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_pai;
    }

    @Override
    public void requestData() {
        showProgressContent();
        profit.profit_top(this, this);
    }

    @Override
    protected void initialized() {
        myAdapter = new MyAdapter();
        profit = new Profit();
        top = new ArrayList<>();
        imageLoader = application.getImageLoader();

//        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();

    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        swiprefresh.setRefreshing(false);
        top_relay.setVisibility(View.VISIBLE);
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        if (var1.getUri().contains("profit_top")) {
            if (map.get("status").equals("200")) {
                Map<String, String> map1 = JSONUtils.parseDataToMap(var2);
                total_profit = map1.get("total_profit");
                rank = map1.get("rank");
                tv_total_profit.setText(total_profit + "元");
//                if (total_profit.equals("0")) {
//                    tv_rank.setText("无收益");
//                } else {
//                    tv_rank.setText("排名：" + rank);
//                }
                tv_rank.setText("排名：" + rank);
                top = JSONUtils.parseKeyAndValueToMapList(map1.get("top"));
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (top.size() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            top_relay.setVisibility(View.GONE);
        }
        showNetError();
        swiprefresh.setRefreshing(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MaterialHeader header = new MaterialHeader(this);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(myAdapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        swiprefresh.setProgressViewEndTarget(true, AutoUtils.getPercentHeightSize(300));
        swiprefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                profit.profit_top(PaiAty.this, PaiAty.this);
            }
        });
        swiprefresh.setColorSchemeResources(R.color.red, R.color.blue, R.color.yellow, R.color.green);
        Glide.with(this)
                .load(application.getUserInfo().get("avatar"))
                .placeholder(R.drawable.default_head)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imgv_head.setImageDrawable(resource);
                    }
                });
        tv_name.setText(application.getUserInfo().get("nickname"));
        loadedTip.setOnReloadListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.pai_tv_up, R.id.pai_imgv_tosun, R.id.pai_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.pai_tv_up:
                PaiAty.this.setResult(-1);
                finish();
                break;
            case R.id.pai_imgv_tosun:
                Bundle bundle = new Bundle();
                bundle.putString("total_profit", total_profit);
                bundle.putString("rank", rank);
                startActivity(SunpaiAty.class, bundle);
                break;
            case R.id.pai_imgv_back:
                finish();
                break;
        }
    }

    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        profit.profit_top(this, this);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PaiAty.this).inflate(R.layout.item_pai_lv, parent, false);
            return new MyAdapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder viewHolder, final int position) {
            viewHolder.tv_rank.setText(top.get(position).get("rank"));
            if (position == 0 || position == 1 || position == 2) {
                viewHolder.tv_rank.setBackground(getResources().getDrawable(R.drawable.shape_top));
                viewHolder.tv_rank.setTextColor(0xffffffff);
            } else {
                viewHolder.tv_rank.setBackground(getResources().getDrawable(R.drawable.shape_top_normal));
                viewHolder.tv_rank.setTextColor(0xff000000);
            }
            viewHolder.tv_name.setText(top.get(position).get("nickname"));
            viewHolder.tv_money.setText(top.get(position).get("profit") + "元");
//            x.image().bind(viewHolder.imgv_head, top.get(position).get("avatar"), imageOptions);
            ImageUtils.display(PaiAty.this, top.get(position).get("avatar"), viewHolder.imgv_head);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return top.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_tv_rank)
            private TextView tv_rank;
            @ViewInject(R.id.item_pai_tv_name)
            private TextView tv_name;
            @ViewInject(R.id.item_tv_money)
            private TextView tv_money;
            @ViewInject(R.id.item_imgv_head)
            private ImageView imgv_head;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }


}
