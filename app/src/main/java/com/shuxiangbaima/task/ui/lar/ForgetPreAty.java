package com.shuxiangbaima.task.ui.lar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Security;
import com.shuxiangbaima.task.interfaces.Verify;
import com.toocms.dink5.mylibrary.base.BaseActivity;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ForgetPreAty extends BaseActivity {

    @ViewInject(R.id.forgetpasspre_etxt_phone)
    private EditText etxt_account;
    @ViewInject(R.id.forgetpasspre_etxt_imgcode)
    private EditText etxt_imgcode;
    @ViewInject(R.id.forgetpasspre_imgv_code)
    private ImageView imgv_code;

    private Verify verify;
    private ImageLoader imageLoader;
    private Security security;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader.displayImage(verify.captcha("forgot_password", this), imgv_code);
    }

    @Override
    public int getLayoutId() {
        return R.layout.aty_forgerpasspre;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        verify = new Verify();
        imageLoader = application.getImageLoader();
        security = new Security();
    }

    @Event(value = {R.id.forgetpasspre_imgv_back, R.id.forgetpasspre_fb_ok, R.id.forgetpasspre_imgv_code})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.forgetpasspre_imgv_back:
                finish();
                break;
            case R.id.forgetpasspre_fb_ok:
                if (TextUtils.isEmpty(etxt_account.getText().toString())) {
                    showShortToast("账号或手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etxt_imgcode.getText().toString())) {
                    showShortToast("验证码不能为空");
                    return;
                }
                showProgressContent();
                security.forgot_password(etxt_account.getText().toString(), etxt_imgcode.getText().toString(), this, this);
                break;
            case R.id.forgetpasspre_imgv_code:
                imageLoader.displayImage(verify.captcha("forgot_password", this), imgv_code);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
//        showToast(map.get("msg"));
        if (var1.getUri().contains("forgot_password")) {
            if (map.get("status").equals("200")) {
                Map<String, String> map1 = JSONUtils.parseDataToMap(var2);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", map1.get("user_id"));
                bundle.putString("mobile", map1.get("mobile"));
                startActivity(ForgetAty.class, bundle);
            }
        }
    }
}
