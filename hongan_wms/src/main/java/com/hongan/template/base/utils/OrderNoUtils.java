package com.hongan.template.base.utils;


import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单号生成器
 *
 * @Author: zyp
 * @Date: 2021/11/19 0019
 */
public class OrderNoUtils {
    private int size;
    private int length;
    private List<String> orderNoList;
    private Set<String> orderNoSet;


    /**
     * 订单号生成器
     * <p>
     * 为了保证生成性能需满足条件size<10^length/4
     *
     * @param size   保证连续不重读的数
     * @param length 生成随机数的长度
     */
    public OrderNoUtils(int size, int length) {
        this.size = size;
        this.length = length;
        requireTrue(size < Math.pow(10, length) / 4, "参数不符合要求");
        orderNoList = new ArrayList<>();
        orderNoSet = new HashSet<>();
    }

    public static void requireTrue(boolean value, String msg) {
        if (!value) {
            throw new RuntimeException(msg);
        }
    }

    public static String getNo() {
        OrderNoUtils orderNoUtils = new OrderNoUtils(1000, 4);
        return orderNoUtils.generatorOrderNo();
    }

    /**
     * 获取不重复的随机数
     *
     * @return
     */
    public synchronized String generatorOrderNo() {
        String randomNumber = RandomUtils.randomNumber(length);
        while (orderNoSet.contains(randomNumber)) {
            randomNumber = RandomUtils.randomNumber(length);
        }
        orderNoList.add(randomNumber);
        orderNoSet.add(randomNumber);
        reduce();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date()) + randomNumber;
    }

    private void reduce() {
        if (orderNoList.size() >= size * 2) {
            List<String> removes = orderNoList.subList(0, size);
            orderNoSet.removeAll(removes);
            removes.clear();
        }
    }
}
