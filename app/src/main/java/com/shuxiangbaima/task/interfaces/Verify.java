package com.shuxiangbaima.task.interfaces;


import android.content.Context;
import com.shuxiangbaima.task.interfaces.utilss.a;
import com.shuxiangbaima.task.config.AppConfig;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Verify {

    private String module;
    private a aa;

    public Verify() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }

    /**
     * 注册获取验证码
     *
     * @param mobile      手机号
     * @param scene       获取类型(register|forget_password)
     * @param apiListener
     */
    public void send_vcode(String mobile, String scene, Context context, String captcha, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("scene", scene);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("captcha", captcha);
        map.put("url", AppConfig.BASE_URL + module + "/send_vcode");
        aa.aaNet(map, apiListener);
    }

    /**
     * 获取图形验证码
     *
     * @param scene 获取类型(send_vcode|login)
     */
    public String captcha(String scene, Context context) {
        Map<String, String> map = new HashMap<>();
        map.put("scene", scene);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/captcha");
        return aa.getUri(map);
    }


}
