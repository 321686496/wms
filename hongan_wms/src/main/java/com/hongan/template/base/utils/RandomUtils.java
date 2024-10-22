package com.hongan.template.base.utils;

import java.util.Random;

/**
 * 随机数工具
 *
 * @Author: zyp
 * @Date: 2021/11/19 0019
 */

public class RandomUtils {
    public static int[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static Random random = new Random();

    /**
     * 生成随机数字
     *
     * @param length 随机数长度
     * @return
     */
    public static String randomNumber(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(10);
            builder.append(nums[index]);
        }
        return builder.toString();
    }

    public static String getRandomChar(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(36)]);
        }
        return buffer.toString();
    }
}
