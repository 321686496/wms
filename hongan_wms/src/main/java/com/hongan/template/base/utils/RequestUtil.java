package com.hongan.template.base.utils;

import com.alibaba.fastjson.JSONObject;
import com.hongan.template.base.entity.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: zyp
 * @Date: 2022/7/1 0001
 */
public class RequestUtil {

    /**
     * 发送get请求
     *
     * @param url     请求URL
     * @param headers 需要携带的请求头
     * @param param   需要携带的url参数
     * @return 请求结果
     */
    public static String sendGetRequest(String url, Map<String, String> headers, Map<String, String> param) throws IOException, InterruptedException {
        String res = "";
        String URL = formatUrlParam(url, param);
        // 要访问的目标页面
        HttpGet httpGet = new HttpGet(URL);

        // 设置请求头
        setHeaders(httpGet, headers);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 执行请求
        CloseableHttpResponse httpResp = httpClient.execute(httpGet);
        HttpEntity entity = httpResp.getEntity();
        if (entity != null) {
            res = EntityUtils.toString(entity, "utf-8");
        }
        httpResp.close();
        httpClient.close();
        httpGet.abort();
        return res;
    }

    /**
     * 发送post请求
     *
     * @param url     请求URL
     * @param headers 需要携带的请求头
     * @param param   需要携带的url参数
     * @param body    需要携带的请求体参数
     * @return 请求结果
     */
    public static String sendPostRequest(String url, Map<String, String> headers, Map<String, String> param, JSONObject body) throws BaseException {
        try {
            String result = "";
            //设置表单参数
            url = formatUrlParam(url, param);

            //要访问的目标URl
            HttpPost httpPost = new HttpPost(url);
            //装填参数
            if (body != null) {
                StringEntity s = new StringEntity(body.toString(), "utf-8");
                s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                //设置参数到请求对象中
                httpPost.setEntity(s);
            }
//
//            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(paramsAdapter(formParams), "utf-8");
//            httpPost.setEntity(uefEntity);
            // 设置请求头
            setHeaders(httpPost, headers);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 执行请求
            CloseableHttpResponse httpResp = httpClient.execute(httpPost);
            HttpEntity entity = httpResp.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            httpResp.close();
            httpClient.close();
            httpPost.abort();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(20100, "请求失败");
        }
    }

    /**
     * 将query格式化到url中   url?a=1&b=1
     *
     * @param url    请求URL
     * @param params 请求中URL需要携带参数
     * @return
     */
    private static String formatUrlParam(String url, Map<String, String> params) {
        if (params == null || params.size() < 1) return url;
        Iterator<String> it = params.keySet().iterator();
        StringBuilder paramStr = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            String value = params.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            // URL 编码
            value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            paramStr.append("&").append(key).append("=").append(value);
        }
        // 去掉第一个&
        String substring = paramStr.substring(1);
        return url + "?" + substring;
    }


    /**
     * 参数适配器，将系统定义的请求参数转换成HttpClient能够接受的参数类型
     *
     * @param map 系统定义的请求参数
     * @return HttpClient要求的参数类型
     */
    private static List<NameValuePair> paramsAdapter(Map<String, String> map) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nvps;
    }


    /**
     * 设置请求头
     *
     * @param httpReq
     */
    private static void setHeaders(HttpRequestBase httpReq, Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
                httpReq.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }
    }
}
