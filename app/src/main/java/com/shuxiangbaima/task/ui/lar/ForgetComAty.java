package com.shuxiangbaima.task.ui.lar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Security;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ForgetComAty extends BasAty {

    @ViewInject(R.id.forgetpasscom_etxt_pass)
    private EditText etxt_pass;
    @ViewInject(R.id.forgetpasscom_etxt_pass2)
    private EditText etxt_pass2;

    private Security security;
    private String sac;
    private String user_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_forgerpasscom;
    }

    @Override
    protected void initialized() {
        security = new Security();
        sac = getIntent().getStringExtra("sac");
        user_id = getIntent().getStringExtra("user_id");
        Log.e("sac", sac);
    }

    @Override
    protected void requestData() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.forgetpasscom_imgv_back, R.id.forgetpasscom_fb_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.forgetpasscom_imgv_back:
                finish();
                break;
            case R.id.forgetpasscom_fb_ok:
                if (TextUtils.isEmpty(etxt_pass.getText().toString())) {
                    showToast("密码位数不能为空");
                    return;
                }
                if (etxt_pass.getText().toString().length() < 6) {
                    showToast("密码位数不能少于6位");
                    return;
                }
                if (!etxt_pass.getText().toString().equals(etxt_pass2.getText().toString())) {
                    showToast("两次输入密码不一致");
                    return;
                }
                showProgressContent();
                security.set_password(user_id, etxt_pass.getText().toString(), sac, this, this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        showToast(map.get("msg"));
        if (var1.getUri().contains("set_password")) {
            if (map.get("status").equals("200")) {
                finish();
            }
        }
    }
}
