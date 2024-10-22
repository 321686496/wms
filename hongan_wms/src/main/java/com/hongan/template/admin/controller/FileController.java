package com.hongan.template.admin.controller;

import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-01-02 17:11
 * @Description: FileController
 */

@Api(tags = "管理端-文件上传接口")
@RestController
@RequestMapping("/admin/upload")
public class FileController {

    @Autowired
    IFileService fileService;

    @ApiOperation("上传图片")
    @PostMapping("/img")
    public BaseResponse uploadImg(@RequestParam("file") MultipartFile file) throws BaseException, IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        String limitFileType = "jpeg,jpg,gif,png,svg,webp,jfif,bmp,dpg,image";
        List<String> suffixLimit = Arrays.asList(limitFileType.split(","));
        if (!suffixLimit.contains(suffix))
            throw new BaseException(BaseError.FileFormatError);

        if (!suffix.equals("jpg") && !suffix.equals("png") && !suffix.equals("jpeg"))
            throw new BaseException(BaseError.ImgFileFormatError);
        String pubURL = fileService.saveFile(file);
        Map<String, String> res = new HashMap<>();
        res.put("name", originalFilename);
        res.put("url", pubURL);
        return new BaseResponse().success(res);
    }

    @ApiOperation("上传文件")
    @PostMapping("/file")
    public BaseResponse uploadFile(@RequestParam("file") MultipartFile file) throws BaseException, IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        String limitFileType = "jpeg,jpg,gif,png,svg,webp,jfif,bmp,dpg,image," +
                "mp4,avi,wmv,flv,mkv,asf,3gp,divx,f4v," +
                "doc,xls,ppt,pdf,docx,xlsx,pptx";
        List<String> suffixLimit = Arrays.asList(limitFileType.split(","));
        if (!suffixLimit.contains(suffix))
            throw new BaseException(BaseError.FileFormatError);
        String pubURL = fileService.saveFile(file);
        Map<String, String> res = new HashMap<>();
        res.put("name", originalFilename);
        res.put("url", pubURL);
        return new BaseResponse().success(res);
    }

    @ApiOperation("上传视频")
    @PostMapping("/video")
    public BaseResponse uploadVideoFile(@RequestParam("file") MultipartFile file) throws BaseException, IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        String limitFileType = "mp4,avi,wmv,flv,mkv,asf,3gp,divx,f4v";
        List<String> suffixLimit = Arrays.asList(limitFileType.split(","));
        if (!suffixLimit.contains(suffix))
            throw new BaseException(BaseError.FileFormatError);
        String pubURL = fileService.saveFile(file);
        Map<String, String> res = new HashMap<>();
        res.put("name", originalFilename);
        res.put("url", pubURL);
        return new BaseResponse().success(res);
    }

}
