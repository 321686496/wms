package com.hongan.template.external.controller;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.service.IUnifiedPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付回调通知
 */
@RestController
@RequestMapping("/base/api/notify")
public class NotifyController {
    @Autowired
    private IUnifiedPayService unifiedPayService;


    @RequestMapping("/wechat")
    public Map<String, String> payNotify(HttpServletRequest request) throws BaseException {
        return unifiedPayService.payNotify(request);
    }


}
