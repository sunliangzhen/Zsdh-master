package com.shuxiangbaima.task.ui.mine.mynews;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.interfaces.Profile;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;
import com.toocms.dink5.mylibrary.flowlayout.SingleFlowLayout;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/15.
 */
public class Morenews extends BasAty implements LoadingTip.onReloadListener {

    @ViewInject(R.id.flow_layout)
    private SingleFlowLayout flow_layout;
    @ViewInject(R.id.flow_layout2)
    private SingleFlowLayout flow_layout2;
    @ViewInject(R.id.flow_layout3)
    private SingleFlowLayout flow_layout3;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
    @ViewInject(R.id.scrollview)
    private ScrollView scrollview;

    private ArrayList<Map<String, String>> age_range_list;
    private ArrayList<Map<String, String>> title_list;
    private ArrayList<Map<String, String>> hobby_list;
    private Profile profile;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_morenews;
    }

    @Override
    protected void initialized() {
        profile = new Profile();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        profile.options(this, this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadedTip.setOnReloadListener(this);
    }

    @Override
    public void initPresenter() {
    }


    @Event(value = {R.id.morenews_imgv_back, R.id.morenews_imgv_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.morenews_imgv_back:
                finish();
                break;
            case R.id.morenews_imgv_ok:
                if (TextUtils.isEmpty(flow_layout.getCheckedString())) {
                    showToast("请选择年龄");
                    return;
                }
                if (TextUtils.isEmpty(flow_layout2.getCheckedString())) {
                    showToast("请选择职业");
                    return;
                }
                if (TextUtils.isEmpty(flow_layout3.getCheckedString())) {
                    showToast("请选择爱好");
                    return;
                }
                showProgressContent();
                profile.modify_profile(flow_layout.getCheckedString(), flow_layout2.getCheckedString(), flow_layout3.getCheckedString(), this, this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        scrollview.setVisibility(View.VISIBLE);
        if (var1.getUri().contains("modify_profile")) {
            application.setUserInfoItem("age_range", flow_layout.getCheckedString());
            application.setUserInfoItem("title", flow_layout2.getCheckedString());
            application.setUserInfoItem("hobbies", flow_layout3.getCheckedString());
            finish();
        }
        if (var1.getUri().contains("options")) {
            Map<String, String> map;
            if (JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
                map = JSONUtils.parseDataToMap(var2);
                PreferencesUtils.putString(this, "v", map.get("version"));
                PreferencesUtils.putString(this, "news_v", var2);
            } else {
                String news = PreferencesUtils.getString(this, "news_v");
                LogUtil.e(news);
                map = JSONUtils.parseDataToMap(news);
            }
            age_range_list = JSONUtils.parseKeyAndValueToMapList(map.get("age_range_list"));
            title_list = JSONUtils.parseKeyAndValueToMapList(map.get("title_list"));
            hobby_list = JSONUtils.parseKeyAndValueToMapList(map.get("hobby_list"));

            flow_layout.setData(age_range_list, 0);
            flow_layout.setCheckeItem(application.getUserInfo().get("age_range"));

            flow_layout2.setData(title_list, 0);
            flow_layout2.setCheckeItem(application.getUserInfo().get("title"));

            flow_layout3.setData(hobby_list, 1);
            flow_layout3.setCheckeItem(application.getUserInfo().get("hobbies").toString());
        }
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        scrollview.setVisibility(View.GONE);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
    }

    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        profile.options(this, this);
    }
}
