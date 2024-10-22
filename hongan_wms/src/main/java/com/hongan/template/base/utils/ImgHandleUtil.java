package com.hongan.template.base.utils;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片大小压缩工具类
 */
public class ImgHandleUtil {

    /**
     * 图片大小压缩
     *
     * @param file 源文件流
     * @return 压缩后的文件流
     * @throws IOException
     */
    public static byte[] imgThumbnails(byte[] file) throws IOException {
        // 获取原始图片的字节数组
        byte[] originalBytes = file;
        // 压缩图片，确保大小不超过2M
        double quality = 1.0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while (originalBytes.length > 2 * 1024 * 1024) {
            outputStream.reset();
            Thumbnails.of(new ByteArrayInputStream(file))
                    .size(1280, 1024)
                    .outputQuality(quality)
                    .toOutputStream(outputStream);
            originalBytes = outputStream.toByteArray();
            quality -= 0.1;
        }
        return originalBytes;
    }
}
