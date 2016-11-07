package com.shuxiangbaima.task.ui.mine.below;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Suggestion;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class IdeaAty extends BasAty {

    @ViewInject(R.id.idea_etxt_content)
    private EditText etxt_content;
    @ViewInject(R.id.idea_etxt_phone)
    private EditText etxt_phone;

    private Suggestion suggestion;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_idea;
    }

    @Override
    protected void initialized() {
        suggestion = new Suggestion();
    }

    @Override
    protected void requestData() {
    }


    @Event(value = {R.id.idea_imgv_back, R.id.idea_btn_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.idea_imgv_back:
                finish();
                break;
            case R.id.idea_btn_ok:
                if (TextUtils.isEmpty(etxt_phone.getText().toString())) {
                    showToast("联系方式不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etxt_content.getText().toString())) {
                    showToast("描述内容不能为空");
                    return;
                }
                showProgressContent();
                suggestion.submit(etxt_phone.getText().toString(), etxt_content.getText().toString(), this, this);
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
        if (var1.getUri().contains("submit")) {
            if (map.get("status").equals("200")) {
                finish();
            }
        }
    }
}
