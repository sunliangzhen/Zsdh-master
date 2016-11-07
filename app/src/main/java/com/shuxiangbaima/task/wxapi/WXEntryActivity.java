package com.shuxiangbaima.task.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.shuxiangbaima.task.R;
import com.shuxiangbaima.task.config.AppConfig;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.toocms.dink5.mylibrary.base.BasAty;

public class WXEntryActivity extends BasAty implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_main;
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void initialized() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppConfig.APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
//        switch (req.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;
//        Log.e("fdfdf", baseResp.errCode + "");
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        goToGetMsg();
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }


    private void goToGetMsg() {
//        if (PreferencesUtils.getString(this, "share_type").equals("yq")) {
//            AppManager.getInstance().killActivity(YqAty.class);
//            Intent intent = new Intent(this, YqAty.class);
//            intent.putExtras(getIntent());
//            startActivity(intent);
//            finish();
//        }else if(PreferencesUtils.getString(this, "share_type").equals("details")){
//
//        }
        finish();
    }

//    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
//        WXMediaMessage wxMsg = showReq.message;
//        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
//
//        StringBuffer msg = new StringBuffer(); // ��֯һ������ʾ����Ϣ����
//        msg.append("description: ");
//        msg.append(wxMsg.description);
//        msg.append("\n");
//        msg.append("extInfo: ");
//        msg.append(obj.extInfo);
//        msg.append("\n");
//        msg.append("filePath: ");
//        msg.append(obj.filePath);
//
//        Intent intent = new Intent(this, ShowFromWXActivity.class);
//        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//        startActivity(intent);
//        finish();
//    }
}