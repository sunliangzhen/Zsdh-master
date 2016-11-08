package com.shuxiangbaima.task.ui.mine.below;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.shuxiangbaima.task.R;
import com.toocms.dink5.mylibrary.base.BasAty;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/8/11.
 */
public class AboutAty extends BasAty {

    @ViewInject(R.id.about_web)
    private WebView about_web;
    @ViewInject(R.id.myProgressBar)
    private ProgressBar progressBar;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_about;
    }

    @Override
    protected void initialized() {
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void requestData() {
    }


    @Event(value = {R.id.bus_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bus_imgv_back:
                finish();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        about_web.loadUrl("http://139.224.17.188/business/aboutus");
        about_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    about_web.setVisibility(View.VISIBLE);
                }
            }

        });
    }

}
