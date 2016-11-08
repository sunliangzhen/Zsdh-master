package com.shuxiangbaima.task.ui.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.api.BaseAty;
import com.shuxiangbaima.task.config.AppConfig;
import com.shuxiangbaima.task.interfaces.Invitation;
import com.shuxiangbaima.task.wxapi.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.toocms.dink5.mylibrary.base.BasAty;
import com.toocms.dink5.mylibrary.commonutils.utils.JSONUtils;
import com.toocms.dink5.mylibrary.commonwidget.LoadingTip;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.net.URL;
import java.util.Map;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by Administrator on 2016/8/11.
 */
public class SunpaiAty extends BaseAty implements LoadingTip.onReloadListener {

    @ViewInject(R.id.include)
    private View include;
    @ViewInject(R.id.yq_imgv_code)
    private ImageView imgv_code;
    @ViewInject(R.id.sunpai_tv_mon)
    private TextView tv_mon;
    @ViewInject(R.id.sunpai_tv_pai)
    private TextView tv_pai;
    private RecyclerView recyclerView;
    @ViewInject(R.id.loadedTip)
    private LoadingTip loadedTip;
    @ViewInject(R.id.center)
    private RelativeLayout center;

    private Invitation invitation;
    private IWXAPI api;
    private String invimg;
    private String invlink;
    private String invtext;
    private static final int THUMB_SIZE = 150;

    private String total_profit;
    private String rank;

    @Override
    protected int getLayoutResId() {

        return R.layout.aty_sunpai;
    }

    @Override
    protected void initialized() {
        total_profit = getIntent().getStringExtra("total_profit");
        rank = getIntent().getStringExtra("rank");
        invitation = new Invitation();
    }

    @Override
    public void requestData() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        invitation.invcode(this, this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regToWx();
        tv_mon.setText("赚了" + total_profit + "元");
        if (total_profit.equals("0")) {
            tv_pai.setText("无收益");
        } else {
            tv_pai.setText(rank);
        }
        loadedTip.setOnReloadListener(this);
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        include.setVisibility(View.GONE);
    }

    @Event(value = {R.id.sunpai_imgv_back, R.id.yq_fb_share, R.id.include, R.id.share_weip, R.id.share_wei, R.id.share_qq})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.sunpai_imgv_back:
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

    @Override
    public void onComplete(RequestParams var1, String var2) {
        super.onComplete(var1, var2);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        center.setVisibility(View.VISIBLE);
        String status = JSONUtils.parseKeyAndValueToMap(var2).get("status");
        if (var1.getUri().contains("invcode")) {
            if (status.equals("200")) {
                Map<String, String> map = JSONUtils.parseDataToMap(var2);
                invimg = map.get("invimg");
                invlink = map.get("invlink");
                invtext = map.get("invtext");
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        return QRCodeEncoder.syncEncodeQRCode(invlink, BGAQRCodeUtil.dp2px(SunpaiAty.this, 150));
                    }
                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            imgv_code.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(SunpaiAty.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        }
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

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
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
