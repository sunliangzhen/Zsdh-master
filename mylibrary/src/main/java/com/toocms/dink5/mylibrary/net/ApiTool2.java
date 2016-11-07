package com.toocms.dink5.mylibrary.net;


import android.text.TextUtils;
import android.util.Log;


import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Zero
 * @date 2016/4/13 8:18
 */
public class ApiTool2 {

    private String uri;

    public void postApi(Call<String> call, ApiListener2 apiListener, String uri) {
        this.uri = uri;
        call.enqueue(new DefaultCallback(apiListener));
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

    private class DefaultCallback implements retrofit2.Callback<String> {
        private ApiListener2 apiListener;

        public DefaultCallback(ApiListener2 apiListener) {
            this.apiListener = apiListener;
        }

        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            String result = (String) response.body();
            if (!TextUtils.isEmpty(result)) {
//                LogUtil.e(result);
                Log.e("d", result);
            }
            this.apiListener.onComplete2(uri, result);
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            LogUtil.e(t.toString());
        }
    }
}

