package com.shuxiangbaima.task.ui.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.Invitation;
import com.shuxiangbaima.task.wxapi.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.net.URL;
import java.util.Map;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by Administrator on 2016/8/10.
 */
public class YqAty extends BasAty implements LoadingTip.onReloadListener {

    @ViewInject(R.id.yhm_tv_code)
    private TextView tv_code;
    @ViewInject(R.id.yq_imgv_code)
    private ImageView imgv_code;
    @ViewInject(R.id.include)
    private View include;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
    @ViewInject(R.id.linlay)
    private LinearLayout linlay;

    private Invitation invitation;
    private IWXAPI api;
    private String invimg;
    private String invlink;
    private String invtext;

//    private ImageOptions imageOptions;
    private static final int THUMB_SIZE = 150;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_yq;
    }

    @Override
    protected void initialized() {
        invitation = new Invitation();
//        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
    }

    @Override
    protected void requestData() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        invitation.invcode(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        include.setVisibility(View.GONE);
    }


    @Event(value = {R.id.yq_imgv_back, R.id.yq_fb_share, R.id.include, R.id.share_weip, R.id.share_wei, R.id.share_qq})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.yq_imgv_back:
                finish();
                break;
            case R.id.yq_fb_share:
                include.setVisibility(View.VISIBLE);
                break;
            case R.id.include:
                include.setVisibility(View.GONE);
                break;
            case R.id.share_weip:
                sendUrl("friend");
                break;
            case R.id.share_wei:
                sendUrl("");
                break;
            case R.id.share_qq:
                break;
        }
    }


    private void sendText() {
        WXTextObject textObj = new WXTextObject();
        textObj.text = "https://www.baidu.com/";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = "dddddddddds";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
//                req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 分享图片
     */
    private void sendImg() {
        new Thread() {
            @Override
            public void run() {
                try {
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
                    Bitmap bmp = BitmapFactory.decodeStream(new URL(invimg).openStream());
                    WXImageObject imgObj = new WXImageObject(bmp);
                    WXMediaMessage msg = new WXMediaMessage();
                    msg.mediaObject = imgObj;
                    msg.description = "https://www.baidu.com/";

                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    bmp.recycle();
                    msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // ��������ͼ
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("img");
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    api.sendReq(req);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
                    webpage.webpageUrl = invlink;
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.description = invtext;
                    msg.title = invtext;
                    Bitmap bmp = BitmapFactory.decodeStream(new URL(invimg).openStream());
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    bmp.recycle();
//            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
//            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regToWx();
        loadedTip.setOnReloadListener(this);

    }

    @Override
    public void initPresenter() {

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        removeProgressContent();
        linlay.setVisibility(View.VISIBLE);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        String status = JSONUtils.parseKeyAndValueToMap(var2).get("status");
        if (var1.getUri().contains("invcode")) {
            if (status.equals("200")) {
                Map<String, String> map = JSONUtils.parseDataToMap(var2);
                tv_code.setText(map.get("invcode"));
                invimg = map.get("invimg");
                invlink = map.get("invlink");
                invtext = map.get("invtext");

                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        return QRCodeEncoder.syncEncodeQRCode(invlink, BGAQRCodeUtil.dp2px(YqAty.this, 150));
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            imgv_code.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(YqAty.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        }
    }

    @Override
    public void onException(Throwable var1, RequestParams params) {
        super.onException(var1, params);
        if (params.getUri().contains("invcode")) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
        }
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, AppConfig.APP_ID, true);
        api.registerApp(AppConfig.APP_ID);
    }


    @Override
    public void reload() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        invitation.invcode(this, this);
    }
}
