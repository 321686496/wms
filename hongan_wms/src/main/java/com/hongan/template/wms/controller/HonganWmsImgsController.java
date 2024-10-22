package com.hongan.template.wms.controller;

import com.hongan.template.config.service.IHonganConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

@RequestMapping("/imgs")
@RestController
public class HonganWmsImgsController {

    @Autowired
    private IHonganConfigService configService;

    @GetMapping("{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception{
        // 解码文件名
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        // 文件类型为pdf格式文件
        response.setContentType("application/pdf");
        System.out.println("fileName: "+fileName);
        Map<String, String> values = configService.getValues(new ArrayList<String>() {{
            add("system_upload_path");
            add("system_upload_publicPath");
            add("system_upload_imgUrl");
            add("system_upload_copyPath");
            add("system_domain");
        }});
        String directionPath=values.get("system_upload_path") + values.get("system_upload_publicPath");
        String filePath = directionPath + fileName;
        System.out.println("filePath："+filePath);
        // 提取资源向客户端展示
        ServletOutputStream outputStream = response.getOutputStream();
        File file = new File(filePath);
        FileInputStream inputStream= new FileInputStream(file);
        // 写入资源
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }
}
