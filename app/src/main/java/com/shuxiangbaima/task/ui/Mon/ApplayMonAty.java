package com.shuxiangbaima.task.ui.Mon;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.interfaces.Cash;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ApplayMonAty extends BaseAty {

    @ViewInject(R.id.applaymon_etxt_mon)
    private EditText etxt_mon;

    private float remainder;
    private float min_cash;
    private float applay;
    private Cash cash;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_applay_mon;
    }

    @Override
    protected void initialized() {
        cash = new Cash();
        remainder = getIntent().getFloatExtra("remainder", 0);
        min_cash = getIntent().getFloatExtra("min_cash", 0);
    }

    @Override
    public void requestData() {
    }

    @Event(value = {R.id.applaymon_imgv_back, R.id.applaymon_fb})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.applaymon_imgv_back:
                finish();
                break;
            case R.id.applaymon_fb:
                if (TextUtils.isEmpty(etxt_mon.getText().toString())) {
                    showToast("提现金额不能为空");
                    return;
                }
                applay = Float.parseFloat(etxt_mon.getText().toString());
                if (applay > remainder) {
                    showToast("提现金额不能大于余额");
                    return;
                }
                if (applay < min_cash) {
                    showToast("提现金额不能小于最低提现金额");
                    return;
                }
                showProgressContent();
                cash.apply_cash(applay, this, this);
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
        if (var1.getUri().contains("apply_cash") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            showToast("等待审核");
            finish();
        }
    }
}
