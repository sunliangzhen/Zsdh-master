package com.shuxiangbaima.task.interfaces.utilss;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.shuxiangbaima.task.config.AppConfig;
import com.toocms.dink5.mylibrary.net.ApiListener;
import com.toocms.dink5.mylibrary.net.ApiTool;


import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/8/17.
 */
public class a {

    /**
     * 获取访问的url
     */
    public String getUri(Map<String, String> map) {
        String url = map.get("url");
        map.remove("url");
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> keys = new ArrayList<>();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            keys.add(key);
        }

        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        for (int i = 0; i < keys.size(); i++) {
            String s = keys.get(i);
            if (i != 0) {
                try {
                    buffer.append("&" + s + "=" + URLEncoder.encode(map.get(s), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    buffer.append(s + "=" + URLEncoder.encode(map.get(s), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        url = url + "?" + buffer.toString();
        String sign = HMACSHA256(buffer.toString().getBytes(), AppConfig.KEY.getBytes());
        url = url + "&sign=" + sign;
        return url;
    }

    /**
     * 获取访问的url
     */
    public String getUri2(Map<String, String> map, String url) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> keys = new ArrayList<>();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            keys.add(key);
        }

        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        for (int i = 0; i < keys.size(); i++) {
            String s = keys.get(i);
            if (i != 0) {
                try {
                    buffer.append("&" + s + "=" + URLEncoder.encode(map.get(s), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    buffer.append(s + "=" + URLEncoder.encode(map.get(s), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        url = url + "?" + buffer.toString();
        String sign = HMACSHA256(buffer.toString().getBytes(), AppConfig.KEY.getBytes());
        url = url + "&sign=" + sign;
        LogUtil.e(url);
        return url;
    }

    /**
     * 访问网络（post）
     */
    public void aaNet2(Map<String, String> map, ApiListener apiListener) {
        String url = map.get("url");
        map.remove("url");
        String sr_0 = null;
        String sr_1 = null;
        String sr_2 = null;
        if (map.containsKey("sr_0")) {
            sr_0 = map.get("sr_0");
            map.remove("sr_0");
        }
        if (map.containsKey("sr_1")) {
            sr_1 = map.get("sr_1");
            map.remove("sr_1");
        }
        if (map.containsKey("sr_2")) {
            sr_2 = map.get("sr_2");
            map.remove("sr_2");
        }
        Log.e("map", map.toString());

        String sign = sortSign(map);
        RequestParams params = new RequestParams(url);
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            params.addBodyParameter(key, map.get(key));
        }
        if (!TextUtils.isEmpty(sr_0)) {
            params.addBodyParameter("sr_0", new File(sr_0));
        }
        if (!TextUtils.isEmpty(sr_1)) {
            params.addBodyParameter("sr_1", new File(sr_1));
        }
        if (!TextUtils.isEmpty(sr_2)) {
            params.addBodyParameter("sr_2", new File(sr_2));
        }
        Log.e("sign", sign);
        try {
            params.addBodyParameter("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setConnectTimeout(10000);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 访问网络（post）
     */
    public void aaNet(Map<String, String> map, ApiListener apiListener) {
        String url = map.get("url");
        map.remove("url");
        String avatar = null;
        String captcha = null;
        if (map.containsKey("captcha")) {
            captcha = map.get("captcha");
            map.remove("captcha");
        }
        if (map.containsKey("avatar")) {
            avatar = map.get("avatar");
            map.remove("avatar");
        }
        Log.e("map", map.toString());

        String sign = sortSign(map);
        RequestParams params = new RequestParams(url);
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            params.addBodyParameter(key, map.get(key));

        }
        if (!TextUtils.isEmpty(captcha)) {
            params.addBodyParameter("captcha", captcha);
        }
        if (!TextUtils.isEmpty(avatar)) {
            params.addBodyParameter("avatar", new File(avatar));
        }
        Log.e("sign", sign);
        try {
            params.addBodyParameter("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setConnectTimeout(10000);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * sign排序加密
     */
    public  String sortSign(Map<String, String> map) {
        String sign = null;
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> keys = new ArrayList<>();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            keys.add(key);
        }
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        for (int i = 0; i < keys.size(); i++) {
            String s = keys.get(i);
            if (i != 0) {
                try {
                    buffer.append("&" + s + "=" + URLEncoder.encode(map.get(s), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    buffer.append(s + "=" + URLEncoder.encode(map.get(s), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            sign = HMACSHA256(buffer.toString().getBytes("UTF-8"), AppConfig.KEY.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        Log.e("s", buffer.toString());
        return sign;
    }


    /**
     * MD5 加密
     */

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toLowerCase();
    }

    /**
     * hmac_sha256加密
     */

    private String HMACSHA256(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * <p>
     * 渠道标志为：
     * 1，andriod（a）
     * <p>
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {

        StringBuilder deviceId = new StringBuilder();
// 渠道标志
        deviceId.append("a");
        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                Log.e("imei", deviceId.toString());
                return deviceId.toString();
            }
//wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!TextUtils.isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                return deviceId.toString();
            }


//序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                return deviceId.toString();
            }

////如果上面都没有， 则生成一个id：随机码
//            String uuid = getUUID(context);
//            if (!TextUtils.isEmpty(uuid)) {
//                deviceId.append("id");
//                deviceId.append(uuid);
//                return deviceId.toString();
//            }
        } catch (Exception e) {
//            e.printStackTrace();
//            deviceId.append("id").append(getUUID(context));
        }

        return deviceId.toString();
    }


    /**
     //     * 得到全局唯一UUID
     //     */
//    public static String getUUID(Context context) {
//        SharedPreferences mShare = getSysShare(context, "sysCacheMap");
//        if (mShare != null) {
//            uuid = mShare.getString("uuid", "");
//        }
//        if (TextUtils.isEmpty(uuid)) {
//            uuid = UUID.randomUUID().toString();
//            saveSysMap(context, "sysCacheMap", "uuid", uuid);
//        }
//        PALog.e(tag, "getUUID : " + uuid);
//        return uuid;
//    }


}
