package com.hongan.template.external.vo.wechatSdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class WXRefundPayConfig extends WXPayConfig {
    //测试环境
//    String certPath = "F:\\work\\mall_xj\\src\\main\\resources\\apiclient_cert.p12";
    //生产环境
    String str = "/data/mall_qz/server/cert/apiclient_cert.p12";


    @Override
    String getAppID() {
        return "wx7a50dc83d9b6feac";
    }

    @Override
    String getMchID() {
        return "1562075301";
    }

    @Override
    String getKey() {
        return "GzgwvOcToJNcxHCBXmKHNzNjhnyLDmDq";
    }

    @Override
    InputStream getCertStream() throws FileNotFoundException {
        File file = new File(str);
        FileInputStream instream = new FileInputStream(file);
        return instream;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        IWXPayDomainImpl iwxPayDomain = new IWXPayDomainImpl();
        return iwxPayDomain;
    }
}
