package com.toocms.dink5.mylibrary.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.toocms.dink5.mylibrary.commonutils.FileManager;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.Settings;
import com.toocms.dink5.mylibrary.commonutils.utils.ListUtils;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Zero
 * @date 2016/4/11 10:13
 */
public class WeApplication extends Application {
    private AppManager appManager;
    private Map<String, String> userInfo;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.appManager = AppManager.getInstance();
        this.initUserInfo();
        start();
    }

    private void finish() {
        this.appManager.AppExit(this);
        System.gc();
    }

    private void initUserInfo() {
        this.userInfo = new HashMap();
        String keys = PreferencesUtils.getString(this, "user_info");
        if (!TextUtils.isEmpty(keys)) {
            String[] userInfos = keys.split(",");
            String[] var3 = userInfos;
            int var4 = userInfos.length;
            for (int var5 = 0; var5 < var4; ++var5) {
                String key = var3[var5];
                this.userInfo.put(key, PreferencesUtils.getString(this, key));
            }
        }
    }

    private void start() {
        Settings.displayWidth = getScreenWidth(this);
        Settings.displayHeight = getScreenHeight(this);
        Settings.cacheCompressPath = FileManager.getCompressFilePath();
        Settings.crashLogPath = FileManager.getCrashLogFilePath();
        Settings.voicePath = FileManager.getVoiceFilePath();
        (new File(Settings.cacheCompressPath)).mkdirs();
        (new File(Settings.crashLogPath)).mkdirs();
        (new File(Settings.voicePath)).mkdirs();
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    public Map<String, String> getUserInfo() {
        return this.userInfo;
    }

    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }

    public void setUserInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
        ArrayList keys = new ArrayList();
        Iterator iterator = this.userInfo.keySet().iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            keys.add(key);
            String value = (String) this.userInfo.get(key);
            PreferencesUtils.putString(this, key, value);
        }
        PreferencesUtils.putString(this, "user_info", ListUtils.join(keys));
    }

    public void setUserInfoItem(String key, String value) {
        this.userInfo.put(key, value);
        PreferencesUtils.putString(this, key, value);
    }
}