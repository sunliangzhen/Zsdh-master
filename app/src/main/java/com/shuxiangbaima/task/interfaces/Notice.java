package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.utilss.a;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Notice {

    private String module;
    private a aa;

    public Notice() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }


    /**
     * 38. 查看是否有新通知 POST
     *
     * @param apiListener
     */
    public void new_notice(String since, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("since", since);
        map.put("url", AppConfig.BASE_URL + module + "/new_notice ");
        aa.aaNet(map, apiListener);
    }

    /**
     * 39. 获取通知列表 POST
     *
     * @param apiListener
     */
    public void notice_list(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/notice_list");
        aa.aaNet(map, apiListener);
    }
}
