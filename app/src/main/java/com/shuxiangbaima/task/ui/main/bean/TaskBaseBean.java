package com.shuxiangbaima.task.ui.main.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/24.
 */

public class TaskBaseBean {


    @SerializedName("status")
    public int status;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public TaskBean data;


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

    public TaskBean getData() {
        return data;
    }

    public void setData(TaskBean data) {
        this.data = data;
    }
}
