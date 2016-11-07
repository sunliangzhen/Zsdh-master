package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.utilss.a;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Cash {

    private String module;
    private a aa;

    public Cash() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }


    /**
     * 35. 提现，获取当前提现中的资金  POST
     *
     * @param apiListener
     */
    public void my_cash(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/my_cash ");
        aa.aaNet(map, apiListener);
    }

    /**
     * 36. 申请提现 POST
     *
     * @param apiListener
     */
    public void apply_cash(float cash, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("cash", String.valueOf(cash));
        map.put("url", AppConfig.BASE_URL + module + "/apply_cash ");
        aa.aaNet(map, apiListener);
    }

    /**
     * 37. 提现历史记录 POST
     *
     * @param apiListener
     */
    public void cash_list(int offset, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("offset", String.valueOf(offset));
        map.put("url", AppConfig.BASE_URL + module + "/cash_list ");
        aa.aaNet(map, apiListener);
    }

    /**
     * 40. 获取最近提现列表（首页滚屏） POST
     *
     * @param apiListener
     */
    public void recent_cash(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        if(Config.isLogin()){
            map.put("user_id", PreferencesUtils.getString(context, "user_id"));
            map.put("token", PreferencesUtils.getString(context, "token"));
        }
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/recent_cash ");
        aa.aaNet(map, apiListener);
    }
}
