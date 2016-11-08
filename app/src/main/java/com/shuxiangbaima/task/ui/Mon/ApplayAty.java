package com.shuxiangbaima.task.ui.Mon;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Cash;
import com.shuxiangbaima.task.interfaces.Security;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ApplayAty extends BaseAty {

    @ViewInject(R.id.apply_tv_low)
    private TextView tv_low;
    @ViewInject(R.id.apply_tv_balance)
    private TextView tv_balance;
    @ViewInject(R.id.apply_tv_cash)
    private TextView tv_cash;


    private Cash cash;
    private Map<String, String> map;
    private Security security;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_applay;
    }

    @Override
    protected void initialized() {
        cash = new Cash();
        security = new Security();
    }

    @Override
    public void requestData() {
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressContent();
        cash.my_cash(this, this);

    }


    @Event(value = {R.id.applay_imgv_back, R.id.applay_fb, R.id.applay_relay_record})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.applay_imgv_back:
                finish();
                break;
            case R.id.applay_fb:
                if (map != null) {
                    if (map.containsKey("remainder")) {
                        if (map.get("frozen").equals("true")) {
                            showToast("账户冻结，不允许申请提现");
                            return;
                        }
                        if (Float.parseFloat(map.get("remainder")) < Float.parseFloat(map.get("min_cash"))) {
                            showToast("余额小于最低提现金额");
                            return;
                        }
                        if (!TextUtils.isEmpty(PreferencesUtils.getString(this, "isBind"))) {
                            if (PreferencesUtils.getString(this, "isBind").equals("1")) {
                                Bundle bundle = new Bundle();
                                bundle.putFloat("remainder", Float.parseFloat(map.get("remainder")));
                                bundle.putFloat("min_cash", Float.parseFloat(map.get("min_cash")));
                                startActivity(ApplayMonAty.class, bundle);
                            } else {
                                showToast("请先绑定支付宝账号");
                            }
                        } else {
                            showProgressContent();
                            security.get_bind_alipay(this, this);
                        }
                    }
                }

                break;
            case R.id.applay_relay_record:
                startActivity(ApplayRecordAty.class, null);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        if (var1.getUri().contains("my_cash") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            map = JSONUtils.parseDataToMap(var2);
            tv_low.setText(map.get("min_cash") + "元");
            tv_balance.setText(map.get("remainder") + "元");
            tv_cash.setText(map.get("my_cash") + "元");
        }
        if (var1.getUri().contains("get_bind_alipay")) {
            if (JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
                String bind = JSONUtils.parseDataToMap(var2).get("bind");
                if (bind.equals("1")) {
                    PreferencesUtils.putString(this, "isBind", "1");
                    Bundle bundle = new Bundle();
                    bundle.putFloat("remainder", Float.parseFloat(map.get("remainder")));
                    bundle.putFloat("min_cash", Float.parseFloat(map.get("min_cash")));
                    startActivity(ApplayMonAty.class, bundle);
                } else {
                    PreferencesUtils.putString(this, "isBind", "0");
                    showToast("请先绑定支付宝账号");
                }

            }
        }
    }
}
