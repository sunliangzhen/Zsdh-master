package com.shuxiangbaima.task.ui.page;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.shuxiangbaima.task.interfaces.Cash;
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
public class Cashervice extends Service {

    private Alarmreceiver alarmreceiver;
    private Cash cash;
    private CashListener cashListener;
    private boolean isEnd = true;
    private boolean isFirst = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public Cashervice getService() {
            return Cashervice.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cash = new Cash();
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


    public void regisetCashListener(CashListener cashListener) {
        this.cashListener = cashListener;
    }

    public interface CashListener {
        void cash(String data, String total_profit,boolean isFromeStart);

    }

    private void registerBroadCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("recent_cash ");
        alarmreceiver = new Alarmreceiver();
        registerReceiver(alarmreceiver, filter);
    }

    private void ada() {
        Intent intent = new Intent();
        intent.setAction("recent_cash ");
        PendingIntent sender = PendingIntent.getBroadcast(Cashervice.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) Cashervice.this.getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 3, sender);
    }

    private class Alarmreceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (System.currentTimeMillis() > PreferencesUtils.getLong(Cashervice.this, "expire_cash", 0)) {
                if (isEnd) {
                    isEnd = false;
                    cash.recent_cash(Cashervice.this, new ApiListener() {
                        @Override
                        public void onCancelled(Callback.CancelledException e) {
                            isEnd = true;
                        }

                        @Override
                        public void onComplete(RequestParams requestParams, String s) {
                            if (JSONUtils.parseKeyAndValueToMap(s).get("status").equals("200")) {
//                                Map<String, String> map = JSONUtils.parseDataToMap(s);
//                                PreferencesUtils.putString(Cashervice.this, "v", map.get("version"));
//                                PreferencesUtils.putString(Cashervice.this, "news", s);

                                Map<String, String> map1 = JSONUtils.parseDataToMap(s);
                                PreferencesUtils.putLong(Cashervice.this, "expire_cash", Long.parseLong(map1.get("expire")) * 1000L);
                                PreferencesUtils.putString(Cashervice.this, "cash_list", map1.get("cash_list"));
                                PreferencesUtils.putString(Cashervice.this, "total_profit", map1.get("total_profit"));
                                if (cashListener != null) {
                                    cashListener.cash(map1.get("cash_list"), map1.get("total_profit"),true);
                                }
                            }
                            isEnd = true;
                        }

                        @Override
                        public void onError(Map<String, String> var1, RequestParams var2) {
                            isEnd = true;
                        }

                        @Override
                        public void onException(Throwable var1, RequestParams params) {

                        }
                    });
                }
            }
//            else {
//                if (isFirst) {
//                    isFirst = false;
//                    cashListener.cash(PreferencesUtils.getString(Cashervice.this, "cash_list"), false);
//                }
//            }

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alarmreceiver != null) {
            unregisterReceiver(alarmreceiver);
        }
    }
}


