package com.shuxiangbaima.task.ui.main.contract;


import com.shuxiangbaima.task.ui.main.bean.PageBean;
import com.toocms.dink5.mylibrary.base.BaseModel;
import com.toocms.dink5.mylibrary.base.BasePresenter;
import com.toocms.dink5.mylibrary.base.BaseView;
import com.toocms.dink5.mylibrary.viewpager.BannerBean;

import java.util.Map;

import rx.Observable;


public interface PageListContract {

    interface Model extends BaseModel {
        Observable<PageBean> getTaskListData(Map<String, String> fields);

        Observable<BannerBean> getBannerListData(Map<String, String> fields);
    }

    interface View extends BaseView {
        void returnTaskListData(PageBean taskBeanList);

        void returnBannerListData(BannerBean taskBeanList);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        public abstract void getPageListDataRequest(Map<String, String> fields);

        public abstract void getBannerListDataRequest(Map<String, String> fields);
    }
}
