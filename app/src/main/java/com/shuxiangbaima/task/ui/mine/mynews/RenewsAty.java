package com.shuxiangbaima.task.ui.mine.mynews;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Profile;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/10.
 */
public class RenewsAty extends BaseAty implements RadioGroup.OnCheckedChangeListener {

    @ViewInject(R.id.renews_rg)
    private RadioGroup renews_rg;
    @ViewInject(R.id.renews_rbtn_nan)
    private RadioButton rbtn_nan;
    @ViewInject(R.id.renews_rbtn_nv)
    private RadioButton rbtn_nv;
    @ViewInject(R.id.renews_tv_title)
    private TextView tv_title;
    @ViewInject(R.id.renews_etxt_name)
    private EditText etxt_name;

    private String type;
    private Profile profile;
    private String gender;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_renews;
    }

    @Override
    public void requestData() {
    }

    @Override
    protected void initialized() {
        type = getIntent().getStringExtra("type");
        profile = new Profile();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renews_rg.setOnCheckedChangeListener(this);
        if (type.equals("rename")) {
            tv_title.setText("更改昵称");
            etxt_name.setVisibility(View.VISIBLE);
            renews_rg.setVisibility(View.GONE);
        } else if (type.equals("resex")) {
            tv_title.setText("更改性别");
            etxt_name.setVisibility(View.GONE);
            renews_rg.setVisibility(View.VISIBLE);
        }
        if (PreferencesUtils.getString(this, "gender").equals("0")) {
            rbtn_nan.setChecked(false);
            rbtn_nv.setChecked(false);
            gender = "";
        } else if (PreferencesUtils.getString(this, "gender").equals("1")) {
            gender = "1";
            rbtn_nan.setChecked(true);
        } else {
            gender = "2";
            rbtn_nv.setChecked(true);
        }
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.renews_imgv, R.id.renews_tv_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.renews_imgv:
                finish();
                break;
            case R.id.renews_tv_ok:
                if (type.equals("rename")) {
                    if (TextUtils.isEmpty(etxt_name.getText().toString())) {
                        showToast("昵称不能为空");
                        return;
                    }
                    showProgressContent();
                    profile.modify_nickname(etxt_name.getText().toString().trim(), this, this);
                } else {
                    if (TextUtils.isEmpty(gender)) {
                        showToast("请选择性别");
                        return;
                    }
                    showProgressContent();
                    profile.modify_gender(gender, this, this);
                }
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(var2);
        if (var1.getUri().contains("modify_nickname")) {
            if (map.get("status").equals("200")) {
                application.setUserInfoItem("nickname", etxt_name.getText().toString());
                finish();
            }
        }
        if (var1.getUri().contains("modify_gender")) {
            if (map.get("status").equals("200")) {
                application.setUserInfoItem("gender", gender);
                finish();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.renews_rbtn_nan:
                gender = "1";
                break;
            case R.id.renews_rbtn_nv:
                gender = "2";
                break;
        }
    }
}
