package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.utilss.a;
import com.shuxiangbaima.task.ui.main.presenter.PageListPresenter;
import com.shuxiangbaima.task.ui.main.presenter.TaskListPresenter;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Task {

    private String module;
    private a aa;
    private RetrofitIf service;

    public Task() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = retrofit.create(RetrofitIf.class);
    }


    /**
     * 首页任务列表 POST
     *
     * @param apiListener
     */

    public void recommend_task(Context context, PageListPresenter apiListener) {
        Map<String, String> map = new HashMap<>();
        if (Config.isLogin()) {
            map.put("user_id", PreferencesUtils.getString(context, "user_id"));
            map.put("token", PreferencesUtils.getString(context, "token"));
        }
        map.put("device", PreferencesUtils.getString(context, "deviceId"));

        String sign = aa.sortSign(map);
        String uri = aa.getUri2(map, AppConfig.BASE_URL + module + "/recommend_task");
        map.put("sign", sign);
        apiListener.getPageListDataRequest(map);
    }

    /**
     * 任务列表 POST
     *
     * @param apiListener
     */
    public void task_list(String task_type, String order_by, int offset, Context context, TaskListPresenter apiListener) {
        Map<String, String> map = new HashMap<>();
        if (Config.isLogin()) {
            map.put("user_id", PreferencesUtils.getString(context, "user_id"));
            map.put("token", PreferencesUtils.getString(context, "token"));
        }
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("task_type", task_type);
        map.put("order_by", order_by);
        map.put("offset", String.valueOf(offset));

        String sign = aa.sortSign(map);
        String uri = aa.getUri2(map, AppConfig.BASE_URL + module + "/task_list");
        map.put("sign", sign);
        apiListener.getTaskListDataRequest(map);

//        Call<String> call = service.task_list(map);
//        ApiTool2 apiTool2 = new ApiTool2();
//        apiTool2.postApi(call, apiListener, uri);
    }

    /**
     * 任务详情 POST
     *
     * @param apiListener
     */
    public void task_info(String task_id, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        if (Config.isLogin()) {
            map.put("user_id", PreferencesUtils.getString(context, "user_id"));
            map.put("token", PreferencesUtils.getString(context, "token"));
        }
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("task_id", task_id);
        map.put("url", AppConfig.BASE_URL + module + "/task_info");
        aa.aaNet(map, apiListener);
    }

    /**
     * 30. 获取分享链接 POST    必须登录
     *
     * @param apiListener
     */
    public void share_link(String task_id, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("task_id", task_id);
        map.put("url", AppConfig.BASE_URL + module + "/share_link");
        aa.aaNet(map, apiListener);
    }

    /**
     * 31. 获取任务提交字段信息 POST    必须登录
     *
     * @param apiListener
     */
    public void submit_fields(String task_id, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("task_id", task_id);
        map.put("url", AppConfig.BASE_URL + module + "/submit_fields");
        aa.aaNet(map, apiListener);
    }

    /**
     * 32. 提交任务 POST
     *
     * @param apiListener
     */
    public void submit_task(Map<String, String> maps, String task_id, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("task_id", task_id);
        Iterator iterator = maps.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = (String) maps.get(key);
            map.put(key, value);
        }
        map.put("url", AppConfig.BASE_URL + module + "/submit_task");
        aa.aaNet2(map, apiListener);
    }

    /**
     * 33. 我的任务 POST
     *
     * @param apiListener
     */
    public void my_task(int offset, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("offset", String.valueOf(offset));
        map.put("url", AppConfig.BASE_URL + module + "/my_task");
        aa.aaNet2(map, apiListener);
    }

}
