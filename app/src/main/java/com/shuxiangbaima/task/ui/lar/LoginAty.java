package com.shuxiangbaima.task.ui.lar;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.User;
import com.shuxiangbaima.task.interfaces.Verify;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class LoginAty extends BasAty {

    @ViewInject(R.id.login_etxt_username)
    private EditText etxt_username;
    @ViewInject(R.id.login_etxt_pass)
    private EditText etxt_pass;
    @ViewInject(R.id.login_linlay_imgcode)
    private LinearLayout linlay_imgcode;
    @ViewInject(R.id.login_etxt_imgcode)
    private EditText etxt_imcode;
    @ViewInject(R.id.login_imgv_code)
    private ImageView imgv_code;

    private User user;
    private Verify verify;
    private int type;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_login;
    }

    @Override
    protected void initialized() {
        user = new User();
        verify = new Verify();
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
        }
        imageLoader = application.getImageLoader();
    }

    @Override
    public void requestData() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SpannableString ss = new SpannableString("请输入手机号");
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(AutoUtils.getPercentWidthSizeBigger(60), false);
//        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textin_name.setHint(new SpannedString(ss));
    }

    @Override
    public void initPresenter() {
    }

    @Event(value = {R.id.login_tv_ok, R.id.login_tv_register, R.id.login_tv_forget, R.id.login_imgv_code, R.id.login_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv_ok:

                if (TextUtils.isEmpty(etxt_username.getText().toString())) {
                    showToast("请先输入手机号");
                    return;
                }
                if (etxt_username.getText().toString().length() < 11) {
                    showToast("请核对手机号位数");
                    return;
                }
                if (TextUtils.isEmpty(etxt_pass.getText().toString())) {
                    showToast("密码不能为空");
                    return;
                }
                showProgressContent();
//                byte[] utf8s = etxt_pass.getText().toString().getBytes("UTF-8");
//                String pass = null;
//                try {
//                    pass = RSAUtils.encryptByPublicKey(utf8s, AppConfig.PUBLIC_KEY);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                user.login(etxt_username.getText().toString(), etxt_pass.getText().toString(), etxt_imcode.getText().toString(), this, this);
                break;
            case R.id.login_tv_register:
                startActivity(RegisterAty.class, null);
                break;
            case R.id.login_tv_forget:
                startActivity(ForgetPreAty.class, null);
                break;
            case R.id.login_imgv_code:
                imageLoader.displayImage(getImgCode(), imgv_code);
                break;
            case R.id.login_imgv_back:
                setResult(1);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        if (var1.getUri().contains("login")) {
            Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
            String status = map.get("status");
            if (!status.equals("200")) {
                showToast(map.get("msg"));
            }
            if (status.equals("1000")) {
                imageLoader.displayImage(getImgCode(), imgv_code);
                linlay_imgcode.setVisibility(View.VISIBLE);
            }
            if (map.get("status").equals("10012") || map.get("status").equals("10011") || map.get("status").equals("1001")) {
                if (linlay_imgcode.getVisibility() == View.VISIBLE) {
                    imageLoader.displayImage(getImgCode(), imgv_code);
                }
            }
            if (map.get("status").equals("200")) {
                Config.setLoginState(true);
                PreferencesUtils.putString(this, "login_account", etxt_username.getText().toString());
                PreferencesUtils.putString(this, "login_password", etxt_pass.getText().toString());
                PreferencesUtils.putString(this, "token", JSONUtils.parseDataToMap(var2).get("token"));
                PreferencesUtils.putString(this, "user_id", JSONUtils.parseDataToMap(var2).get("user_id"));
                switch (type) {
                    case AppConfig.FRG_MON:
                        setResult(-1);
                        finish();
                        break;
                    case AppConfig.FRG_MINE:
                        setResult(-1);
                        finish();
                        break;
                    case AppConfig.ATY_SHARE:
                        setResult(-1);
                        finish();
                        break;
                    case AppConfig.ATY_SUBMIT:
                        setResult(-1);
                        finish();
                        break;
                    case AppConfig.FRG_TASK:
                        setResult(-1);
                        finish();
                        break;
                    default:
                        setResult(1);
                        finish();
                }
            }
        }
    }

    public String getImgCode() {
        return verify.captcha("login", LoginAty.this);
    }
}
