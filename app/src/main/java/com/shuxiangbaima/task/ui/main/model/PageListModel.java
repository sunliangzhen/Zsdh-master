package com.shuxiangbaima.task.ui.main.model;


import com.shuxiangbaima.task.api.Api;
import com.shuxiangbaima.task.api.HostType;
import com.shuxiangbaima.task.ui.main.bean.PageBaseBean;
import com.shuxiangbaima.task.ui.main.bean.PageBean;
import com.shuxiangbaima.task.ui.main.contract.PageListContract;
import com.toocms.dink5.mylibrary.baserx.RxSchedulers;
import com.toocms.dink5.mylibrary.viewpager.BannerBean;

import java.util.Map;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by xsf
 */
public class PageListModel implements PageListContract.Model {


    @Override
    public Observable<PageBean> getTaskListData(Map<String, String> fields) {
        return Api.getDefault(HostType.GANK_GIRL_PHOTO)
                .recommend_task(fields)
                .map(new Func1<PageBaseBean, PageBean>() {
                    @Override
                    public PageBean call(PageBaseBean girlData) {
                        return girlData.getData();
                    }
                })
                .compose(RxSchedulers.<PageBean>io_main());
    }

    @Override
    public Observable<BannerBean> getBannerListData(Map<String, String> fields) {
        return Api.getDefault(HostType.GANK_GIRL_PHOTO)
                .carousel(fields)
                .compose(RxSchedulers.<BannerBean>io_main());
    }
}
