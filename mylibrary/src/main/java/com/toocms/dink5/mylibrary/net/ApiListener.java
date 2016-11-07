package com.toocms.dink5.mylibrary.net;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.Map;

/**
 * @author Zero
 * @date 2016/4/13 8:19
 */

public interface ApiListener {
    void onCancelled(Callback.CancelledException var1);

    void onComplete(RequestParams var1, String var2);

    void onError(Map<String, String> var1, RequestParams var2);

    void onException(Throwable var1, RequestParams params);
}

