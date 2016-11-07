package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.utilss.a;
import com.shuxiangbaima.task.ui.main.presenter.PageListPresenter;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Ad {

    private String module;
    private a aa;
    private RetrofitIf service;

    public Ad() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = retrofit.create(RetrofitIf.class);
    }

    /**
     * 首页轮播图
     *
     * @param apiListener
     */
    public void carousel(Context context, PageListPresenter apiListener) {
        Map<String, String> map = new HashMap<>();
        if (Config.isLogin()) {
            map.put("user_id", PreferencesUtils.getString(context, "user_id"));
            map.put("token", PreferencesUtils.getString(context, "token"));
        }
        map.put("device", PreferencesUtils.getString(context, "deviceId"));

        String sign = aa.sortSign(map);
        String uri = aa.getUri2(map, AppConfig.BASE_URL + module + "/carousel");
        map.put("sign", sign);
        apiListener.getBannerListDataRequest(map);
    }
}
