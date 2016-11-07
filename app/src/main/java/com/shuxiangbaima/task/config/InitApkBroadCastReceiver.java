package com.shuxiangbaima.task.config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.xutils.common.util.LogUtil;

/**
 * Created by Administrator on 2016/9/30.
 */

public class InitApkBroadCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            System.out.println("监听到系统广播添加");
            String dataString = intent.getDataString();
            LogUtil.e("dataString" + dataString);

        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            System.out.println("监听到系统广播移除");
            String dataString = intent.getDataString();
            LogUtil.e("dataString" + dataString);

        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            System.out.println("监听到系统广播替换");
            String dataString = intent.getDataString();
            LogUtil.e("dataString" + dataString);
        }
    }

}
