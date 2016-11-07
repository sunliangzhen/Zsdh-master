package com.shuxiangbaima.task.ui.notice;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuxiangbaima.task.R;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;
import com.toocms.dink5.mylibrary.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/9/22.
 */

public class NoticeDetailsAty extends BaseActivity {

    @ViewInject(R.id.noticede_web)
    private WebView webView;
    @ViewInject(R.id.noticede_tv_title)
    private TextView tv_title;
    @ViewInject(R.id.myProgressBar)
    private ProgressBar myProgressBar;

    private String type;
    private String link;
    private String notice_id;


    @Override
    public int getLayoutId() {
        return R.layout.aty_notice_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        type = getIntent().getStringExtra("type");
        link = getIntent().getStringExtra("link");
        notice_id = getIntent().getStringExtra("notice_id");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesUtils.putInt(this, notice_id, 1);
        switch (type) {
            case "1":
                tv_title.setText("公告");
                break;
            case "2":
                tv_title.setText("通知");
                break;
            case "3":
                tv_title.setText("回复");
                break;
        }
        webView.loadUrl(link);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                myProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    myProgressBar.setVisibility(View.GONE);
                }
            }

        });
    }


    @Event(value = {R.id.noticede_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.noticede_imgv_back:
                finish();
                break;
        }
    }
}
