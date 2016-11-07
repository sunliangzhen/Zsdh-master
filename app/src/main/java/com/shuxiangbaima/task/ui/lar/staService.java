package com.shuxiangbaima.task.ui.lar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.shuxiangbaima.task.interfaces.Profile;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.net.ApiListener;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.Map;


/**
 * @author Zero
 * @date 2016/6/8 17:33
 */
public class staService extends Service {

    private Alarmreceiver alarmreceiver;
    private Profile profile;
    private String news;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        profile = new Profile();
        registerBroadCast();
        ada();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alarmreceiver != null) {
            unregisterReceiver(alarmreceiver);
        }
    }

    private void registerBroadCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("sun2");
        alarmreceiver = new Alarmreceiver();
        registerReceiver(alarmreceiver, filter);
    }

    private void ada() {
        Intent intent = new Intent();
        intent.setAction("sun2");
        PendingIntent sender = PendingIntent.getBroadcast(staService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) staService.this.getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.cancel(sender);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 30, sender);
    }

    private class Alarmreceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            profile.options(staService.this, new ApiListener() {
                @Override
                public void onCancelled(Callback.CancelledException e) {
                }

                @Override
                public void onComplete(RequestParams requestParams, String s) {
                    news = s;
                    if (JSONUtils.parseKeyAndValueToMap(s).get("status").equals("200")) {
                        Map<String, String> map = JSONUtils.parseDataToMap(s);
                        PreferencesUtils.putString(staService.this, "v", map.get("version"));
                        PreferencesUtils.putString(staService.this, "news", s);
                    }
                }

                @Override
                public void onError(Map<String, String> var1, RequestParams var2) {

                }

                @Override
                public void onException(Throwable var1, RequestParams params) {

                }
            });
        }
    }
}


