package com.hongan.template.base.utils;

import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DataCheckUtils {


    /**
     * 校验参数是否符合格式
     *
     * @param param    参数
     * @param regex    正则表达式
     * @param errorMsg 错误信息
     * @throws BaseException
     */
    public static void checkParamRegex(String param, String regex, BaseError errorMsg) throws BaseException {
        if (StringUtils.isEmpty(param) || StringUtils.isEmpty(regex)) throw new BaseException(BaseError.BadRequest);
        boolean res = param.matches(regex);
        if (!res) {
            throw new BaseException(errorMsg);
        }
    }

    /**
     * 校验数据是否为空
     *
     * @param data 数据
     * @throws BaseException
     */
    public static void checkData(Object data) throws BaseException {
        if (data == null)
            throw new BaseException(BaseError.DataNotExist);
    }

    public static void checkData(Object data, BaseError errorMsg) throws BaseException {
        if (data == null)
            throw new BaseException(errorMsg);
    }

    /**
     * 校验参数
     *
     * @param str      值
     * @param errorMsg 提醒的信息
     */
    public static void checkParam(String str, BaseError errorMsg) throws BaseException {
        if (StringUtils.isEmpty(str))
            throw new BaseException(errorMsg);
    }

    /**
     * 校验参数
     *
     * @param param    参数值
     * @param errorMsg 错误信息
     */
    public static void checkParam(Long param, BaseError errorMsg) throws BaseException {
        if (param == null || param < 1)
            throw new BaseException(errorMsg);
    }

    /**
     * 校验参数
     *
     * @param param    参数值
     * @param errorMsg 错误信息
     */
    public static void checkParam(Integer param, String errorMsg) throws BaseException {
        if (param == null || param < 1)
            throw new BaseException(errorMsg);
    }

    /**
     * 校验参数
     *
     * @param param    参数值
     * @param errorMsg 错误信息
     */
    public static void checkParam(Object param, String errorMsg) throws BaseException {
        if (param == null)
            throw new BaseException(errorMsg);
    }

    /**
     * 校验参数
     *
     * @param param    参数值
     * @param errorMsg 错误信息
     */
    public static void checkParam(List<Long> param, String errorMsg) throws BaseException {
        if (param == null || param.size() < 1)
            throw new BaseException(errorMsg);
    }

    /**
     * 校验佣金比例
     *
     * @param proportion 比例值
     * @param errorMsg   提醒的信息
     */
    public static void checkProportion(BigDecimal proportion, String errorMsg) throws BaseException {
        if (proportion == null)
            throw new BaseException("请填写" + errorMsg + "！");
        if (proportion.compareTo(BigDecimal.ZERO) < 0 || proportion.compareTo(new BigDecimal(100)) > 0)
            throw new BaseException(errorMsg + "在0~100之间！");
    }

    /**
     * 校验金额是否合法
     *
     * @param amount   比例值
     * @param errorMsg 提醒的信息
     */
    public static void checkAmount(BigDecimal amount, String errorMsg) throws BaseException {
        if (amount == null)
            throw new BaseException("请填写" + errorMsg + "！");
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new BaseException(errorMsg + "不能小于0！");
    }

    /**
     * 校验经度是否合法
     *
     * @param required  是否必填
     * @param longitude 经度坐标
     * @throws BaseException
     */
    public static void checkLongitude(Boolean required, BigDecimal longitude) throws BaseException {
        if (required) {
            if (longitude == null)
                throw new BaseException("经度坐标为空！");
        }
        if (longitude != null) {
            if (new BigDecimal(180).compareTo(longitude) < 1 || new BigDecimal(-180).compareTo(longitude) > -1)
                throw new BaseException("经度坐标不正确！");
        }
    }

    /**
     * 校验纬度是否合法
     *
     * @param required 是否必填
     * @param latitude 纬度坐标
     * @throws BaseException
     */
    public static void checkLatitude(Boolean required, BigDecimal latitude) throws BaseException {
        if (required) {
            if (latitude == null)
                throw new BaseException("纬度坐标为空！");
        }
        if (latitude != null) {
            if (new BigDecimal(90).compareTo(latitude) < 1 || new BigDecimal(-90).compareTo(latitude) > -1)
                throw new BaseException("纬度坐标不正确！");
        }
    }

    /**
     * 计算两点间的距离
     *
     * @param startLongitude 起点坐标(经度)
     * @param startLatitude  起点坐标(纬度)
     * @param endLongitude   终点坐标(经度)
     * @param endLatitude    终点坐标(纬度)
     * @return 距离(km)
     * @throws BaseException
     */
    public static BigDecimal completeDistance(BigDecimal startLongitude, BigDecimal startLatitude, BigDecimal endLongitude, BigDecimal endLatitude) throws BaseException {
        if (startLongitude == null || startLatitude == null || endLongitude == null || endLatitude == null)
            return BigDecimal.ZERO;
        checkLongitude(true, startLongitude);
        checkLatitude(true, startLatitude);
        checkLongitude(true, endLongitude);
        checkLatitude(true, endLatitude);

        //给定两个坐标系,计算两点相差距离
        GlobalCoordinates source = new GlobalCoordinates(startLatitude.doubleValue(), startLongitude.doubleValue());
        GlobalCoordinates target = new GlobalCoordinates(endLatitude.doubleValue(), endLongitude.doubleValue());
        //Sphere坐标的计算结果 距离
        //创建GeodeticCalculator,调用计算方法,传入坐标系,经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, source, target);
        double distance = geoCurve.getEllipsoidalDistance();
        return new BigDecimal(distance).divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP);
    }

    /**
     * 计算两点间的距离(米)
     *
     * @param startLongitude 起点坐标(经度)
     * @param startLatitude  起点坐标(纬度)
     * @param endLongitude   终点坐标(经度)
     * @param endLatitude    终点坐标(纬度)
     * @return 距离(km)
     * @throws BaseException
     */
    public static BigDecimal completeDistanceRice(BigDecimal startLongitude, BigDecimal startLatitude, BigDecimal endLongitude, BigDecimal endLatitude) throws BaseException {
        if (startLongitude == null || startLatitude == null || endLongitude == null || endLatitude == null)
            return BigDecimal.ZERO;
        checkLongitude(true, startLongitude);
        checkLatitude(true, startLatitude);
        checkLongitude(true, endLongitude);
        checkLatitude(true, endLatitude);

        //给定两个坐标系,计算两点相差距离
        GlobalCoordinates source = new GlobalCoordinates(startLatitude.doubleValue(), startLongitude.doubleValue());
        GlobalCoordinates target = new GlobalCoordinates(endLatitude.doubleValue(), endLongitude.doubleValue());
        //Sphere坐标的计算结果 距离
        //创建GeodeticCalculator,调用计算方法,传入坐标系,经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, source, target);
        double distance = geoCurve.getEllipsoidalDistance();
        return new BigDecimal(distance);
    }


    /**
     * 根据佣金比例计算佣金
     *
     * @param amount     金额
     * @param proportion 佣金比例(%) 最大值为100
     * @return
     * @throws BaseException
     */
    public static BigDecimal completeAmount(BigDecimal amount, BigDecimal proportion) throws BaseException {
        if (amount == null || proportion == null) return BigDecimal.ZERO;
//        if (amount.compareTo(BigDecimal.ZERO) < 1) return BigDecimal.ZERO;
        if (proportion.compareTo(BigDecimal.ZERO) < 1) return BigDecimal.ZERO;
//        if (proportion.compareTo(new BigDecimal(100)) > -1) return amount;
        //佣金 = 金额 * (佣金比例 / 100)
        return amount.multiply(proportion.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal completeByProportion(BigDecimal amount, BigDecimal proportion) throws BaseException {
        if (amount == null || proportion == null) return BigDecimal.ZERO;
//        if (amount.compareTo(BigDecimal.ZERO) < 1) return BigDecimal.ZERO;
        if (proportion.compareTo(BigDecimal.ZERO) < 1) return BigDecimal.ZERO;
//        if (proportion.compareTo(new BigDecimal(100)) > -1) return amount;
        //佣金 = 金额 * (佣金比例 / 100)
        return amount.multiply(proportion.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 根据金额计算比例
     *
     * @throws BaseException
     */
    public static BigDecimal completeProportion(BigDecimal data, BigDecimal data2) throws BaseException {
        if (data == null || data2 == null) return BigDecimal.ZERO;
        if (data.compareTo(BigDecimal.ZERO) == 0 || data2.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return data.divide(data2, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal completeProportion(Integer intData, Integer intData2) throws BaseException {
        BigDecimal data = new BigDecimal(intData);
        BigDecimal data2 = new BigDecimal(intData2);
        if (data == null || data2 == null) return BigDecimal.ZERO;
        if (data.compareTo(BigDecimal.ZERO) == 0 || data2.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return data.divide(data2, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
    }
    /**
     * 金额相乘数值
     *
     * @param data  数据1
     * @param data2 数据2
     * @param scale 保留位数
     * @return
     */
//    public static BigDecimal bigDecimalMultiply(BigDecimal data, BigDecimal data2, Integer scale) {
//        if (scale == null || scale < 0) scale = 0;
//        if (data == null || data2 == null) return BigDecimal.ZERO;
//        return data.multiply(data2).setScale(scale, RoundingMode.HALF_UP);
//    }

    /**
     * 金额相乘数值(保留两位小数)(一般用于金额)
     */
//    public static BigDecimal bigDecimalMultiply2(BigDecimal data, BigDecimal data2) {
//        if (data == null || data2 == null) return BigDecimal.ZERO;
//        return data.multiply(data2).setScale(2, RoundingMode.HALF_UP);
//    }

    /**
     * 金额相乘数值(保留三位小数)(一般用于重量，数量)
     */
//    public static BigDecimal bigDecimalMultiply3(BigDecimal data, BigDecimal data2) {
//        if (data == null || data2 == null) return BigDecimal.ZERO;
//        return data.multiply(data2).setScale(3, RoundingMode.HALF_UP);
//    }

    /**
     * 数值相除
     *
     * @param data  数据1
     * @param data2 数据2
     * @param scale 保留位数
     * @return
     */
//    public static BigDecimal bigDecimalDivide(BigDecimal data, BigDecimal data2, Integer scale) {
//        if (data == null || data2 == null) return BigDecimal.ZERO;
//        if (data.compareTo(BigDecimal.ZERO) == 0 || data2.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
//        return data.divide(data2, scale, RoundingMode.HALF_UP);
//    }

    /**
     * 数值相除
     */
//    public static BigDecimal bigDecimalDivide2(BigDecimal data, BigDecimal data2) {
//        if (data == null || data2 == null) return BigDecimal.ZERO;
//        if (data.compareTo(BigDecimal.ZERO) == 0 || data2.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
//        return data.divide(data2, 2, RoundingMode.HALF_UP);
//    }


    /**
     * 数值相除
     */
//    public static BigDecimal bigDecimalDivide3(BigDecimal data, BigDecimal data2) {
//        if (data == null || data2 == null) return BigDecimal.ZERO;
//        if (data.compareTo(BigDecimal.ZERO) == 0 || data2.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
//        return data.divide(data2, 3, RoundingMode.HALF_UP);
//    }


}
