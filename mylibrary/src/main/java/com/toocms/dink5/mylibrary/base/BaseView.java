package com.toocms.dink5.mylibrary.base;

/**
 * des:baseview
 * Created by xsf
 * on 2016.07.11:53
 */
public interface BaseView {
    /*******
     * 内嵌加载
     *******/
    void showLoading(String title);

    int getDataCount();

    void stopLoading();

    void showErrorTip(String msg);
}
