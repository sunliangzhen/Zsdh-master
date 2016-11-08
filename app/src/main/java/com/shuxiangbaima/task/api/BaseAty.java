package com.shuxiangbaima.task.api;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.User;
import com.shuxiangbaima.task.ui.MainAty;
import com.toocms.dink5.mylibrary.app.AppConstant;
import com.toocms.dink5.mylibrary.app.AppManager;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.baserx.RxBus;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/7.
 */

public abstract class BaseAty extends BasAty {
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User();
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();

        if (var1.getUri().contains("login")) {
            Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
            String status = map.get("status");
            if (map.get("status").equals("200")) {
                Config.setLoginState(true);
                PreferencesUtils.putString(this, "token", JSONUtils.parseDataToMap(var2).get("token"));
                PreferencesUtils.putString(this, "user_id", JSONUtils.parseDataToMap(var2).get("user_id"));
                BasAty topActivity = (BasAty) AppManager.getInstance().getTopActivity();
                topActivity.requestData();
            }
        }
        if (JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("600")) {
            Config.setLoginState(false);
            showBuilder(JSONUtils.parseKeyAndValueToMap(var2).get("msg"));
        }
    }

    public void showBuilder(String content) {
        View view = View.inflate(this, R.layout.dlg_relogin, null);
        TextView tv_content = (TextView) view.findViewById(R.id.buildeexti_tv_content);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        final Dialog dialog = new Dialog(this,R.style.dialog);
        tv_content.setText(content);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                AppManager.getInstance().killtoActivity(MainAty.class);
                RxBus.getInstance().post(AppConstant.LOG_OUT, true);
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                showProgressContent();
                user.login(PreferencesUtils.getString(BaseAty.this, "login_account"), PreferencesUtils.getString(BaseAty.this, "login_password"),
                        "", BaseAty.this, BaseAty.this);

            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }
}
