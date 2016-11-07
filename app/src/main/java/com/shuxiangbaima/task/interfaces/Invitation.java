package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.utilss.a;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Invitation {

    private String module;
    private a aa;

    public Invitation() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }

    /**
     * 邀请码 POST
     *
     * @param apiListener
     */
    public void invcode(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("url", AppConfig.BASE_URL + module + "/invcode");
        aa.aaNet(map, apiListener);
    }
}
