package com.shuxiangbaima.task.ui.mine.below.applay;

import android.view.View;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Security;
import com.shuxiangbaima.task.ui.Mon.ApplayAty;
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
public class ApplayMangerAty extends BasAty {

    @ViewInject(R.id.applaymanger_tv_01)
    private TextView tv_01;
    @ViewInject(R.id.applaymanger_tv_02)
    private TextView tv_02;

    private Security security;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_applaymanger;
    }

    @Override
    protected void initialized() {
        security = new Security();
    }

    @Override
    protected void requestData() {
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressContent();
        security.get_bind_alipay(this, this);
    }


    @Event(value = {R.id.applaymanger_imgv_back, R.id.applaymanger_relay_applay, R.id.applaymanger_relay_reaccount})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.applaymanger_imgv_back:
                finish();
                break;
            case R.id.applaymanger_relay_applay:
                startActivity(ApplayAty.class, null);
                break;
            case R.id.applaymanger_relay_reaccount:
                startActivity(BindApplayAty.class, null);
                break;

        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        if (var1.getUri().contains("get_bind_alipay")) {
            if (JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
                Map<String, String> map = JSONUtils.parseDataToMap(var2);
                if (map.get("bind").equals("1")) {
                    tv_01.setText(map.get("alipay_account"));
                    application.setUserInfoItem("alipay_account", map.get("alipay_account"));
                    application.setUserInfoItem("alipay_name", map.get("alipay_name"));
                    tv_02.setText("已绑定支付宝账号");
                    PreferencesUtils.putString(this, "isBind", "1");
                } else {
                    tv_01.setText("请绑定支付宝账号");
                    tv_02.setText("未绑定支付宝账号");
                    PreferencesUtils.putString(this, "isBind", "0");
                }

            }
        }
    }
}
