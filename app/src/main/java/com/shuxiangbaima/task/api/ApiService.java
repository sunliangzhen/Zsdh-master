package com.shuxiangbaima.task.api;

import com.shuxiangbaima.task.ui.main.bean.PageBaseBean;
import com.shuxiangbaima.task.ui.main.bean.TaskBaseBean;
import com.toocms.dink5.mylibrary.viewpager.BannerBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
public interface ApiService {


    @FormUrlEncoded
    @POST("task/task_list")
    Observable<TaskBaseBean> task_list(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("task/recommend_task")
    Observable<PageBaseBean> recommend_task(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("ad/carousel")
    Observable<BannerBean> carousel(@FieldMap Map<String, String> fields);


}
