package com.shuxiangbaima.task.ui.main.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.Task;
import com.shuxiangbaima.task.ui.lar.LoginAty;
import com.shuxiangbaima.task.wxapi.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.toocms.dink5.mylibrary.app.AppConstant;
import com.toocms.dink5.mylibrary.app.Config;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/10.
 */
public class DetailsAty extends BaseAty implements LoadingTip.onReloadListener {

    @ViewInject(R.id.details_web)
    private WebView details_web;
    @ViewInject(R.id.details_imgv_cover)
    private ImageView imgv_cover;
    @ViewInject(R.id.details_tv_title)
    private TextView tv_title;
    @ViewInject(R.id.details_tv_mon)
    private TextView tv_mon;
    @ViewInject(R.id.details_tv_state)
    private TextView tv_state;
    @ViewInject(R.id.include)
    private View include;

    @ViewInject(R.id.details_tv_more)
    private TextView btn_share;
    @ViewInject(R.id.details_register)
    private LinearLayout linlay_register;
    @ViewInject(R.id.details_task_01)
    private TextView task_01;
    @ViewInject(R.id.details_task_02)
    private TextView task_02;
    @ViewInject(R.id.myProgressBar)
    private ProgressBar progressBar;
    @ViewInject(R.id.details_relay)
    private RelativeLayout details_relay;
    @ViewInject(R.id.linlay)
    private LinearLayout linlay;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;

    //    private ImageOptions imageOptions;
    private String task_id;
    private String task_type;
    private String img_url;
    private String name;
    private String mon;
    private Task task;
    private Map<String, String> map;

    private String state;
    private IWXAPI api;
    private String share_link;
    private String share_text;
    private String share_img;
    private static final int THUMB_SIZE = 150;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_details;
    }

    @Override
    public void requestData() {
    }

    @Override
    protected void initialized() {
        task = new Task();
//        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        task_id = getIntent().getStringExtra("task_id");
        task_type = getIntent().getStringExtra("task_type");
        img_url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        state = getIntent().getStringExtra("state");
        mon = getIntent().getStringExtra("mon");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (task_type.equals("1")) {
            include.setVisibility(View.GONE);
        }
        showProgressContent();
        task.task_info(task_id, this, this);
    }

    @Event(value = {R.id.details_back, R.id.include, R.id.details_tv_more, R.id.share_weip, R.id.share_wei, R.id.details_task_01})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.details_back:
                onBackPressed();
                break;
            case R.id.details_tv_more:
                if (Config.isLogin()) {
                    if (TextUtils.isEmpty(share_link)) {
                        task.share_link(task_id, this, this);
                    } else {
//                        btn_share.setVisibility(View.GONE);
                        include.setVisibility(View.VISIBLE);
                    }
                } else {
                    Intent intent = new Intent(this, LoginAty.class);
                    intent.putExtra("type", AppConfig.ATY_SHARE);
                    startActivityForResult(intent, AppConfig.ATY_SHARE);
                }
                break;
            case R.id.include:
                include.setVisibility(View.GONE);
