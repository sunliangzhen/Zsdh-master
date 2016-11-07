package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;
import com.shuxiangbaima.task.interfaces.utilss.a;
/**
 * Created by Administrator on 2016/8/23.
 */
public class Suggestion {

    private String module;
    private a aa;

    public Suggestion() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }

    /**
     * 问题反馈  POST
     *
     * @param apiListener
     */
    public void submit(String contact, String suggestion, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("contact", contact);
        map.put("suggestion", suggestion);
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/submit");
        aa.aaNet(map, apiListener);
    }
}
