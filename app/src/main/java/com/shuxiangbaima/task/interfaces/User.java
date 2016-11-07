package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;
import com.shuxiangbaima.task.interfaces.utilss.a;
/**
 * Created by Administrator on 2016/8/17.
 */
public class User {

    private String module;
    private a aa;

    public User() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }


    /**
     * 用户注册
     *
     * @param apiListener
     */
    public void register(String username, String password, String vcode, String invcode, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("vcode", vcode);
        map.put("invcode", invcode);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("brand", PreferencesUtils.getString(context, "品牌"));
        map.put("model", PreferencesUtils.getString(context, "型号"));
        map.put("os", PreferencesUtils.getString(context, "操作系统"));
        map.put("os_version", PreferencesUtils.getString(context, "系统版本"));

        map.put("url", AppConfig.BASE_URL + module + "/register");
        aa.aaNet(map, apiListener);
    }

    /**
     * 用户登陆
     *
     * @param apiListener
     */
    public void login(String username, String password, String captcha, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("captcha", captcha);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("brand", PreferencesUtils.getString(context, "品牌"));
        map.put("model", PreferencesUtils.getString(context, "型号"));
        map.put("os", PreferencesUtils.getString(context, "操作系统"));
        map.put("os_version", PreferencesUtils.getString(context, "系统版本"));
        map.put("url", AppConfig.BASE_URL + module + "/login");
        aa.aaNet(map, apiListener);
    }

    /**
     * 用户退出登陆
     *
     * @param apiListener
     */
    public void logout(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/logout");
        aa.aaNet(map, apiListener);
    }

    /**
     * 用户退出登陆
     *
     * @param apiListener
     */
    public void valid_username(String username, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("url", AppConfig.BASE_URL + module + "/valid_username");
        aa.aaNet(map, apiListener);
    }
}
