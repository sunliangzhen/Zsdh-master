package com.shuxiangbaima.task.config;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.shuxiangbaima.task.interfaces.utilss.a;
import com.toocms.dink5.mylibrary.baseapp.BaseApplication;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Myappcation extends BaseApplication {

    private String deviceId;

    @Override
    public void onCreate() {
        super.onCreate();

        deviceId = a.getMD5Str(a.getDeviceId(this));
        PreferencesUtils.putString(this, "品牌", android.os.Build.BRAND);
        PreferencesUtils.putString(this, "deviceId", deviceId);
        PreferencesUtils.putString(this, "系统版本", Build.VERSION.RELEASE);
        PreferencesUtils.putString(this, "型号", android.os.Build.MODEL);
        PreferencesUtils.putString(this, "操作系统", System.getProperty("os.name"));
        if (TextUtils.isEmpty(PreferencesUtils.getString(this, "v"))) {
            PreferencesUtils.putString(this, "v", "0");
        }
        Log.e("r", "系统版本=" + Build.VERSION.RELEASE);
        Log.e("r", "SDK=" + Build.VERSION.SDK);
        Log.e("r", "型号=" + android.os.Build.MODEL);
        Log.e("r", "品牌=" + android.os.Build.BRAND);
        Log.e("r", "操作系统=" + System.getProperty("os.name"));
        Log.e("r", "deviceId=" + deviceId);
//        Log.e("r", "PREVIEW_SDK_INT=" + Build.VERSION.PREVIEW_SDK_INT);
//        Log.e("r", "SDK_INT=" + Build.VERSION.SDK_INT);
//        String phoneInfo = "Product: " + android.os.Build.PRODUCT;
//        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
//        phoneInfo += ", TAGS: " + android.os.Build.TAGS;
//        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
//        phoneInfo += ", MODEL: " + android.os.Build.MODEL;
//        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
//        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
//        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
//        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
//        phoneInfo += ", BRAND: " + android.os.Build.BRAND;
//        phoneInfo += ", BOARD: " + android.os.Build.BOARD;
//        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
//        phoneInfo += ", ID: " + android.os.Build.ID;
//        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
//        phoneInfo += ", USER: " + android.os.Build.USER;
//        Log.e("r", phoneInfo);
//        regToWx();
    }

    public String getDeviced() {
        return deviceId;
    }

//    private static final String APP_ID = "wxa1ad4eee69fb5608";
//    private IWXAPI api;
//
//    private void regToWx() {
//        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
//        api.registerApp(APP_ID);
//    }
}
