package com.shuxiangbaima.task.ui.lar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.User;
import com.shuxiangbaima.task.interfaces.Verify;
import com.toocms.dink5.mylibrary.app.AppCountdown;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class RegisterAty extends BasAty {

    @ViewInject(R.id.register_etxt_phone)
    private EditText etxt_phone;
    @ViewInject(R.id.register_etxt_pass)
    private EditText etxt_pass;
    @ViewInject(R.id.register_etxt_code)
    private EditText etxt_code;
    @ViewInject(R.id.register_etxt_yqm)
    private EditText etxt_yqm;
    @ViewInject(R.id.register_tv_send)
    private Button tv_send;
    @ViewInject(R.id.register_tv_isregister)
    private TextView tv_isregister;
    @ViewInject(R.id.register_linlay_imgcode)
    private LinearLayout linlay_imgcode;
    @ViewInject(R.id.register_etxt_imgcode)
    private EditText etxt_imgcode;
    @ViewInject(R.id.register_imgv_code)
    private ImageView imgv_code;

    private Verify verify;
    private User user;
    private AppCountdown appCountdown;
    private ImageLoader imageLoader;
    private String available = "0";

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_register;
    }

    @Override
    protected void initialized() {
        verify = new Verify();
        imageLoader = application.getImageLoader();
        user = new User();
    }

    @Override
    public void requestData() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCountdown = AppCountdown.getInstance();
        appCountdown.play(tv_send);
    }

    @Override
    public void initPresenter() {
    }

    @Event(value = {R.id.register_imgv_back, R.id.register_tv_send, R.id.register_tv_ok, R.id.register_imgv_code})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.register_imgv_back:
                finish();
                break;
            case R.id.register_tv_send:
                if (TextUtils.isEmpty(etxt_phone.getText().toString())) {
                    showToast("请先输入手机号");
                    return;
                }
                if (etxt_phone.getText().toString().length() < 11) {
                    showToast("请核对手机号位数");
                    return;
                }
                if (linlay_imgcode.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(etxt_imgcode.getText().toString())) {
                        showToast("图片验证码不能为空");
                        return;
                    }
                }
                showProgressContent();
                verify.send_vcode(etxt_phone.getText().toString(), "register", this, etxt_imgcode.getText().toString(), this);
                break;
            case R.id.register_tv_ok:
                if (TextUtils.isEmpty(etxt_phone.getText().toString())) {
                    showToast("请先输入手机号");
                    return;
                }
                if (etxt_phone.getText().toString().length() < 11) {
                    showToast("请核对手机号位数");
                    return;
                }
                if (TextUtils.isEmpty(etxt_pass.getText().toString())) {
                    showToast("密码不能为空");
                    return;
                }
                if (etxt_pass.getText().toString().length() < 6) {
                    showToast("请核对密码位数");
                    return;
                }
                if (TextUtils.isEmpty(etxt_code.getText().toString())) {
                    showToast("验证码不能为空");
                    return;
                }
                showProgressContent();
                user.register(etxt_phone.getText().toString(), etxt_pass.getText().toString(), etxt_code.getText().toString(),
                        etxt_yqm.getText().toString(), this, this);
                break;
            case R.id.register_imgv_code:
                imageLoader.displayImage(getImgCode(), imgv_code);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        showToast(map.get("msg"));
        if (var1.getUri().contains("send_vcode")) {
            if (map.get("status").equals("200")) {
                appCountdown.mStart();
//                tv_send.setEnabled(false);
                linlay_imgcode.setVisibility(View.GONE);
            }
            if (map.get("status").equals("10010")) {
                tv_isregister.setVisibility(View.VISIBLE);
            }
            if (map.get("status").equals("1000")) {
                imageLoader.displayImage(getImgCode(), imgv_code);
                linlay_imgcode.setVisibility(View.VISIBLE);
            }
            if (map.get("status").equals("10012") || map.get("status").equals("10011") || map.get("status").equals("1001")) {
                if (linlay_imgcode.getVisibility() == View.VISIBLE) {
                    imageLoader.displayImage(getImgCode(), imgv_code);
                }
            }
        }
        if (var1.getUri().contains("register")) {
            if (map.get("status").equals("200")) {
                finish();
                appCountdown.reSet();
            }
        }
    }

    public String getImgCode() {
        return verify.captcha("send_vcode", this);
    }
}
