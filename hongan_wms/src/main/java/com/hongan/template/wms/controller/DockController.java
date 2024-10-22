package com.hongan.template.wms.controller;

import com.hongan.template.base.entity.BaseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RequestMapping("/doc")
@RestController
public class DockController {
    @GetMapping("/download/{name}/{passwd}")
    public void docdownload(@PathVariable("name") String name, @PathVariable("passwd") String passwd, HttpServletResponse response) throws BaseException, IOException {
        if(!("admin".equals(name)&&"admin123".equals(passwd))){
            throw new BaseException("用户名或密码错误");
        }
        // 获取位于resources目录资源下的文件路径
        String path = "static/assets/doc/doc.docx";
        System.out.println("path: "+path);
//        String absolutePath=new ClassPathResource(path).getURL().getPath();
        String absolutePath="/doc.docx";
        System.out.println("absolutePath: "+absolutePath);
        // 文件类型为word文档
        String fileType = "docx";
        // 下载在客户端
        response.setHeader("Content-Disposition", "attachment; filename=" + name + "." + fileType);
//        response.setContentType("application/octet-stream");
        // 提取资源向客户端展示
        ServletOutputStream outputStream = response.getOutputStream();
        File file = new File(absolutePath);
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
