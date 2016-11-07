package com.shuxiangbaima.task.ui.main.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/24.
 */

public class PageBaseBean {


    @SerializedName("status")
    public int status;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public PageBean data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PageBean getData() {
        return data;
    }

    public void setData(PageBean data) {
        this.data = data;
    }
}
