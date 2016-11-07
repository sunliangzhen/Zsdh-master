package com.toocms.dink5.mylibrary.app;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 *
 */
public class AppCountdown extends CountDownTimer {

    private Button textView;
    private static long surplusTime;
    private static AppCountdown appCountdown;

    public static AppCountdown getInstance() {
        if (appCountdown == null) {
            appCountdown = new AppCountdown();
        }
        return appCountdown;
    }

    private AppCountdown() {
        super(surplusTime > 0 ? surplusTime : 60000, 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        surplusTime = millisUntilFinished;
        textView.setText(millisUntilFinished / 1000 + "s后重新发送");
    }

    @Override
    public void onFinish() {
        surplusTime = 0;
        textView.setText("重新获取");
        textView.setEnabled(true);
        cancel();
    }

    public void play(Button textView) {
        this.textView = textView;
        if (surplusTime > 0) {
            textView.setEnabled(false);
        } else {
            textView.setText("获取验证码");
            textView.setEnabled(true);
        }
    }

    public void reSet() {
        surplusTime = 0;
        textView.setEnabled(true);
        textView.setText("获取验证码");
        cancel();
    }

    public void mStart() {
        textView.setEnabled(false);
        start();
    }
}