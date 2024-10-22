package com.hongan.template.external.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.enums.PrintWorkStatus;
import com.hongan.template.enums.PrintPaperSize;
import com.hongan.template.external.service.IFeieyunService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FeieyunServiceImpl implements IFeieyunService {
    //飞鹅云URL
    static String URL = "http://api.feieyun.cn/Api/Open/";//不需要修改

    @Autowired
    private IHonganConfigService systemConfigService;

    @Override
    public void addPrint(String printSn, String printKey, String printRemark) throws BaseException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("apiname", "Open_printerAddlist"));//接口名称 固定值,不需要修改
        //以下为业务参数
        //打印机编号(必填) # 打印机识别码(必填) # 备注名称(选填) # 流量卡号码(选填)
        nvps.add(new BasicNameValuePair("printerContent", printSn + "#" + printKey + "#" + printRemark + "#"));
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {
            Map<String, Object> data = (Map<String, Object>) jsonObject.get("data");
            JSONArray no = (JSONArray) data.get("no");
            if (no.size() > 0) {
                String o = (String) no.get(0);
                if (o.contains("打印机编号不合法")) {
                    throw new BaseException(BaseError.PrinterSnError);
                } else if (o.contains("打印机编号和KEY不正确")) {
                    throw new BaseException(BaseError.PrinterSnKeyError);
                } else if (o.contains("已被添加过")) {
                    throw new BaseException(BaseError.PrinterHasBeenAdded);
                }
            }
        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public void delPrint(String printSn) throws BaseException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_printerDelList"));//接口名称 固定值,不需要修改
        //打印机编号，多台打印机请用减号“-”连接起来。
        nvps.add(new BasicNameValuePair("snlist", printSn));
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {
            Map<String, Object> data = (Map<String, Object>) jsonObject.get("data");
            JSONArray no = (JSONArray) data.get("no");
            if (no.size() > 0) {
                throw new BaseException(BaseError.DeleteError);
            }
        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public PrintWorkStatus queryPrintStatus(String sn) throws BaseException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_queryPrinterStatus"));//接口名称 固定值,不需要修改

        //打印机编号，多台打印机请用减号“-”连接起来。
        nvps.add(new BasicNameValuePair("sn", sn));
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {
            String status = (String) jsonObject.get("data");
            if (status.contains("离线")) {
                return PrintWorkStatus.OffLine;
            } else if (status.contains("工作状态正常")) {
                return PrintWorkStatus.Normal;
            } else {
                return PrintWorkStatus.Exception;
            }

        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public void setPrintSize(String sn, PrintPaperSize paperSize) throws BaseException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
        String content = "";
        if (paperSize == PrintPaperSize.Size3020) {
            content = "<DIRECTION>1</DIRECTION> <SIZE>30,20</SIZE>";
        } else if (paperSize == PrintPaperSize.Size4030) {
            content = "<DIRECTION>1</DIRECTION> <SIZE>40,30</SIZE>";
        } else if (paperSize == PrintPaperSize.Size6040) {
            content = "<DIRECTION>1</DIRECTION> <SIZE>60,40</SIZE>";
        } else if (paperSize == PrintPaperSize.Size6080) {
            content = "<DIRECTION>1</DIRECTION> <SIZE>60,80</SIZE>";
        } else {
            return;
        }
        nvps.add(new BasicNameValuePair("content", content));//打印内容
        nvps.add(new BasicNameValuePair("times", 1 + ""));//打印数量
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {

        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }


    @Override
    public void print3020(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
        String content = String.format("<DIRECTION>1</DIRECTION>" +
                "<BC128 x=\"10\" y=\"10\" h=\"50\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                "<TEXT x=\"10\" y=\"70\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                "<TEXT x=\"10\" y=\"100\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                "<TEXT x=\"10\" y=\"130\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>", code, code, materialName, spec);
        nvps.add(new BasicNameValuePair("content", content));//打印内容
        nvps.add(new BasicNameValuePair("times", count + ""));//打印数量
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {

        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public void print3020Sn(String sn, Integer count, Integer startNumber, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException {
        for (int i = 0; i < count; i++) {
            Integer startNum = startNumber + i;
            String printCode = code + "-" + startNum;
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
            String content = String.format("<DIRECTION>1</DIRECTION>" +
                    "<BC128 x=\"10\" y=\"10\" h=\"50\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                    "<TEXT x=\"10\" y=\"70\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"10\" y=\"100\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"10\" y=\"130\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>", printCode, printCode, materialName, spec);
            nvps.add(new BasicNameValuePair("content", content));//打印内容
            nvps.add(new BasicNameValuePair("times", "1"));//打印数量
            String res = callHttp(nvps);
            Map<String, Object> jsonObject = JSONObject.parseObject(res);
            if (jsonObject.get("msg").equals("ok")) {

            } else {
                throw new BaseException(BaseError.FeieyunAPIError);
            }
        }
    }

    @Override
    public void print3020RandomSn(String sn, Integer count, Integer startNumber, String materialName, String spec, List<String> snList) throws BaseException, UnsupportedEncodingException {
        for (String code : snList) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
            String content = String.format("<DIRECTION>1</DIRECTION>" +
                    "<BC128 x=\"10\" y=\"10\" h=\"50\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                    "<TEXT x=\"10\" y=\"70\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"10\" y=\"100\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"10\" y=\"130\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>", code, code, materialName, spec);
            nvps.add(new BasicNameValuePair("content", content));//打印内容
            nvps.add(new BasicNameValuePair("times", "1"));//打印数量
            String res = callHttp(nvps);
            Map<String, Object> jsonObject = JSONObject.parseObject(res);
            if (jsonObject.get("msg").equals("ok")) {
            } else {
                throw new BaseException(BaseError.FeieyunAPIError);
            }
        }
    }

    @Override
    public void print4030(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
        String content = String.format("<DIRECTION>1</DIRECTION>" +
                "<BC128 x=\"15\" y=\"20\" h=\"80\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                "<TEXT x=\"15\" y=\"110\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                "<TEXT x=\"15\" y=\"150\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                "<TEXT x=\"15\" y=\"190\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>", code, code, materialName, spec);
//        String content = String.format("<DIRECTION>1</DIRECTION>" +
//                        "<BC128 x=\"15\" y=\"20\" h=\"80\" s=\"1\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
//                        "<TEXT x=\"15\" y=\"140\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
//                        "<TEXT x=\"15\" y=\"180\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>",
//                code, materialName, spec);
        nvps.add(new BasicNameValuePair("content", content));//打印内容
        nvps.add(new BasicNameValuePair("times", count + ""));//打印数量
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {

        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public void print4030Sn(String sn, Integer count, Integer startNumber, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException {
        for (int i = 0; i < count; i++) {
            Integer startNum = startNumber + i;
            String printCode = code + "-" + startNum;
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
            String content = String.format("<DIRECTION>1</DIRECTION>" +
                    "<BC128 x=\"15\" y=\"20\" h=\"80\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                    "<TEXT x=\"15\" y=\"110\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"15\" y=\"150\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"15\" y=\"190\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>", printCode, printCode, materialName, spec);
            nvps.add(new BasicNameValuePair("content", content));//打印内容
            nvps.add(new BasicNameValuePair("times", "1"));//打印数量
            String res = callHttp(nvps);
            Map<String, Object> jsonObject = JSONObject.parseObject(res);
            if (jsonObject.get("msg").equals("ok")) {

            } else {
                throw new BaseException(BaseError.FeieyunAPIError);
            }
        }
    }


    @Override
    public void print4030RandomSn(String sn, Integer count, Integer startNumber, String materialName, String spec, List<String> snList) throws BaseException, UnsupportedEncodingException {
        for (String code : snList) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
            String content = String.format("<DIRECTION>1</DIRECTION>" +
                    "<BC128 x=\"15\" y=\"20\" h=\"80\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                    "<TEXT x=\"15\" y=\"110\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"15\" y=\"150\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                    "<TEXT x=\"15\" y=\"190\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>", code, code, materialName, spec);
            nvps.add(new BasicNameValuePair("content", content));//打印内容
            nvps.add(new BasicNameValuePair("times", "1"));//打印数量
            String res = callHttp(nvps);
            Map<String, Object> jsonObject = JSONObject.parseObject(res);
            if (jsonObject.get("msg").equals("ok")) {
            } else {
                throw new BaseException(BaseError.FeieyunAPIError);
            }
        }
    }


    @Override
    public void print6040(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
        String content = String.format("<DIRECTION>1</DIRECTION>" +
                        "<BC128 x=\"15\" y=\"20\" h=\"140\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                        "<TEXT x=\"15\" y=\"170\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                        "<TEXT x=\"15\" y=\"210\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                        "<TEXT x=\"15\" y=\"260\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>",
                code, code, materialName, spec);
//        String content = String.format("<DIRECTION>1</DIRECTION>" +
//                        "<BC128 x=\"15\" y=\"20\" h=\"140\" s=\"1\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
//                        "<TEXT x=\"15\" y=\"210\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
//                        "<TEXT x=\"15\" y=\"260\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>",
//                code, materialName, spec);
        nvps.add(new BasicNameValuePair("content", content));//打印内容
        nvps.add(new BasicNameValuePair("times", count + ""));//打印数量
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {

        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public void print6040Sn(String sn, Integer count, Integer startNumber, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException {
        for (int i = 0; i < count; i++) {
            Integer startNum = startNumber + i;
            String printCode = code + "-" + startNum;
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
            String content = String.format("<DIRECTION>1</DIRECTION>" +
                            "<BC128 x=\"15\" y=\"20\" h=\"140\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                            "<TEXT x=\"15\" y=\"170\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                            "<TEXT x=\"15\" y=\"210\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                            "<TEXT x=\"15\" y=\"260\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>",
                    printCode, printCode, materialName, spec);
            nvps.add(new BasicNameValuePair("content", content));//打印内容
            nvps.add(new BasicNameValuePair("times", "1"));//打印数量
            String res = callHttp(nvps);
            Map<String, Object> jsonObject = JSONObject.parseObject(res);
            if (jsonObject.get("msg").equals("ok")) {

            } else {
                throw new BaseException(BaseError.FeieyunAPIError);
            }
        }
    }

    @Override
    public void print6040RandomSn(String sn, Integer count, Integer startNumber, String materialName, String spec, List<String> snList) throws BaseException, UnsupportedEncodingException {
        for (String code : snList) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
            String content = String.format("<DIRECTION>1</DIRECTION>" +
                            "<BC128 x=\"15\" y=\"20\" h=\"140\" s=\"0\" r=\"0\" n=\"2\" w=\"2\">%s</BC128>" +
                            "<TEXT x=\"15\" y=\"170\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                            "<TEXT x=\"15\" y=\"210\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>" +
                            "<TEXT x=\"15\" y=\"260\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">%s</TEXT>",
                    code, code, materialName, spec);
            nvps.add(new BasicNameValuePair("content", content));//打印内容
            nvps.add(new BasicNameValuePair("times", "1"));//打印数量
            String res = callHttp(nvps);
            Map<String, Object> jsonObject = JSONObject.parseObject(res);
            if (jsonObject.get("msg").equals("ok")) {
            } else {
                throw new BaseException(BaseError.FeieyunAPIError);
            }
        }
    }

    @Override
    public void print6080(String sn, Integer count, String materialName, String spec, String code) throws BaseException, UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
        //base版本使用
        String content = "<DIRECTION>1</DIRECTION>" +
                "<BC128 x=\"30\" y=\"170\" h=\"150\" s=\"1\" r=\"0\" n=\"2\" w=\"2\">" + code + "</BC128>" +
                "<TEXT x=\"30\" y=\"370\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">" + materialName + "</TEXT>" +
                "<TEXT x=\"30\" y=\"420\" font=\"12\" w=\"1\" h=\"1\" r=\"0\">" + spec + "</TEXT>";
        nvps.add(new BasicNameValuePair("content", content));//打印内容
        nvps.add(new BasicNameValuePair("times", count + ""));//打印数量
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {
        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public void printByContent(String sn, Integer count, String content) throws BaseException, UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//接口名称 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));//接口名称 固定值,不需要修改
        nvps.add(new BasicNameValuePair("content", content));//打印内容
        nvps.add(new BasicNameValuePair("times", count + ""));//打印数量
        String res = callHttp(nvps);
        Map<String, Object> jsonObject = JSONObject.parseObject(res);
        if (jsonObject.get("msg").equals("ok")) {
        } else {
            throw new BaseException(BaseError.FeieyunAPIError);
        }
    }

    @Override
    public String getCenterText(String str, Integer length) throws UnsupportedEncodingException {
        return fillSpaces(str, length);
    }


    //统计调用
    private String callHttp(List<NameValuePair> nvps) {
        try {
            Map<String, String> config = systemConfigService.getValues(new ArrayList<String>() {{
                add("system_feieyun_prict_user");
                add("system_feieyun_prict_key");
            }});
            nvps.add(new BasicNameValuePair("user", config.get("system_feieyun_prict_user")));
            String STIME = String.valueOf(System.currentTimeMillis() / 1000);
            nvps.add(new BasicNameValuePair("stime", STIME));
            nvps.add(new BasicNameValuePair("sig", signature(config.get("system_feieyun_prict_user"), config.get("system_feieyun_prict_key"), STIME)));

            //通过POST请求，发送打印信息到服务器
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)//读取超时
                    .setConnectTimeout(30000)//连接超时
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            HttpPost post = new HttpPost(URL);

            CloseableHttpResponse response = null;
            String result = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
                response = httpClient.execute(post);
                int statecode = response.getStatusLine().getStatusCode();
                if (statecode == 200) {
                    HttpEntity httpentity = response.getEntity();
                    if (httpentity != null) {
                        //服务器返回的JSON字符串，建议要当做日志记录起来
                        result = EntityUtils.toString(httpentity);
                        return result;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    post.abort();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.debug("飞鹅云调用结果：result:{}", result);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("************飞鹅云调用失败************");
            return "";
        }
        return "";
    }

    //生成签名字符串
    private static String signature(String USER, String UKEY, String STIME) {
        String s = DigestUtils.sha1Hex(USER + UKEY + STIME);
        return s;
    }

    /**
     * 40*60
     * 补空格 使对齐
     *
     * @param data
     * @param count 26
     * @return
     */
    private String fillSpaces(String data, Integer count) throws UnsupportedEncodingException {
        int length = getStrLength(data);
        if (length < count) {
            int loss = count - length;
            int bu = loss / 2;
            for (int i = 0; i < bu; i++) {
                data = " " + data + " ";
            }
        }
        return data;
    }

    //获取字符串总长度
    private Integer getStrLength(String str) throws UnsupportedEncodingException {
        int len = str.length();
        Integer length = 0;
        for (int i = 0; i < len; i++) {
            String temp = str.charAt(i) + "";
            if (isContainChinese(temp)) {
                length += 2;
            } else {

                length += 1;
            }
        }
        return length;
    }

    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符 false 不包含中文字符
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
