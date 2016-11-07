package com.toocms.dink5.mylibrary.net;


import android.text.TextUtils;


import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonutils.utils.MapUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * @author Zero
 * @date 2016/4/13 8:18
 */
public class ApiTool {
    public ApiTool() {
    }

    RequestParams params;

    public Callback.Cancelable getApi(RequestParams params, ApiListener apiListener) {
        this.params = params;
        return x.http().get(params, new ApiTool.DefaultRequestCallBack(apiListener));
    }

    public Callback.Cancelable postApi(RequestParams params, ApiListener apiListener) {
        this.params = params;
        return x.http().post(params, new ApiTool.DefaultRequestCallBack(apiListener));
    }


    private class DefaultRequestCallBack implements Callback.ProgressCallback<String> {
        private ApiListener apiListener;

        public DefaultRequestCallBack(ApiListener apiListener) {
            this.apiListener = apiListener;
        }

        @Override
        public void onSuccess(String result) {
            if (!TextUtils.isEmpty(result)) {
                LogUtil.e(result);
            }
            try {
                Map e = ApiTool.this.parseError(result);
                if (MapUtils.isEmpty(e)) {
                    if (this.apiListener != null) {
                        this.apiListener.onComplete(params, result);
                    }
                } else if (this.apiListener != null) {
                    this.apiListener.onError(e, params);
                }
            } catch (Exception var4) {
                if (this.apiListener != null) {
                    this.apiListener.onException(var4, params);
                }
            }
        }

        public void onError(Throwable ex, boolean isOnCallback) {
            this.apiListener.onException(ex, params);
        }

        public void onCancelled(CancelledException cex) {
            this.apiListener.onCancelled(cex);
        }

        public void onFinished() {

        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {

        }
    }

    public static Map<String, String> parseError(String json) {
        JSONObject jsonObject = null;
        if (json.startsWith("[") && json.endsWith("]")) {
            return null;
        } else {
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException var4) {
                return null;
            }

            String flag = jsonObject.optString("flag");
            return flag != null && flag.equals("error") ? JSONUtils.parseKeyAndValueToMap(json) : null;
        }
    }
}

