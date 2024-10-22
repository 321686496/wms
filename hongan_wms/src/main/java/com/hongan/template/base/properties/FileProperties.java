package com.hongan.template.base.properties;

import lombok.Data;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-06 11:22
 * @Description: FileProperties
 */

@Data
public class FileProperties {
    private String imgPath;
    private String imgURL;
    private String privatePath;
    private String securityPath;
    private String location;
}
