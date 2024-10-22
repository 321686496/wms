package com.hongan.template.base.service.impl;

import com.hongan.template.base.service.IFileService;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.service.IQiniuyunService;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.ImgHandleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-06 11:30
 * @Description: File
 */

@Slf4j
@Service
public class FileServiceTmpl implements IFileService {

    //    @Autowired
//    private UploadFileConfigure fileConfigure;
    @Autowired
    private IHonganConfigService configService;
    @Autowired
    private IQiniuyunService qiniuyunService;
    @Value("${spring.profiles.active}")
    private String path;

    @Override
    public String saveImgFilename(MultipartFile file, String filename) throws BaseException {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf("");
        String fileType = filename.substring(index);
        filename = filename.substring(0, index);
        // 生成系统保存的路径 和返回的路径
        String localPath = getLocalPath(filename, fileType);

        File dest = new File(localPath);
        try {
            file.transferTo(dest);
            if (path.startsWith("dev")) {
                //文件复制到另一个路径
                Files.copy(new File(localPath).toPath(), new File(configService.getValue("system_upload_copyPath") + filename).toPath());
            }
            return configService.getValue("system_domain") + getPublicPath(localPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(BaseError.IOFileError);
        }
    }

    @Override
    public String saveImg(MultipartFile file) throws BaseException {
        String filename = file.getOriginalFilename();
        int index = filename.lastIndexOf(".");
        //后缀
        String suffix = filename.substring(index);
        filename = UUID.randomUUID().toString() + suffix;
        //如果是图片 进行大小压缩


        return saveImgFilename(file, filename);
    }

    @Override
    public String saveBase64ToFile(String b64, String filename) throws BaseException {
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        String fileType = b64.substring(11, b64.indexOf(";"));
        String localPath = getLocalPath(filename, "" + fileType);
        try {
            byte[] bytes = Base64.getDecoder().decode(b64.substring(b64.indexOf(",") + 1).trim());
            File file = new File(localPath);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            return getPublicPath(localPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseError.IOFileError);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String saveBytesToFile(byte[] bytes, String filename) throws BaseException {
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        String localPath = getLocalPath(filename, "");
        try {
            File file = new File(localPath);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            if (path.startsWith("dev")) {
                //文件复制到另一个路径
                Files.copy(new File(localPath).toPath(), new File(configService.getValue("system_upload_copyPath") + filename).toPath());
            }
            return configService.getValue("system_domain") + getPublicPath(localPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseError.IOFileError);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getPublicPath(String localPath) throws BaseException {
        String urlPath = configService.getValue("system_upload_publicPath");
        String filePath = configService.getValue("system_upload_path") + urlPath;
        return urlPath + localPath.substring(filePath.length());
    }

    @Override
    public String getLocalPath(String filename, String fileType) throws BaseException {
        String filePath = configService.getValue("system_upload_path") + configService.getValue("system_upload_imgUrl");
        log.debug(filePath);
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        return filePath + filename + fileType;
    }

    @Override
    public String saveFile(MultipartFile file) throws BaseException, IOException {
        if (configService.getValue("system_upload_type").equals("location")) {
            return saveFileToLocation(file);
        } else {
            return saveFileToQiniuyun(file);
        }
    }

    @Override
    public String saveFile(byte[] file, String filename) throws BaseException, IOException {
        if (configService.getValue("system_upload_type").equals("location")) {
            return saveFileToLocation(file, filename);
        } else {
            return saveFileToQiniuyun(file, filename);
        }
    }

    @Override
    public String saveFileToLocation(MultipartFile file) throws BaseException, IOException {
        //文件名称
        String originalFilename = file.getOriginalFilename();
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        //随机生成文件名称
        String newFilename = UUID.randomUUID().toString() + "." + suffix;
        //判断文件类型
        if (suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg")) {
            //如果是图片，判断文件大小是否超出，超出则进行压缩
            return saveBytesToFile(ImgHandleUtil.imgThumbnails(file.getBytes()), newFilename);
        } else {
            return saveImgFilename(file, newFilename);
        }
    }


    @Override
    public String saveFileToLocation(byte[] file, String filename) throws BaseException, IOException {
        //文件名称
        String originalFilename = filename;
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        //随机生成文件名称
        String newFilename = UUID.randomUUID().toString() + "." + suffix;
        //判断文件类型
        if (suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg")) {
            //如果是图片，判断文件大小是否超出，超出则进行压缩
            return saveBytesToFile(ImgHandleUtil.imgThumbnails(file), newFilename);
        } else {
            return saveBytesToFile(file, newFilename);
        }
    }

    @Override
    public String saveFileToQiniuyun(MultipartFile file) throws BaseException, IOException {
        //文件名称
        String originalFilename = file.getOriginalFilename();
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        //随机生成文件名称
        String newFilename = UUID.randomUUID().toString() + "." + suffix;
        //判断文件类型
        if (suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg")) {
            //如果是图片，判断文件大小是否超出，超出则进行压缩
            return qiniuyunService.uploadFile(ImgHandleUtil.imgThumbnails(file.getBytes()), newFilename);
        } else {
            return qiniuyunService.uploadFile(file.getBytes(), newFilename);
        }
    }


    @Override
    public String saveFileToQiniuyun(byte[] file, String filename) throws BaseException, IOException {
        //文件名称
        String originalFilename = filename;
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        //随机生成文件名称
        String newFilename = UUID.randomUUID().toString() + "." + suffix;
        //判断文件类型
        if (suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg")) {
            //如果是图片，判断文件大小是否超出，超出则进行压缩
            return qiniuyunService.uploadFile(ImgHandleUtil.imgThumbnails(file), newFilename);
        } else {
            return qiniuyunService.uploadFile(file, newFilename);
        }
    }

}
