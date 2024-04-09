package com.chw.utils;

import java.util.List;

public class StringUtils {

    public static String firstUpper(String str) {
        if (null == str) {
            return null;
        }
        else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    public static boolean isEmpty(String str) {
        return null == str || "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String join(List<String> list, String sep) {
        Integer size = list.size();
        String res = "";
        for (int i = 0; i < size; i++) {
            if (i != (size - 1)) {
                res += list.get(i) + sep;
            }
            else {
                res += list.get(i);
            }
        }
        return res;
    }


    public static void main(String[] args) {

        System.out.println(isEmpty(null));

    }

}
