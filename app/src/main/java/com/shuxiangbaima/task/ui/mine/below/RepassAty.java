package com.shuxiangbaima.task.ui.mine.below;

import android.text.TextUtils;
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
 * Created by Administrator on 2016/8/11.
 */
public class RepassAty extends BasAty {

    @ViewInject(R.id.repass_etxt_pass)
    private EditText etxt_pass;
    @ViewInject(R.id.repass_etxt_pass2)
    private EditText etxt_pass2;
    @ViewInject(R.id.repass_etxt_pass3)
    private EditText etxt_pass3;

    private Security security;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_repass;
    }

    @Override
    protected void initialized() {
        security = new Security();
    }

    @Override
    protected void requestData() {
    }


    @Event(value = {R.id.repass_imgv_back, R.id.repass_tv_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.repass_imgv_back:
                finish();
                break;
            case R.id.repass_tv_ok:
                if (TextUtils.isEmpty(etxt_pass.getText().toString())) {
                    showToast("原密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etxt_pass2.getText().toString())) {
                    showToast("新密码不能为空");
                    return;
                }
                if (etxt_pass2.getText().toString().length() < 6) {
                    showToast("新密码位数不能少于6位");
                    return;
                }
                if (!etxt_pass2.getText().toString().equals(etxt_pass3.getText().toString())) {
                    showToast("两次密码输入不一致");
                    return;
                }
                showProgressContent();
                security.modify_password(etxt_pass.getText().toString(), etxt_pass2.getText().toString(), this, this);
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
        if (var1.getUri().contains("modify_password")) {
            if (map.get("status").equals("200")) {
                finish();
            }
        }
    }
}
