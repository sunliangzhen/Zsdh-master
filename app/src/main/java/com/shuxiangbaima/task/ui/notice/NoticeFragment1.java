package com.shuxiangbaima.task.ui.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Notice;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.base.BaseFragment;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.net.ApiListener;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
public class NoticeFragment1 extends BaseFragment implements ApiListener, LoadingTip.onReloadListener {

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewInject(R.id.notice_swiprefresh)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;

    private String type;
    private MyAdapter myAdapter;
    private Notice notice;
    private ArrayList<Map<String, String>> notice_list;
    private ArrayList<Map<String, String>> notice_list2;

    public NoticeFragment1 newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        this.setArguments(bundle);
        return this;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        notice.notice_list(getActivity(), this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.frg_notice;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {
        notice_list2 = new ArrayList<>();
        Bundle arguments = getArguments();
        type = arguments.getString("type", "1");
        myAdapter = new MyAdapter();
        notice = new Notice();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
//        swipeRefreshLayout.setProgressViewEndTarget(true, AutoUtils.getPercentHeightSize(300));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notice.notice_list(getActivity(), NoticeFragment1.this);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.blue, R.color.yellow, R.color.green);
        loadedTip.setOnReloadListener(this);

    }

    @Override
    public void onCancelled(Callback.CancelledException var1) {
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        swipeRefreshLayout.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (var1.getUri().contains("notice_list") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            Map<String, String> map = JSONUtils.parseDataToMap(var2);
            if (!map.get("count").equals("0")) {
                notice_list = JSONUtils.parseKeyAndValueToMapList(map.get("notice_list"));
                if (type.equals("1")) {
                    PreferencesUtils.putString(getActivity(), "notice_data", map.get("notice_list"));
                }
                notice_list2.clear();
                for (int i = 0; i < notice_list.size(); i++) {
                    if (notice_list.get(i).get("notice_type").equals(type)) {
                        notice_list2.add(notice_list.get(i));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(Map<String, String> var1, RequestParams var2) {

    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        swipeRefreshLayout.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        showNetError();
        if (notice_list2.size() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        }
    }

    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        notice.notice_list(getActivity(), this);
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_notice, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            String date = sdf.format(new Date(Long.parseLong(notice_list2.get(position).get("send_time")) * 1000L));

            holder.tv_content.setText(notice_list2.get(position).get("title"));
            holder.tv_time.setText(date);
            if (PreferencesUtils.getInt(getActivity(), notice_list2.get(position).get("notice_id"), 0) == 1) {
                holder.tv_content.setTextColor(0xff999999);
                holder.tv_time.setTextColor(0xff999999);
            } else {
                holder.tv_content.setTextColor(0xff000000);
                holder.tv_time.setTextColor(0xff000000);
            }
            holder.notice_linlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    bundle.putString("notice_id", notice_list2.get(position).get("notice_id"));
                    bundle.putString("link", notice_list2.get(position).get("link"));
                    startActivity(NoticeDetailsAty.class, bundle);
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return notice_list2.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_notice_content)
            TextView tv_content;
            @ViewInject(R.id.item_notice_time)
            TextView tv_time;
            @ViewInject(R.id.item_notice_linlay)
            LinearLayout notice_linlay;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

}
