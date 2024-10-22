package com.hongan.template.user.controller;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-01-02 17:11
 * @Description: FileController
 */

@Api(tags = "用户端-文件上传接口")
@RestController
@RequestMapping("/user/upload")
public class UserFileController {

    @Autowired
    IFileService fileService;

    @ApiOperation("上传图片")
    @PostMapping("/img")
    public BaseResponse uploadFile(@RequestParam("file") MultipartFile file) throws BaseException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        if (!suffix.equals("jpg") && !suffix.equals("png") && !suffix.equals("jpeg"))
            return new BaseResponse().error(20000, "图片格式不正确");
        String pubURL = fileService.saveImg(file);
        return new BaseResponse().success(pubURL);
    }

    @ApiOperation("上传视频")
    @PostMapping("/video")
    public BaseResponse uploadVideoFile(@RequestParam("file") MultipartFile file) throws BaseException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        if (!suffix.equals("mp4")) {
            return new BaseResponse().error(20000, "视频格式不正确");
        }
        String pubURL = fileService.saveImg(file);
        return new BaseResponse().success(pubURL);
    }
}