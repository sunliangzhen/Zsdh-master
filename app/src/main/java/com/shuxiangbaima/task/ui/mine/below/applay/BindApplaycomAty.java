package com.shuxiangbaima.task.ui.mine.below.applay;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Security;
import com.toocms.dink5.mylibrary.app.AppCountdown;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/23.
 */
public class BindApplaycomAty extends BaseAty {

    @ViewInject(R.id.bingapplaycom_etxt_pass)
    private EditText etxt_account;
    @ViewInject(R.id.bingapplaycom_etxt_pass2)
    private EditText etxt_name;

    private String sac;
    private Security security;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bindapplaycom;
    }

    @Override
    protected void initialized() {
        etxt_account.setText(application.getUserInfo().get("alipay_account"));
        etxt_name.setText(application.getUserInfo().get("alipay_name"));
        sac = getIntent().getStringExtra("sac");
        security = new Security();
    }

    @Override
    public void requestData() {
    }

    @Event(value = {R.id.bingapplaycom_imgv_back, R.id.bingapplaycom_tv_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bingapplaycom_imgv_back:
                finish();
                break;
            case R.id.bingapplaycom_tv_ok:
                if (TextUtils.isEmpty(etxt_account.getText().toString())) {
                    showToast("支付宝账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etxt_name.getText().toString())) {
                    showToast("支付宝姓名不能为空");
                    return;
                }
                showProgressContent();
                security.bind_alipay(sac, etxt_account.getText().toString(), etxt_name.getText().toString(), this, this);
                break;

        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        showToast(map.get("msg"));
        if (var1.getUri().contains("bind_alipay")) {
            if (map.get("status").equals("200")) {
                AppCountdown.getInstance().reSet();
                finish();
            }
        }
    }
}
