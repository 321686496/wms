package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.enums.PrintWorkStatus;
import com.hongan.template.enums.PrintPaperSize;

import java.io.UnsupportedEncodingException;
import java.util.List;

//飞鹅云服务接口
public interface IFeieyunService {

    /**
     * 添加打印机
     *
     * @param printSn     打印机编号
     * @param printKey    打印机K密钥
     * @param printRemark 打印机备注
     */
    void addPrint(String printSn, String printKey, String printRemark) throws BaseException;

    /**
     * 删除打印机
     *
     * @param printSn 打印机列表
     */
    void delPrint(String printSn) throws BaseException;

    /**
     * 查询打印机状态
     *
     * @param sn
     * @return
     * @throws BaseException
     */
    PrintWorkStatus queryPrintStatus(String sn) throws BaseException;

    //打印纸张尺寸
    void setPrintSize(String sn, PrintPaperSize paperSize) throws BaseException;


    /**
     * 打印
     *
     * @param sn
     */
    void print3020(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException;

    void print3020Sn(String sn,Integer count,Integer startNumber,  String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException;

    void print3020RandomSn(String sn,Integer count,Integer startNumber,  String materialName, String spec, List<String> snList) throws BaseException, UnsupportedEncodingException;


    /**
     * 打印
     *
     * @param sn
     */
    void print4030(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException;

    void print4030Sn(String sn,Integer count,Integer startNumber,  String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException;

    void print4030RandomSn(String sn,Integer count,Integer startNumber,  String materialName, String spec, List<String> snList) throws BaseException, UnsupportedEncodingException;

    void print6040(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException;

    void print6040Sn(String sn, Integer count,Integer startNumber,  String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException;

    void print6040RandomSn(String sn,Integer count,Integer startNumber,  String materialName, String spec, List<String> snList) throws BaseException, UnsupportedEncodingException;

    /**
     * 打印
     *
     * @param sn
     */
    void print6080(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException;

    void printByContent(String sn, Integer count, String content) throws BaseException, UnsupportedEncodingException;

    String getCenterText(String str, Integer length) throws UnsupportedEncodingException;
}
