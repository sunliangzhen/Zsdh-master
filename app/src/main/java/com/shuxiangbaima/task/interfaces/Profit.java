package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;
import com.shuxiangbaima.task.interfaces.utilss.a;
/**
 * Created by Administrator on 2016/9/1.
 */
public class Profit {

    private String module;
    private a aa;

    public Profit() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }

    /**
     * 获取用户近期收益
     *
     * @param apiListener
     */
    public void recent_profit(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/recent_profit");
        aa.aaNet(map, apiListener);
    }

    /**
     * 获取收益排行榜
     *
     * @param apiListener
     */
    public void profit_top(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/profit_top");
        aa.aaNet(map, apiListener);
    }

    /**
     * 获取好友（找人一起赚） POST
     *
     * @param apiListener
     */
    public void friends(int p, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("offset", String.valueOf(p));
        map.put("url", AppConfig.BASE_URL + module + "/friends");
        aa.aaNet(map, apiListener);
    }

    /**
     * 34. 收益明细 POST
     *
     * @param apiListener
     */
    public void profit_detail(int offset, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("offset", String.valueOf(offset));
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/profit_detail");
        aa.aaNet(map, apiListener);
    }
}
