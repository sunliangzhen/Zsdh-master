package com.shuxiangbaima.task.ui.page;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.shuxiangbaima.task.R;
import com.toocms.dink5.mylibrary.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/9/22.
 */

public class AdDetailsAty extends BaseActivity {

    @ViewInject(R.id.ad_web)
    private WebView ad_web;
    @ViewInject(R.id.myProgressBar)
    private ProgressBar progressBar;

    private String link;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ad_web.loadUrl(link);

        ad_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.aty_ad_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        link = getIntent().getStringExtra("link");
    }

    @Event(value = {R.id.ad_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.ad_imgv_back:
                finish();
                break;
        }
    }

}
