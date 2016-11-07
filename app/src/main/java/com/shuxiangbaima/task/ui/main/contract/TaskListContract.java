package com.shuxiangbaima.task.ui.main.contract;


import com.shuxiangbaima.task.ui.main.bean.TaskBean;
import com.toocms.dink5.mylibrary.base.BaseModel;
import com.toocms.dink5.mylibrary.base.BasePresenter;
import com.toocms.dink5.mylibrary.base.BaseView;

import java.util.Map;

import rx.Observable;


/**
 * des:新闻列表contract
 * Created by xsf
 * on 2016.09.14:38
 */
public interface TaskListContract {

    interface Model extends BaseModel {
        //请求获取新闻
        Observable<TaskBean> getTaskListData(Map<String, String> fields);
    }

    interface View extends BaseView {
        //返回获取的新闻
        void returnTaskListData(TaskBean taskBeanList);

//        //返回顶部
//        void scrolltoTop();
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取新闻请求
        public abstract void getTaskListDataRequest(Map<String, String> fields);
    }
}
