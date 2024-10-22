package com.hongan.template.base.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    //根据两个日期获取区间内时间
    public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= numOfDays; i++) {
            LocalDate date = startDate.plusDays(i);
            dateList.add(date);
        }
        return dateList;
    }
}
