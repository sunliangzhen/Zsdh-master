package com.shuxiangbaima.task.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.shuxiangbaima.task.ui.main.activity.DetailsAty;
import com.shuxiangbaima.task.ui.main.bean.TaskBean;
import com.toocms.dink5.mylibrary.ire.IRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private Context context;
    private List<TaskBean.TaskListEntity> task_list;
//    private ImageOptions imageOptions;
    private IRecyclerView recyclerView;

    public TaskListAdapter(Context context, List<TaskBean.TaskListEntity> task_list, IRecyclerView recyclerView) {
        this.context = context;
        this.task_list = task_list;
        this.recyclerView = recyclerView;
//        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
    }

    public int getScrolledPosition() {
        int firstItemPosition = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            firstItemPosition = linearManager.findFirstVisibleItemPosition();
        }
        return firstItemPosition;
    }

    public int getDataCount() {
        if (task_list != null) {
            return task_list.size();
        } else {
            return 0;
        }
    }

    public void replaceAll(List<TaskBean.TaskListEntity> elements) {
        if (task_list.size() > 0) {
            task_list.clear();
        }
        task_list.addAll(elements);
        task_list.addAll(elements);
        task_list.addAll(elements);
        notifyDataSetChanged();
    }

    public void addAll(List<TaskBean.TaskListEntity> elements) {
        task_list.addAll(elements);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_page_lv, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        switch (task_list.get(position).getTaskType()) {
            case "1":
                viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share);
                viewHolder.tv_type.setTextColor(0xffff0000);
                viewHolder.tv_type.setText("分享");
                break;
            case "2":
                viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share2);
                viewHolder.tv_type.setTextColor(0xffEBC581);
                viewHolder.tv_type.setText("试用");
                break;
            case "3":
                viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share3);
                viewHolder.tv_type.setTextColor(0xff2693EE);
                viewHolder.tv_type.setText("游戏");
                break;
            case "4":
                viewHolder.tv_type.setBackgroundResource(R.drawable.shape_page_share4);
                viewHolder.tv_type.setTextColor(0xff2EEE26);
                viewHolder.tv_type.setText("充值");
                break;
        }
        viewHolder.tv_title.setText(task_list.get(position).getTaskName());
        viewHolder.tv_mon.setText("￥" + task_list.get(position).getProfit());
        viewHolder.tv_num.setText("剩余：" + task_list.get(position).getLeftNum());
        viewHolder.tv_title.setText(task_list.get(position).getTaskName());
        switch (task_list.get(position).getCurrentStatus()) {
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
//        x.image().bind(viewHolder.imav_cover, task_list.get(position).getFigure(), imageOptions);
        Glide.with(context)
                .load(task_list.get(position).getFigure())
                .into(viewHolder.imav_cover);
        viewHolder.page_linlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsAty.class);
                Bundle bundle = new Bundle();
                bundle.putString("task_id", task_list.get(position).getTaskId());
                bundle.putString("task_type", task_list.get(position).getTaskType());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
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
        @ViewInject(R.id.page_linlay)
        private LinearLayout page_linlay;

        public MyViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
            AutoUtils.autoSize(itemView);
        }
    }
}
