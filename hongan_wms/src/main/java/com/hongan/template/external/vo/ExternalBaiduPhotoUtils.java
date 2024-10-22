package com.hongan.template.external.vo;

import com.baidu.aip.imagesearch.AipImageSearch;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class ExternalBaiduPhotoUtils {
    //测试使用
    public static final String APP_ID = "45451595";
    public static final String API_KEY = "xHcgqM4lCGmntfKHhPmdt4rP";
    public static final String SECRET_KEY = "iMtPsXl45Hu1zVyZHLhu73uQdGPCphUN";

    private static final AipImageSearch client = new AipImageSearch(APP_ID, API_KEY, SECRET_KEY);

    /**
     * @param imgUrl  图片URL
     * @param brief   携带标识，检索时可带回
     * @param options 附加选项
     * @return
     * @throws IOException
     */
    public static JSONObject inStockGoodsPicture(String imgUrl, String brief, HashMap<String, String> options) throws IOException {
        byte[] imgBytes = downloadImage(imgUrl);
        JSONObject res = client.productAdd(imgBytes, brief, options);
//        System.out.println("图片入库："+res.toString(2));
        return res;
    }

    public static void main(String[] args) {
        JSONObject jsonObject = client.similarAddUrl("https://p8-cdn.ponlay.com/FmMLHpJ_fN38iovUOuMXn69JfScR.jpg", "123", null);
        System.out.println(jsonObject);
    }

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

}
