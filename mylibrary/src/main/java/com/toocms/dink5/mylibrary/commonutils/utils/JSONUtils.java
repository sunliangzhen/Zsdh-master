package com.toocms.dink5.mylibrary.commonutils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Zero
 * @date 2016/4/15 9:42
 */
public class JSONUtils {
    public static boolean isPrintException = true;

    public static Map<String, String> parseDataToMap(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        } else {
            try {
                JSONObject e = new JSONObject(source);
                return parseKeyAndValueToMap(e.getJSONObject("data"));
            } catch (JSONException var2) {
                if (isPrintException) {
                    var2.printStackTrace();
                }
                return null;
            }
        }
    }

    public static ArrayList<Map<String, String>> parseDataToMapList(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        } else {
            try {
                ArrayList e = new ArrayList();
                JSONObject jsonObject = new JSONObject(source);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    e.add(parseKeyAndValueToMap(jsonArray.getJSONObject(i)));
                }
                return e;
            } catch (JSONException var5) {
                if (isPrintException) {
                    var5.printStackTrace();
                }
                return null;
            }
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
        if (sourceObj == null) {
            return null;
        } else {
            HashMap keyAndValueMap = new HashMap();
            Iterator iter = sourceObj.keys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                putMapNotEmptyKey(keyAndValueMap, key, getString(sourceObj, key, ""));
            }
            return keyAndValueMap;
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        } else {
            try {
                JSONObject e = new JSONObject(source);
                return parseKeyAndValueToMap(e);
            } catch (JSONException var2) {
                if (isPrintException) {
                    var2.printStackTrace();
                }
                return null;
            }
        }
    }

    public static ArrayList<Map<String, String>> parseKeyAndValueToMapList(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        } else if (!source.startsWith("[") && !source.endsWith("]")) {
            return null;
        } else {
            try {
                ArrayList e = new ArrayList();
                JSONArray jsonArray = new JSONArray(source);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    e.add(parseKeyAndValueToMap(jsonObject));
                }
                return e;
            } catch (JSONException var5) {
                if (isPrintException) {
                    var5.printStackTrace();
                }
                return null;
            }
        }
    }

    public static boolean putMapNotEmptyKey(Map<String, String> map, String key, String value) {
        if (map != null && !StringUtils.isEmpty(key)) {
            map.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if (jsonObject != null && !StringUtils.isEmpty(key)) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException var4) {
                if (isPrintException) {
                    var4.printStackTrace();
                }
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
}
