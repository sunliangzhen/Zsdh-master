package com.shuxiangbaima.task.ui.lar;

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
import com.toocms.dink5.mylibrary.app.AppManager;
import com.toocms.dink5.mylibrary.base.BaseActivity;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ForgetAty extends BaseActivity {

    @ViewInject(R.id.forgetpass_tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.forgetpass_linlay_imgcode)
    private LinearLayout linlay_imgcode;
    @ViewInject(R.id.forgetpass_etxt_imgcode)
    private EditText etxt_imgcode;
    @ViewInject(R.id.forgetpass_imgv_code)
    private ImageView imgv_imgcodee;
    @ViewInject(R.id.forgetpass_tv_send)
    private Button tv_send;
    @ViewInject(R.id.forgetpass_etxt_code)
    private EditText etxt_code;

    private AppCountdown appCountdown;
    private Verify verify;
    private String mobile;
    private String user_id;
    private ImageLoader imageLoader;
    private Security security;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_phone.setText(mobile);
        appCountdown = AppCountdown.getInstance();
        appCountdown.play(tv_send);
    }

    @Override
    public int getLayoutId() {
        return R.layout.aty_forgerpass;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        verify = new Verify();
        security = new Security();
        mobile = getIntent().getStringExtra("mobile");
        user_id = getIntent().getStringExtra("user_id");
        imageLoader = application.getImageLoader();
        verify = new Verify();
    }

    @Event(value = {R.id.forger_imgv_back, R.id.forgetpass_tv_send, R.id.forgetpass_fb_ok, R.id.forgetpass_imgv_code})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.forger_imgv_back:
                finish();
                break;
            case R.id.forgetpass_tv_send:
                if (linlay_imgcode.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(etxt_imgcode.getText().toString())) {
                        showShortToast("图片验证码不能为空");
                        return;
                    }
                }
                showProgressContent();
                verify.send_vcode(mobile, "forgot_password", this, etxt_imgcode.getText().toString(), this);
                break;
            case R.id.forgetpass_fb_ok:
                if (TextUtils.isEmpty(etxt_code.getText().toString())) {
                    showShortToast("验证码不能为空");
                    return;
                }
                showProgressContent();
                security.cert_by_mobile(user_id, mobile, etxt_code.getText().toString(), "forgot_password", this, this);
                break;
            case R.id.forgetpass_imgv_code:
                imageLoader.displayImage(verify.captcha("send_vcode", this), imgv_imgcodee);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
//        showToast(map.get("msg"));
        if (var1.getUri().contains("send_vcode")) {
            if (map.get("status").equals("200")) {
                appCountdown.mStart();
                linlay_imgcode.setVisibility(View.GONE);
            }
            if (map.get("status").equals("1000")) {
                imageLoader.displayImage(verify.captcha("send_vcode", this), imgv_imgcodee);
                linlay_imgcode.setVisibility(View.VISIBLE);
            }
            if (map.get("status").equals("10012") || map.get("status").equals("10011") || map.get("status").equals("1001")) {
                linlay_imgcode.setVisibility(View.VISIBLE);
                if (linlay_imgcode.getVisibility() == View.VISIBLE) {
                    imageLoader.displayImage(verify.captcha("send_vcode", this), imgv_imgcodee);
                }
            }
        }

        if (var1.getUri().contains("cert_by_mobile")) {
            if (map.get("status").equals("200")) {
                Bundle bundle = new Bundle();
                bundle.putString("sac", JSONUtils.parseDataToMap(var2).get("sac"));
                bundle.putString("user_id", user_id);
                startActivity(ForgetComAty.class, bundle);
                AppManager.getInstance().killActivity(ForgetPreAty.class);
                finish();
            }
        }
    }
}
