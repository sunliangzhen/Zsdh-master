package com.toocms.dink5.mylibrary.app;

import com.toocms.dink5.mylibrary.app.AppManager;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/7/11 11:20
 */
public class Config {
    public static final String DEFAULT_NOTIFY_URL = "http://www.toocms.com/OAuth2.html";
    public static final String PREF_KEY_LOGIN_STATE = "PREF_KEY_LOGIN_STATE";
    public static final String PREF_KEY_USE_SNACKBAR = "PREF_KEY_USE_SNACKBAR";
    public static final int MAX_IMAGE_NUM = 9;

    public Config() {
    }

    public static boolean isLogin() {
        return PreferencesUtils.getBoolean(AppManager.getInstance().getTopActivity(), "PREF_KEY_LOGIN_STATE");
    }

    public static void setLoginState(boolean isLogin) {
        PreferencesUtils.putBoolean(AppManager.getInstance().getTopActivity(), "PREF_KEY_LOGIN_STATE", isLogin);
        if(!isLogin) {
//            MobclickAgent.onProfileSignOff();
        }

    }

    public static boolean isUseSnackBar() {
        return PreferencesUtils.getBoolean(AppManager.getInstance().getTopActivity(), "PREF_KEY_USE_SNACKBAR", true);
    }

    public static void setUseSnackBar(boolean isUse) {
        PreferencesUtils.putBoolean(AppManager.getInstance().getTopActivity(), "PREF_KEY_USE_SNACKBAR", isUse);
    }
}

