package com.shuxiangbaima.task.interfaces;

import android.content.Context;

import com.shuxiangbaima.task.config.AppConfig;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;

import java.util.HashMap;
import java.util.Map;
import com.shuxiangbaima.task.interfaces.utilss.a;
/**
 * Created by Administrator on 2016/8/22.
 */
public class Security {

    private String module;
    private a aa;

    public Security() {
        aa = new a();
        module = this.getClass().getSimpleName().toLowerCase(); // 模块名
    }

    /**
     * 忘记密码，通过该接口返回用户绑定的手机号，用于接收短信验证码验证用户身份 POST
     *
     * @param apiListener
     */
    public void forgot_password(String username, String captcha, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("captcha", captcha);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/forgot_password");
        aa.aaNet(map, apiListener);
    }

    /**
     * 忘记密码，通过短信验证码验证用户身份  POST
     *
     * @param apiListener
     */
    public void cert_by_mobile(String user_id, String mobile, String vcode, String scene, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("mobile", mobile);
        map.put("vcode", vcode);
        map.put("scene", scene);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/cert_by_mobile");
        aa.aaNet(map, apiListener);
    }

    /**
     * 忘记密码，设置新密码  POST
     *
     * @param apiListener
     */
    public void set_password(String user_id, String password, String sac, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("password", password);
        map.put("sac", sac);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/set_password");
        aa.aaNet(map, apiListener);
    }

    /**
     * 修改旧密码  POST
     *
     * @param apiListener
     */
    public void modify_password(String old_password, String new_password, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("old_password", old_password);
        map.put("new_password", new_password);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/modify_password");
        aa.aaNet(map, apiListener);
    }

    /**
     * 绑定支付宝账号  POST
     *
     * @param apiListener
     */
    public void bind_alipay(String sac, String alipay_account, String alipay_name, Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("sac", sac);
        map.put("alipay_account", alipay_account);
        map.put("alipay_name", alipay_name);
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/bind_alipay");
        aa.aaNet(map, apiListener);
    }

    /**
     * 获取当前登录用户的绑定支付宝账号   POST
     *
     * @param apiListener
     */
    public void get_bind_alipay(Context context, ApiListener apiListener) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", PreferencesUtils.getString(context, "user_id"));
        map.put("token", PreferencesUtils.getString(context, "token"));
        map.put("device", PreferencesUtils.getString(context, "deviceId"));
        map.put("url", AppConfig.BASE_URL + module + "/get_bind_alipay");
        aa.aaNet(map, apiListener);
    }
}
