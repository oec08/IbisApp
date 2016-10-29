package com.codepath.apps.ibisapp.utils;

/**
 * Created by ocarty on 10/29/2016.
 */

public class StringUtils {
    public static String addHashtagToString(String userName) {
        if (userName != null) {
            return "@" + userName;
        } else {
            return "";
        }
    }
}
