package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;
import com.toocms.dink5.mylibrary.net.ApiTool;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

import com.shuxiangbaima.task.interfaces.utilss.a;

/**
 * Created by Administrator on 2016/8/19.
 */
public class Profile {

    private String module;
    private a aa;

    public Profile() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }

    /**
     * 获取用户信息
     *
     * @param apiListener
     */
    public void profile(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module);
        aa.aaNet(map, apiListener);
    }

    /**
     * 获取用户信息的选项
     *
     * @param apiListener
     */
    public void options(Context context, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/options");
        params.addBodyParameter("v", PreferencesUtils.getString(context, "v"));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 上传头像
     *
     * @param apiListener
     */
    public void modify_avatar(String avatar, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("avatar", avatar);
        map.put("url", AppConfig.BASE_URL + module + "/modify_avatar");
        aa.aaNet(map, apiListener);
    }

    /**
     * 修改昵称
     *
     * @param apiListener
     */
    public void modify_nickname(String nickname, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("nickname", nickname);
        map.put("url", AppConfig.BASE_URL + module + "/modify_nickname");
        aa.aaNet(map, apiListener);
    }

    /**
     * 修改性别
     *
     * @param apiListener
     */
    public void modify_gender(String gender, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("gender", gender);
        map.put("url", AppConfig.BASE_URL + module + "/modify_gender");
        aa.aaNet(map, apiListener);
    }

    /**
     * 修改性别
     *
     * @param apiListener
     */
    public void modify_profile(String age_range, String title, String hobbies, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("age_range", age_range);
        map.put("title", title);
        map.put("hobbies", hobbies);
        map.put("url", AppConfig.BASE_URL + module + "/modify_profile");
        aa.aaNet(map, apiListener);
    }

}
