package com.hongan.template.base.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-02-19 11:16
 * @Description: UploadConfigure
 */

@Data
//@Configuration
//@ConfigurationProperties(prefix = "upload-file")
public class UploadFileConfigure {
    private String root;
    private String imgPath;
    private String URLPath;
    private String privatePath;
}
