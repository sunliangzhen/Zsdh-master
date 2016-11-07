package com.toocms.dink5.mylibrary.commonutils.utils;

import java.util.List;

/**
 * @author Zero
 * @date 2016/4/13 9:15
 */
public class ListUtils {
    public static String join(List<String> list) {
        return join(list, ",");
    }

    public static <V> boolean isEmpty(List<V> sourceList) {
        return sourceList == null || sourceList.size() == 0;
    }

    public static String join(List<String> list, String separator) {
        if (isEmpty(list)) {
            return "";
        } else {
            if (separator == null) {
                separator = ",";
            }
            StringBuilder joinStr = new StringBuilder();
            for (int i = 0; i < list.size(); ++i) {
                joinStr.append((String) list.get(i));
                if (i != list.size() - 1) {
                    joinStr.append(separator);
                }
            }
            return joinStr.toString();
        }
    }
    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null?0:sourceList.size();
    }
}
