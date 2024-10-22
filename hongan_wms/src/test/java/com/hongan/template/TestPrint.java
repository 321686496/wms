package com.hongan.template;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
public class TestPrint {

    @Test
    public void test() {

        Map<String, Integer> map = new HashMap<>();


        Integer lotteryCount = 10000;
        for (int i = 0; i < lotteryCount; i++) {
            String lottery = lottery();
            Integer integer = map.get(lottery);
            if (integer == null) {
                map.put(lottery, 1);
            } else {
                map.put(lottery, integer + 1);
            }
        }
        System.out.println(map);
    }

    private String lottery() {
        Map<String, Double> lotteryMap = new HashMap<>();
        lotteryMap.put("奖品1", 0.01);
        lotteryMap.put("奖品9", 0.09);
        lotteryMap.put("奖品20", 0.2);
        lotteryMap.put("奖品30", 0.3);
        lotteryMap.put("奖品40", 0.4);
        lotteryMap.put("奖品90", 0.9);
//        lotteryMap.put("奖品50", 0.5);
//        lotteryMap.put("奖品60", 0.6);

        double totalProbability = lotteryMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double cumulativeProbability = 0.0;
        Map<String, Double> cumulativeProbabilityMap = new HashMap<>();
        for (Map.Entry<String, Double> entry : lotteryMap.entrySet()) {
            cumulativeProbability += entry.getValue() / totalProbability;
            cumulativeProbabilityMap.put(entry.getKey(), cumulativeProbability);
        }

        double random = Math.random();
        String result = null;
        for (Map.Entry<String, Double> entry : cumulativeProbabilityMap.entrySet()) {
            if (random < entry.getValue()) {
                result = entry.getKey();
                break;
            }
        }
        return result;
    }


}
