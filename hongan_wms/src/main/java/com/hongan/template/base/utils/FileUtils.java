package com.hongan.template.base.utils;

import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {

    //将网络图片下载为byte[]
    public static byte[] downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 图片增加水印
     *
     * @param targetImg 原始图片
     * @param textColor 水印字体颜色
     * @param fontSize  水印字体大小
     * @param text      水印内容
     * @param outPath   输出路径
     */
    public static void addTextWaterMark(BufferedImage targetImg, Color textColor, int fontSize, String text, String outPath) {
        try {
            int width = targetImg.getWidth(); //图片宽
            int height = targetImg.getHeight(); //图片高
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(targetImg, 0, 0, width, height, null);
            g.setColor(textColor); //水印颜色
            g.setFont(new Font("微软雅黑", Font.ITALIC, fontSize));
            // 水印内容放置在右下角
            int x = width - (text.length() + 1) * fontSize;
            int y = height - fontSize * 2;
            g.drawString(text, x, y);
            FileOutputStream outImgStream = new FileOutputStream(outPath);
            ImageIO.write(bufferedImage, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws IOException {
////        File file = new File("F:\\test\\2222.jpg");
//        String imgUrl = "https://p8-cdn.ponlay.com/FmMLHpJ_fN38iovUOuMXn69JfScR.jpg";
//        URL url = new URL(imgUrl);
//        BufferedImage image = ImageIO.read(url);
//        addTextWaterMark(image, new Color(212, 190, 190), 20, "1", "F:\\test\\2222-3.jpg");
//    }

    /**
     * 图片增加水印(获取byte[])
     *
     * @param imgUrl    原始图片(网络图片)
     * @param textColor 水印字体颜色
     * @param fontSize  水印字体大小
     * @param text      水印内容
     */
    public static byte[] addTextWaterMark(String imgUrl, Color textColor, int fontSize, String text) throws BaseException {
        try {
            System.out.println("imgUrl:::" + imgUrl);
            URL url = new URL(imgUrl);
            BufferedImage targetImg = ImageIO.read(url);
            int width = targetImg.getWidth(); //图片宽
            int height = targetImg.getHeight(); //图片高
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(targetImg, 0, 0, width, height, null);
            g.setColor(textColor); //水印颜色
            g.setFont(new Font("微软雅黑", Font.ITALIC, fontSize));
            // 水印内容放置在右下角
            int x = width - (text.length() + 1) * fontSize;
            int y = height - fontSize * 2;
            g.drawString(text, x, y);
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseError.FormatError);
        }
    }
}
