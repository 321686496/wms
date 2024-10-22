package com.hongan.template.base.utils;

public class PlUtils {

    public static String hideNumPhone(String phone, int num) {
        int l = phone.length();
        int start = (l - num) / 2;
        StringBuilder builder = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            if (i < start || i >= start + num) {
                builder.append(phone.charAt(i));
            } else {
                builder.append('*');
            }
        }
        return builder.toString();
    }
}
