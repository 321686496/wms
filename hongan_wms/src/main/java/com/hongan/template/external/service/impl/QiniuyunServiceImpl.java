package com.hongan.template.external.service.impl;

import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.service.IQiniuyunService;
import com.hongan.template.external.vo.QiniuToken;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class QiniuyunServiceImpl implements IQiniuyunService {
    @Autowired
    private IHonganConfigService configService;

    @Override
    public QiniuToken getToken() {
        Map<String, String> values = configService.getValues(new ArrayList<String>() {{
            add("system_upload_qiniu_domain");
            add("system_upload_qiniu_bucket");
            add("system_upload_qiniu_accessKey");
            add("system_upload_qiniu_secretKey");
        }});
        Auth auth = Auth.create(values.get("system_upload_qiniu_accessKey"), values.get("system_upload_qiniu_secretKey"));
        StringMap putPolicy = new StringMap();
        // 配置资源名生成策略
        putPolicy.put("saveKey", "$(etag)$(ext)");
//        putPolicy.put("forceSaveKey", "true");
//        putPolicy.put("saveKey", "$(fname)");
        putPolicy.put("returnBody", "{\"data\":{\"key\":\"$(key)\",\"hash\":\"$(etag)\"},\"status\":0}");
        int expires = 3600;
        String token = auth.uploadToken(values.get("system_upload_qiniu_bucket"), null, expires, putPolicy);
        return QiniuToken.from(token, expires, values.get("system_upload_qiniu_domain"));
    }


    @Override
    public String uploadFile(byte[] file, String key) {
        Map<String, String> config = configService.getValues(new ArrayList<String>() {{
            add("system_upload_qiniu_domain");
            add("system_upload_qiniu_bucket");
            add("system_upload_qiniu_accessKey");
            add("system_upload_qiniu_secretKey");
        }});
        Auth auth = Auth.create(config.get("system_upload_qiniu_accessKey"), config.get("system_upload_qiniu_secretKey"));

        // 创建上传对象，Zone*代表地区  zoneAs0东南亚
        Configuration configuration = new Configuration(Zone.zone1());
//        Configuration configuration = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(configuration);
        try {
            // 调用put方法上传
            String token = auth.uploadToken(config.get("system_upload_qiniu_bucket"));
            if (StringUtils.isEmpty(token)) {
                System.out.println("未获取到token，请重试！");
                return null;
            }
            Response res = uploadManager.put(file, key, token);
            // 打印返回的信息
            if (res.isOK()) {
                return config.get("system_upload_qiniu_domain") + "/" + key;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            e.printStackTrace();
            System.out.println("error " + r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                System.out.println("error " + e1.error());
            }
        }
        return null;
    }

}
