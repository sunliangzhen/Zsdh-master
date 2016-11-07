package com.shuxiangbaima.task.ui.main.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainAty extends Activity {
    private WebView webView;
//    public static final int INPUT_FILE_REQUEST_CODE = 1;
//    private ValueCallback<Uri> mUploadMessage;
//    private final static int FILECHOOSER_RESULTCODE = 2;
//    private ValueCallback<Uri[]> mFilePathCallback;
//
//    private String mCameraPhotoPath;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.aty_main);
//        new StatuBarBasty().changeStatusBar(this);

//        webView = (WebView) findViewById(R.id.main_web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);//可以使用Android4.4手机和Chrome Inspcet Device联调
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//5.0以上
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if( url.startsWith("http:") || url.startsWith("https:") ) {
//                    return false;
//                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
//        webView.setWebChromeClient(mWebChromeClient);

//        webView.loadUrl("http://ecd-home.toocms.com/");
        webView.loadUrl("http://mp.weixin.qq.com/s?__biz=MjM5MjAwOTkwMA==&mid=2658017966&idx=1&sn=7b833f3c6817a09e4d233dcc4a82d34b&chksm=bd36596d8a41d07bacc16a14cb0b5cf25ac78528eae85798541fd0e2a1596689dedad23ffb59&scene=1&srcid=09102y81LnRapDvzkNRdoAle#rd");
        webView.setWebChromeClient(
                new WebChromeClient() {

                    //扩展浏览器上传文件
                    //3.0++版本
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                        openFileChooserImpl(uploadMsg);
                    }

                    //3.0--版本
                    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                        openFileChooserImpl(uploadMsg);
                    }
                    //4.0--版本
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                        openFileChooserImpl(uploadMsg);
                    }

                    // For Android > 5.0
                    public boolean onShowFileChooser (WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
                        openFileChooserImplForAndroid5(uploadMsg);
                        return true;
                    }
                }
        );
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null: intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5){
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null: intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

//
//    //在sdcard卡创建缩略图
//    //createImageFileInSdcard
//    @SuppressLint("SdCardPath")
//    private File createImageFile() {
//        //mCameraPhotoPath="/mnt/sdcard/tmp.png";
//        File file = new File(Environment.getExternalStorageDirectory() + "/", "tmp.png");
//        mCameraPhotoPath = file.getAbsolutePath();
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return file;
//    }
//
//    private WebChromeClient mWebChromeClient = new WebChromeClient() {
//
//
//        // android 5.0 这里需要使用android5.0 sdk
//        public boolean onShowFileChooser(
//                WebView webView, ValueCallback<Uri[]> filePathCallback,
//                WebChromeClient.FileChooserParams fileChooserParams) {
//
//            Log.d(TAG, "onShowFileChooser");
//            if (mFilePathCallback != null) {
//                mFilePathCallback.onReceiveValue(null);
//            }
//            mFilePathCallback = filePathCallback;
//            /**
//             Open Declaration   String android.provider.MediaStore.ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE"
//             Standard Intent action that can be sent to have the camera application capture an image and return it.
//             The caller may pass an extra EXTRA_OUTPUT to control where this image will be written. If the EXTRA_OUTPUT is not present, then a small sized image is returned as a Bitmap object in the extra field. This is useful for applications that only need a small image. If the EXTRA_OUTPUT is present, then the full-sized image will be written to the Uri value of EXTRA_OUTPUT. As of android.os.Build.VERSION_CODES.LOLLIPOP, this uri can also be supplied through android.content.Intent.setClipData(ClipData). If using this approach, you still must supply the uri through the EXTRA_OUTPUT field for compatibility with old applications. If you don't set a ClipData, it will be copied there for you when calling Context.startActivity(Intent).
//             See Also:EXTRA_OUTPUT
//             标准意图，被发送到相机应用程序捕获一个图像，并返回它。通过一个额外的extra_output控制这个图像将被写入。如果extra_output是不存在的，
//             那么一个小尺寸的图像作为位图对象返回。如果extra_output是存在的，那么全尺寸的图像将被写入extra_output URI值。
//             */
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                // Create the File where the photo should go
//                File photoFile = null;
//                try {
//                    //设置MediaStore.EXTRA_OUTPUT路径,相机拍照写入的全路径
//                    photoFile = createImageFile();
//                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
//                } catch (Exception ex) {
//                    // Error occurred while creating the File
//                    Log.e("WebViewSetting", "Unable to create Image File", ex);
//                }
//
//                // Continue only if the File was successfully created
//                if (photoFile != null) {
//                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(photoFile));
//                    System.out.println(mCameraPhotoPath);
//                } else {
//                    takePictureIntent = null;
//                }
//            }
//
//            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//            contentSelectionIntent.setType("image/*");
//            Intent[] intentArray;
//            if (takePictureIntent != null) {
//                intentArray = new Intent[]{takePictureIntent};
//                System.out.println(takePictureIntent);
//            } else {
//                intentArray = new Intent[0];
//            }
//
//            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//
//            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
//
//            return true;
//        }
//
//
//        //The undocumented magic method override
//        //Eclipse will swear at you if you try to put @Override here
//        // For Android 3.0+
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            Log.d(TAG, "openFileChooser1");
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//            startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);
//        }
//
//        // For Android 3.0+
//        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//            Log.d(TAG, "openFileChooser2");
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//            startActivityForResult(
//                    Intent.createChooser(i, "Image Chooser"),
//                    FILECHOOSER_RESULTCODE);
//        }
//
//        //For Android 4.1
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//            Log.d(TAG, "openFileChooser3");
//
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//            startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);
//        }
//    };
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult");
//
//        if (requestCode == FILECHOOSER_RESULTCODE) {
//            if (null == mUploadMessage) return;
//            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
//            if (result != null) {
//                String imagePath = result.getPath();
//                if (!TextUtils.isEmpty(imagePath)) {
//                    result = Uri.parse("file:///" + imagePath);
//                }
//            }
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//        } else if (requestCode == INPUT_FILE_REQUEST_CODE && mFilePathCallback != null) {
//            // 5.0的回调
//            Uri[] results = null;
//
//            // Check that the response is a good one
//            if (resultCode == Activity.RESULT_OK) {
//                if (data == null) {
//                    // If there is not data, then we may have taken a photo
//                    if (mCameraPhotoPath != null) {
//                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
//                    }
//                } else {
//                    String dataString = data.getDataString();
//                    if (dataString != null) {
//                        results = new Uri[]{Uri.parse(dataString)};
//                    }
//                }
//            }
//
//            mFilePathCallback.onReceiveValue(results);
//            mFilePathCallback = null;
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//            return;
//        }
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //获取历史列表
            WebBackForwardList mWebBackForwardList = webView
                    .copyBackForwardList();
            //判断当前历史列表是否最顶端,其实canGoBack已经判断过
            if (mWebBackForwardList.getCurrentIndex() > 0) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void checkAppIsUsing(String orginName, String destinationName, String regionName, String lon, String lat) {
        String[] items = {"百度地图", "高德地图"};
        new AlertDialog.Builder(this).setTitle("提示")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (isAvilible(MainAty.this, "com.baidu.BaiduMap")) {//传入指定应用包名
                                    Intent intent = null;
                                    try {
//                    intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068" +
//                            "|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                        intent = Intent.getIntent("intent://map/direction?" +
                                                "origin=大柏树&" +
                                                "destination=江湾镇" +
                                                "&mode=driving&" +
                                                "region=上海" +
                                                "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                        startActivity(intent); //启动调用
                                    } catch (URISyntaxException e) {
                                        Log.e("intent", e.getMessage());
                                    }
                                } else {//未安装
                                    //market为路径，id为包名
                                    //显示手机上所有的market商店
                                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                                break;
                            case 1:
                                if (isAvilible(MainAty.this, "com.autonavi.minimap")) {//传入指定应用包名
                                    goToNaviActivity(MainAty.this, "test", null, "34.264642646862", "108.95108518068", "1", "2");
                                } else {//未安装
                                    //market为路径，id为包名
                                    //显示手机上所有的market商店
                                    Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                                break;
                        }
                    }
                })
                .show();
    }


    //获取手机上的软件比对看看是否安装着这款软件
    private boolean isAvilible(Context context, String packageName) {
        //用于存储所有已安装程序的包名
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        List<String> packageNames = new ArrayList<>();
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 启动高德App进行导航
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:58
     * <h3>UpdateTime</h3> 2016/6/27,13:58
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param sourceApplication 必填 第三方调用应用名称。如 amap
     * @param poiname           非必填 POI 名称
     * @param lat               必填 纬度
     * @param lon               必填 经度
     * @param dev               必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style             必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    private void goToNaviActivity(Context context, String sourceApplication, String poiname, String lat, String lon, String dev, String style) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)) {
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);

        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

//    //
//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this).setTitle("提示")
//                .setMessage("是否确认退出?")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                })
//                .show();
//    }


}

