package com.hongan.template.external.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.BaseCrypto;
import com.hongan.template.base.utils.RequestUtil;
import com.hongan.template.base.utils.StringUtils;
import com.hongan.template.external.service.IBaiduTranslateService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BaiduTranslateServiceImpl implements IBaiduTranslateService {
    @Override
    public List<String> translate(String from, String to, String query) throws BaseException, IOException, InterruptedException {
        if (StringUtils.isEmpty(query)) throw new BaseException(BaseError.BadRequest);
        if (StringUtils.isEmpty(from)) from = "en";
        if (StringUtils.isEmpty(to)) to = "zh";

        //请求URL
        String URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        //appId
        String appId = "20221115001453832";
        //密钥
        String secretKey = "eQ_Lef8rC4pn2r9oGEox";
        //盐值
        String salt = System.currentTimeMillis() + "";
        String sign = BaseCrypto.MD5(appId + query + salt + secretKey).toLowerCase();
        String finalFrom = from;
        String finalTo = to;
        String resp = RequestUtil.sendGetRequest(URL, null, new HashMap<>() {{
            put("q", query);
            put("from", finalFrom);
            put("to", finalTo);
            put("appid", appId);
            put("salt", salt);
            put("sign", sign);
        }});
        JSONObject res = JSONObject.parseObject(resp);
        JSONArray jsonArray = res.getJSONArray("trans_result");
        List<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            result.add(jsonObject.getString("dst"));
//            result.put(jsonObject.getString("src"), jsonObject.getString("dst"));
        }
//        JSONObject jsonObject = jsonArray.getJSONObject(0);
//        return jsonObject.getString("dst");
        return result;
    }
}
