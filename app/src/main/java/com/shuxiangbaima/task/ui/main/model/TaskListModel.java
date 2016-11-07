package com.shuxiangbaima.task.ui.main.model;


import com.shuxiangbaima.task.api.Api;
import com.shuxiangbaima.task.api.HostType;
import com.shuxiangbaima.task.ui.main.bean.TaskBaseBean;
import com.shuxiangbaima.task.ui.main.bean.TaskBean;
import com.shuxiangbaima.task.ui.main.contract.TaskListContract;
import com.toocms.dink5.mylibrary.baserx.RxSchedulers;

import java.util.Map;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by xsf
 */
public class TaskListModel implements TaskListContract.Model {


    @Override
    public Observable<TaskBean> getTaskListData(Map<String, String> fields) {
        return Api.getDefault(HostType.GANK_GIRL_PHOTO)
                .task_list(fields)
                .map(new Func1<TaskBaseBean, TaskBean>() {
                    @Override
                    public TaskBean call(TaskBaseBean girlData) {
                        return girlData.getData();
                    }
                })
                .compose(RxSchedulers.<TaskBean>io_main());
    }
}
