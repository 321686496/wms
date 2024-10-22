package com.hongan.template.base.utils;

import java.util.Random;

/**
 * @author zyp
 * @version 1.0
 * @date 2020/8/29 17:42
 * @description
 */
public class RandomNum {
    public static String getRandomNickname(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
}
