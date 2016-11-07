package com.toocms.dink5.mylibrary.commonutils.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Zero
 * @date 2016/7/8 9:36
 */
public class MapUtils {

    public static final String DEFAULT_KEY_AND_VALUE_SEPARATOR = ":";
    public static final String DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR = ",";

    public MapUtils() {
    }

    public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
        return sourceMap == null || sourceMap.size() == 0;
    }

    public static boolean putMapNotEmptyKey(Map<String, String> map, String key, String value) {
        if (map != null && !StringUtils.isEmpty(key)) {
            map.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value) {
        if (map != null && !StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
            map.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value, String defaultValue) {
        if (map != null && !StringUtils.isEmpty(key)) {
            map.put(key, StringUtils.isEmpty(value) ? defaultValue : value);
            return true;
        } else {
            return false;
        }
    }

    public static <K, V> boolean putMapNotNullKey(Map<K, V> map, K key, V value) {
        if (map != null && key != null) {
            map.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public static <K, V> boolean putMapNotNullKeyAndValue(Map<K, V> map, K key, V value) {
        if (map != null && key != null && value != null) {
            map.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public static <K, V> Object getKeyByValue(Map<K, V> map, V value) {
        if (isEmpty(map)) {
            return null;
        } else {
            Iterator var2 = map.entrySet().iterator();

            Map.Entry entry;
            do {
                if (!var2.hasNext()) {
                    return null;
                }
                entry = (Map.Entry) var2.next();
            } while (!ObjectUtils.isEquals(entry.getValue(), value));

            return entry.getKey();
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(String source, String keyAndValueSeparator, String keyAndValuePairSeparator, boolean ignoreSpace) {
        if (StringUtils.isEmpty(source)) {
            return null;
        } else {
            if (StringUtils.isEmpty(keyAndValueSeparator)) {
                keyAndValueSeparator = ":";
            }

            if (StringUtils.isEmpty(keyAndValuePairSeparator)) {
                keyAndValuePairSeparator = ",";
            }

            HashMap keyAndValueMap = new HashMap();
            String[] keyAndValueArray = source.split(keyAndValuePairSeparator);
            if (keyAndValueArray == null) {
                return null;
            } else {
                String[] var7 = keyAndValueArray;
                int var8 = keyAndValueArray.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    String valueEntity = var7[var9];
                    if (!StringUtils.isEmpty(valueEntity)) {
                        int seperator = valueEntity.indexOf(keyAndValueSeparator);
                        if (seperator != -1) {
                            if (ignoreSpace) {
                                putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator).trim(), valueEntity.substring(seperator + 1).trim());
                            } else {
                                putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator), valueEntity.substring(seperator + 1));
                            }
                        }
                    }
                }

                return keyAndValueMap;
            }
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(String source, boolean ignoreSpace) {
        return parseKeyAndValueToMap(source, ":", ",", ignoreSpace);
    }

    public static Map<String, String> parseKeyAndValueToMap(String source) {
        return parseKeyAndValueToMap(source, ":", ",", true);
    }

    public static String toJson(Map<String, String> map) {
        if (map != null && map.size() != 0) {
            StringBuilder paras = new StringBuilder();
            paras.append("{");
            Iterator ite = map.entrySet().iterator();

            while (ite.hasNext()) {
                Map.Entry entry = (Map.Entry) ite.next();
                paras.append("\"").append((String) entry.getKey()).append("\":\"").append((String) entry.getValue()).append("\"");
                if (ite.hasNext()) {
                    paras.append(",");
                }
            }

            paras.append("}");
            return paras.toString();
        } else {
            return null;
        }
    }
}
