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
public class BusAty extends BasAty {

    @ViewInject(R.id.bus_web)
    private WebView bus_web;
    @ViewInject(R.id.myProgressBar)
    private ProgressBar progressBar;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bus;
    }

    @Override
    protected void initialized() {
    }

    @Override
    public void requestData() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus_web.loadUrl("http://139.224.17.188/business/cooperation");
        bus_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    bus_web.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    @Override
    public void initPresenter() {

    }

    @Event(value = {R.id.bus_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bus_imgv_back:
                finish();
                break;
        }
    }

}
