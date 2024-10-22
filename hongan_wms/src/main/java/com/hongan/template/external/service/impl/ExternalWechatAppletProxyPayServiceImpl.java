package com.hongan.template.external.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.service.IExternalWechatAppletProxyPayService;
import com.hongan.template.external.vo.CreateSign02;
import com.hongan.template.external.vo.RefundOrderVo;
import com.hongan.template.external.vo.UnifiedOrderVo;
import com.hongan.template.external.vo.WechatMiniAppVo;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.partnerpayments.jsapi.JsapiService;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 系统微信支付配置表 服务实现类
 * </p>
 * 服务商支付(小程序)
 *
 * @author Administrator
 * @since 2023-04-19
 */
@Slf4j
@Service
public class ExternalWechatAppletProxyPayServiceImpl implements IExternalWechatAppletProxyPayService {
    @Autowired
    private IHonganConfigService configService;
    @Value("${spring.profiles.active}")
    private String path;

    static Map<String, String> systemConfig;

    //利用@PostConstruct将application中配置的值赋给本地的变量
    @PostConstruct
    public void getServelDate() {
        systemConfig = configService.getValues(new ArrayList<String>() {{
            add("system_domain");
            add("external_wechat_applet_appid");
            add("external_wechat_applet_secret");
            add("external_wechat_proxyPay_mchid");
            add("external_wechat_proxyPay_secret");
            add("external_wechat_proxyPay_certPath");
            add("external_wechat_proxyPay_spAppid");
            add("external_wechat_proxyPay_spMchid");
            add("external_wechat_proxyPay_spV3Secret");
            add("external_wechat_proxyPay_spPrivateKeyPath");
            add("external_wechat_proxyPay_spSerialNumber");
            add("external_wechat_pay_notifyUrl");
        }});
    }

    //配置对象需要为单例模式
    private static Config config;
    public static JsapiService service;
    public static RefundService refundService;

    /**
     * 唯一公开获取实例的方法（静态工厂方法），该方法使用synchronized加锁，来保证线程安全性
     *
     * @return
     */
    public static synchronized Config getInstance() {
        if (config == null) {
            config = new RSAAutoCertificateConfig.Builder().merchantId(systemConfig.get("external_wechat_proxyPay_spMchid"))
                    .privateKeyFromPath(systemConfig.get("external_wechat_proxyPay_spPrivateKeyPath"))
                    .merchantSerialNumber(systemConfig.get("external_wechat_proxyPay_spSerialNumber"))
                    .apiV3Key(systemConfig.get("external_wechat_proxyPay_spV3Secret"))
                    .build();
        }
        log.info("config:{}", config);
        return config;
    }


    @Override
    public Object unifiedPay(UnifiedOrderVo vo) throws BaseException {
        // 初始化服务
        service = new JsapiService.Builder().config(getInstance()).build();

        //构造Jsapi下单参数
        com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayRequest request = new com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayRequest();
        com.wechat.pay.java.service.partnerpayments.jsapi.model.Amount amount = new com.wechat.pay.java.service.partnerpayments.jsapi.model.Amount();
        amount.setTotal(Integer.parseInt(vo.getAmount()));
        request.setAmount(amount);
        request.setSpAppid(systemConfig.get("external_wechat_proxyPay_spAppid"));
        request.setSpMchid(systemConfig.get("external_wechat_proxyPay_spMchid"));
        request.setSubAppid(systemConfig.get("external_wechat_applet_appid"));
        request.setSubMchid(systemConfig.get("external_wechat_proxyPay_mchid"));
        request.setDescription(vo.getBody());
        request.setNotifyUrl(systemConfig.get("system_domain") + systemConfig.get("external_wechat_proxyPay_notifyUrl"));
        request.setOutTradeNo(vo.getOrderNo());
        com.wechat.pay.java.service.partnerpayments.jsapi.model.Payer payer = new com.wechat.pay.java.service.partnerpayments.jsapi.model.Payer();
        payer.setSubOpenid(vo.getOpenid());
        request.setPayer(payer);
        request.setAttach(vo.getOrderMark());
        log.info("下单参数：{}", request.toString());
        // 调用接口
        PrepayResponse prepay = service.prepay(request);
        return buildMiniPrePay(prepay);

    }