//                btn_share.setVisibility(View.VISIBLE);
                break;
            case R.id.share_weip:
                sendUrl("friend");
                break;
            case R.id.share_wei:
                sendUrl("");
                break;
            case R.id.details_task_01:
                if (Config.isLogin() && state.equals("1")) {
                    task.submit_fields(task_id, this, this);
                } else if (!Config.isLogin()) {
                    Intent intent = new Intent(this, LoginAty.class);
                    intent.putExtra("type", AppConfig.ATY_SUBMIT);
                    startActivityForResult(intent, AppConfig.ATY_SUBMIT);
                }
                break;
        }
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regToWx();
        details_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    linlay.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    setDataType();
                }
            }
        });
        loadedTip.setOnReloadListener(this);
        Glide.with(this)
                .load(img_url)
                .into(imgv_cover);
        tv_title.setText(name);
        tv_mon.setText(mon + "");
        switch (state) {
            case "1":
                tv_state.setText("进行中");
                tv_state.setBackgroundResource(R.drawable.shape_page_s);
                btn_share.setText("分享");
                break;
            case "2":
                tv_state.setText("进行中");
                btn_share.setText("分享");
                tv_state.setBackgroundResource(R.drawable.shape_page_s);
                break;
            case "66":
                task_01.setBackgroundResource(R.drawable.shape_page_more2);
                task_02.setBackgroundResource(R.drawable.shape_page_more2);
                tv_state.setText("已抢光");
                btn_share.setText("友情分享");
                tv_state.setBackgroundResource(R.drawable.shape_page_s1);
                break;
            case "3":
                tv_state.setText("审核中");
                tv_state.setBackgroundResource(R.drawable.shape_page_s);
                task_01.setBackgroundResource(R.drawable.shape_page_more2);
                task_02.setBackgroundResource(R.drawable.shape_page_more2);
                btn_share.setText("友情分享");
                break;
            case "4":
                tv_state.setBackgroundResource(R.drawable.shape_page_s);
                task_01.setBackgroundResource(R.drawable.shape_page_more2);
                task_02.setBackgroundResource(R.drawable.shape_page_more2);
                tv_state.setText("已完成");
                btn_share.setText("友情分享");
                break;
        }
        initTransition(imgv_cover, AppConstant.TRANSITION_ANIMATION_NEWS_PHOTOS);
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        if (var1.getUri().contains("submit_fields") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            Bundle bundle = new Bundle();
            bundle.putString("data", var2);
            bundle.putString("task_id", task_id);
            startActivity(TaskSubmitAty.class, bundle);
        }
        if (var1.getUri().contains("share_link") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            Map<String, String> map = JSONUtils.parseDataToMap(var2);
            share_img = map.get("share_img");
            share_text = map.get("share_text");
            share_link = map.get("share_link");
            include.setVisibility(View.VISIBLE);
//            btn_share.setVisibility(View.GONE);
        }
        if (var1.getUri().contains("task_info") && JSONUtils.parseKeyAndValueToMap(var2).get("status").equals("200")) {
            map = JSONUtils.parseKeyAndValueToMap(JSONUtils.parseDataToMap(var2).get("task_info"));
            WebSettings webSettings = details_web.getSettings();
            webSettings.setJavaScriptEnabled(true);//设置支持JavaScript脚本
            webSettings.setAllowFileAccess(true);//设置可以访问文件
            webSettings.setBuiltInZoomControls(false);//设置支持缩放
            webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
            webSettings.setSupportZoom(false);  //支持缩放
            details_web.requestFocusFromTouch();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
            webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
            webSettings.setAppCacheEnabled(false);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setDomStorageEnabled(true);//可以使用Android4.4手机和Chrome Inspcet Device联调
            details_web.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        return false;
                    }
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }
//                syncCookie(this,url);
            });
            details_web.loadUrl(map.get("preview_link"));
        }
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (details_web.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            //获取历史列表
//            WebBackForwardList mWebBackForwardList = details_web
//                    .copyBackForwardList();
//            //判断当前历史列表是否最顶端,其实canGoBack已经判断过
//            if (mWebBackForwardList.getCurrentIndex() > 0) {
//                details_web.goBack();
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && details_web.canGoBack()) {
            details_web.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Sync Cookie
     */
    private void syncCookie(Context context, String url) {
        try {
//            Log.d("Nat: webView.syncCookie.url", url);

            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
//                Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", "INPUT YOUR JSESSIONID STRING"));
            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s", "INPUT YOUR PATH STRING"));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
//                Log.d("Nat: webView.syncCookie.newCookie", newCookie);
            }
        } catch (Exception e) {
//            Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }

    protected boolean openWithWevView(String url) {//处理判断url的合法性
// TODO Auto-generated method stub
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return true;
        }
        return false;
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        if (params.getUri().contains("task_info")) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        }
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, AppConfig.APP_ID, true);
        api.registerApp(AppConfig.APP_ID);
    }

    /**
     * 分享网页
     */
    private void sendUrl(final String type) {
        new Thread() {
            @Override
            public void run() {
                try {
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = share_link;
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.description = share_text;
                    msg.title = share_text;
                    Bitmap bmp = BitmapFactory.decodeStream(new URL(share_img).openStream());
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    bmp.recycle();
//            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
//            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    msg.thumbData = Util.bmpToByteArray2(thumbBmp, true);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    if (type.equals("friend")) {
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    } else {
                        req.scene = SendMessageToWX.Req.WXSceneSession;
                    }
                    api.sendReq(req);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void setDataType() {
        switch (task_type) {
            case "1":    //分享文章任务
                btn_share.setVisibility(View.VISIBLE);
                linlay_register.setVisibility(View.GONE);
                break;
            case "2":    //注册试用任务
                btn_share.setVisibility(View.GONE);
                linlay_register.setVisibility(View.VISIBLE);
                task_01.setText("提交任务");
                task_02.setText("开始注册");
                break;
            case "3":    //游戏测试任务，4 - 游戏充值任务
                btn_share.setVisibility(View.GONE);
                linlay_register.setVisibility(View.VISIBLE);
                task_01.setText("提交任务");
                task_02.setText("下载游戏");
                break;
            case "4":    //游戏测试任务，4 - 游戏充值任务
                btn_share.setVisibility(View.GONE);
                linlay_register.setVisibility(View.VISIBLE);
                task_01.setText("提交任务");
                task_02.setText("下载游戏");
                break;
        }
    }


    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        task.task_info(task_id, this, this);
    }
}
