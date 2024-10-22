package com.hongan.template;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.BaseCrypto;
import com.hongan.template.base.utils.RequestUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TranslateDemo {

    @Test
    public void test() throws IOException, InterruptedException, BaseException {
        //请求URL
        String URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        //appId
        String appId = "20221115001453832";
        //密钥
        String secretKey = "eQ_Lef8rC4pn2r9oGEox";
        //翻译内容
        String query = "apple | banana";
        //盐值
        String salt = System.currentTimeMillis() + "";

        String from = "en";
        String to = "zh";
        String sign = BaseCrypto.MD5(appId + query + salt + secretKey).toLowerCase();
        String resp = RequestUtil.sendGetRequest(URL, null, new HashMap<>() {{
            put("q", query);
            put("from", from);
            put("to", to);
            put("appid", appId);
            put("salt", salt);
            put("sign", sign);
        }});
        JSONObject res = JSONObject.parseObject(resp);
        JSONArray jsonArray = res.getJSONArray("trans_result");
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            result.put(jsonObject.getString("src"), jsonObject.getString("dst"));
        }
        System.out.println(result);
    }

    @Test
    public void test1() {
    }
}