    private WechatMiniAppVo buildMiniPrePay(PrepayResponse prepay) throws BaseException {
        WechatMiniAppVo vo = new WechatMiniAppVo();
        vo.setAppId(systemConfig.get("external_wechat_applet_appid"));
        vo.setNonceStr(RandomStringUtils.randomAlphanumeric(8));
        vo.setPackageInfo("prepay_id=" + prepay.getPrepayId());
        vo.setSignType("RSA");
        Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        vo.setTimeStamp(second + "");
        try {
//            appParams.put("paySign", WXPayUtil.generateSignature(appParams, "your_key"));
            //使用特定的签名方式 需要服务商的私钥
            vo.setPaySign(CreateSign02.getToken(vo, systemConfig.get("external_wechat_proxyPay_spPrivateKeyPath")));
//            vo.setPaySign(WXPayUtil.generateSignature(vo, wechatPay.getSpPrivateKeyPath()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("签名错误");
        }
        log.debug("微信支付返回信息:{}", vo);
        return vo;
    }


    @Override
    public Map<String, String> payNotify(HttpServletRequest request) throws BaseException {
        //获取报文
        String body = getRequestBody(request);
        Map<String, String> map = new HashMap<>(2);
        try {
            //解密微信返回的数据
            String plainBody = decryptBody(body);
            Map<String, Object> res = JSONObject.parseObject(plainBody);
            log.info("Wechat Notify :: {}", res);
            //处理业务逻辑 TODO
            String orderNo = res.get("out_trade_no").toString();
            switch (res.get("attach").toString()) {
                // 商品支付成功回调
                case "order":
                    break;
            }
            //响应微信
            map.put("code", "SUCCESS");
            map.put("message", "成功");
        } catch (Exception e) {
            log.error("微信支付回调异常:{}", e);
            map.put("code", "FAIL");
            map.put("message", "失败");
        }
        return map;
    }

    /**
     * 解密body的密文
     *
     * @param body
     * @return
     */
    private String decryptBody(String body) throws UnsupportedEncodingException, GeneralSecurityException {
        AesUtil aesUtil = new AesUtil(systemConfig.get("external_wechat_proxyPay_spV3Secret").getBytes("utf-8"));
        JSONObject object = JSONObject.parseObject(body);
        JSONObject resource = object.getJSONObject("resource");
        String ciphertext = resource.getString("ciphertext");
        String associatedData = resource.getString("associated_data");
        String nonce = resource.getString("nonce");
        return aesUtil.decryptToString(associatedData.getBytes("utf-8"), nonce.getBytes("utf-8"), ciphertext);
    }

    /**
     * 读取请求数据流
     *
     * @param request
     * @return
     */
    private String getRequestBody(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        try (
                ServletInputStream inputStream = request.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("读取数据流异常:{}", e);
        }
        return sb.toString();
    }

    @Override
    public Boolean wechatRefund(RefundOrderVo vo) throws BaseException {
        try {
            refundService = new RefundService.Builder().config(getInstance()).build();
            //构造退款参数
            CreateRequest createRequest = new CreateRequest();
            createRequest.setSubMchid(systemConfig.get("external_wechat_proxyPay_mchid"));
            createRequest.setOutTradeNo(vo.getOrderNo());
            createRequest.setOutRefundNo(vo.getOutRefundNo());
            createRequest.setReason(vo.getReason());
            AmountReq amount = new AmountReq();
            amount.setTotal(Long.parseLong(vo.getAmount()));
            amount.setRefund(Long.parseLong(vo.getRefundFee()));
            amount.setCurrency("CNY");
            createRequest.setAmount(amount);
            Refund refundRes = refundService.create(createRequest);
            log.info("退款结果res=>{}", refundRes);
            return refundRes.getStatus() != Status.ABNORMAL;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
