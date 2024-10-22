package com.hongan.template.base.service;

import com.hongan.template.base.entity.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {

    String saveImgFilename(MultipartFile file, String filename) throws BaseException;

    String saveImg(MultipartFile file) throws BaseException;

    String saveBase64ToFile(String b64, String filename) throws BaseException;

    /**
     * 将bytes输出为文件
     *
     * @param bytes    文件大小
     * @param filename 输出的文件名
     * @return 文件可访问URL
     * @throws BaseException
     */
    String saveBytesToFile(byte[] bytes, String filename) throws BaseException;

    String getPublicPath(String localPath) throws BaseException;

    String getLocalPath(String filename, String fileType) throws BaseException;

    String saveFile(MultipartFile file) throws BaseException, IOException;

    /**
     * 保存文件到本地
     * 如果是图片，进行压缩
     */
    String saveFileToLocation(MultipartFile file) throws BaseException, IOException;

    /**
     * 保存文件到七牛云
     * 如果是图片，进行压缩
     */
    String saveFileToQiniuyun(MultipartFile file) throws BaseException, IOException;

    String saveFile(byte[] file, String filename) throws BaseException, IOException;

    /**
     * 保存文件到本地
     * 如果是图片，进行压缩
     */
    String saveFileToLocation(byte[] file, String filename) throws BaseException, IOException;

    /**
     * 保存文件到七牛云
     * 如果是图片，进行压缩
     */
    String saveFileToQiniuyun(byte[] file, String filename) throws BaseException, IOException;
}
