package com.shuxiangbaima.task.ui.mine.below.applay;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Security;
import com.shuxiangbaima.task.interfaces.Verify;
import com.toocms.dink5.mylibrary.app.AppCountdown;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/23.
 */
public class BindApplayAty extends BasAty {

    @ViewInject(R.id.bindapplay_tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.bindapplay_etxt_code)
    private EditText etxt_code;
    @ViewInject(R.id.bindapplay_tv_send)
    private Button btn_send;
    @ViewInject(R.id.bindapplay_linlay_imgcode)
    private LinearLayout linlay_imgcode;
    @ViewInject(R.id.bindapplay_etxt_imgcode)
    private EditText etxt_imgcode;
    @ViewInject(R.id.bindapplay_imgv_code)
    private ImageView imgv_code;

    private AppCountdown appCountdown;
    private Verify verify;
    private Security security;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bindapplay;
    }


    @Override
    protected void initialized() {
        verify = new Verify();
        security = new Security();
        imageLoader = application.getImageLoader();
    }
    @Override
    protected void requestData() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCountdown = AppCountdown.getInstance();
        appCountdown.play(btn_send);
        tv_phone.setText(PreferencesUtils.getString(this, "mobile"));
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.bindapplay_imgv_back, R.id.bindapplay_tv_send, R.id.bindapplay_fb_ok, R.id.bindapplay_imgv_code})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bindapplay_imgv_back:
                finish();
                break;
            case R.id.bindapplay_tv_send:
                if (linlay_imgcode.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(etxt_imgcode.getText().toString())) {
                        showToast("图片验证码不能为空");
                        return;
                    }
                }
                showProgressContent();
                verify.send_vcode(PreferencesUtils.getString(this, "mobile"), "bind_alipay", this, etxt_imgcode.getText().toString(), this);
                break;
            case R.id.bindapplay_fb_ok:
                if (TextUtils.isEmpty(etxt_code.getText().toString())) {
                    showToast("验证码不能为空");
                    return;
                }
                showProgressContent();
                security.cert_by_mobile(PreferencesUtils.getString(this, "user_id"), PreferencesUtils.getString(this, "mobile"),
                        etxt_code.getText().toString(), "bind_alipay", this, this);
                break;
            case R.id.bindapplay_imgv_code:
                imageLoader.displayImage(verify.captcha("send_vcode", this), imgv_code);
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
                linlay_imgcode.setVisibility(View.GONE);
            }
            if (map.get("status").equals("1000")) {
                imageLoader.displayImage(verify.captcha("send_vcode", this), imgv_code);
                linlay_imgcode.setVisibility(View.VISIBLE);
            }
            if (map.get("status").equals("10012") || map.get("status").equals("10011") || map.get("status").equals("1001")) {
                linlay_imgcode.setVisibility(View.VISIBLE);
                if (linlay_imgcode.getVisibility() == View.VISIBLE) {
                    imageLoader.displayImage(verify.captcha("send_vcode", this), imgv_code);
                }
            }
        }
        if (var1.getUri().contains("cert_by_mobile")) {
            if (map.get("status").equals("200")) {
                Bundle bundle = new Bundle();
                bundle.putString("sac", JSONUtils.parseDataToMap(var2).get("sac"));
                startActivity(BindApplaycomAty.class, bundle);
                finish();
            }
        }
    }
}
