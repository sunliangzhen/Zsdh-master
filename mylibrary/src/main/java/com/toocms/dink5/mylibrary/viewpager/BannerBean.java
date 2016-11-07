package com.toocms.dink5.mylibrary.viewpager;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public class BannerBean {


    @SerializedName("status")
    public int status;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public DataEntity data;

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

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {

        @SerializedName("count")
        public int count;
        @SerializedName("expire")
        public int expire;
        @SerializedName("ad_list")
        public List<AdListEntity> adList;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public List<AdListEntity> getAdList() {
            return adList;
        }

        public void setAdList(List<AdListEntity> adList) {
            this.adList = adList;
        }

        public static class AdListEntity {

            @SerializedName("ad_img")
            public String adImg;
            @SerializedName("ad_link")
            public String adLink;

            public String getAdImg() {
                return adImg;
            }

            public void setAdImg(String adImg) {
                this.adImg = adImg;
            }

            public String getAdLink() {
                return adLink;
            }

            public void setAdLink(String adLink) {
                this.adLink = adLink;
            }
        }
    }
}
