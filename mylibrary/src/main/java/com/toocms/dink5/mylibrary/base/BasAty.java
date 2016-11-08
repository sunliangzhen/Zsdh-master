package com.toocms.dink5.mylibrary.base;

import android.os.Bundle;

import com.toocms.dink5.mylibrary.commonwidget.LoadingDialog;

/**
 * Created by slz on 2016/7/29.
 */

public abstract class BasAty extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    public void initView() {
        initialized();
    }

    @Override
    public int getLayoutId() {
        return getLayoutResId();
    }

    public abstract void requestData();

    protected abstract void initialized();

    protected abstract int getLayoutResId();

    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(this);
    }

    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(this, msg, true);
    }

    public void showToast(String text) {
        showShortToast(text);
    }
}
