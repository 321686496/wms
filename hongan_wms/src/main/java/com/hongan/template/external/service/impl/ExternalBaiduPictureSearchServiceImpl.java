package com.hongan.template.external.service.impl;

import com.baidu.aip.imagesearch.AipImageSearch;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.service.IExternalBaiduPictureSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExternalBaiduPictureSearchServiceImpl implements IExternalBaiduPictureSearchService {
    private static AipImageSearch client;
    static Map<String, String> config;
    @Autowired
    private IHonganConfigService systemConfigService;

    //利用@PostConstruct将application中配置的值赋给本地的变量
    @PostConstruct
    public void getServelDate() {
        config = systemConfigService.getValues(new ArrayList<String>() {{
            add("external_baidu_picture_search_appid");
            add("external_baidu_picture_search_key");
            add("external_baidu_picture_search_secret");
        }});
    }

    /**
     * 唯一公开获取实例的方法（静态工厂方法），该方法使用synchronized加锁，来保证线程安全性
     *
     * @return
     */
    public static synchronized AipImageSearch getInstance() {
        if (client == null) {
            client = new AipImageSearch(config.get("external_baidu_picture_search_appid"), config.get("external_baidu_picture_search_key"), config.get("external_baidu_picture_search_secret"));
        }
        return client;
    }

    @Value("${spring.profiles.active}")
    private String path;


    private String getTags() {
        return "";
//        if (path.equals("dev")) {
//            return "1";
//        } else if (path.equals("prodTest")) {
//            return "2";
//        } else if (path.equals("prod")) {
//            return "3";
//        } else {
//            return "9999";
//        }
    }

    @Override
    public String pictureInStock(String imgUrl, String code) throws BaseException {
        //查询租户信息
        return "";
//        //判断是否有以图识物模块
//        if (!tenant.getHasPictureSearchModule()) return "";
//        //判断是否选择了入库类型
//        if (tenant.getPictureSearchType() == PictureSearchType.NotSelected) {
//            return "";
//        }
//        //减少可用次数
//        tenantPictureService.used(tenantId);
//        try {
//            HashMap<String, String> options = new HashMap<String, String>();
//            if (tenant.getPictureSearchType() == PictureSearchType.Product) {
//                //商品入库
//                //将租户ID作为分类1的值
//                options.put("class_id1", tenantId.toString());
//                options.put("class_id2", getTags());
////                JSONObject jsonObject = getInstance().productAddUrl(imgUrl, code, options);
//                //图片增加水印
//                byte[] bytes = FileUtils.addTextWaterMark(imgUrl, new Color(212, 190, 190), 20, tenantId + "-" + code);
//                JSONObject jsonObject = getInstance().productAdd(bytes, code, options);
//                return jsonObject.getString("cont_sign");
//            } else if (tenant.getPictureSearchType() == PictureSearchType.Similarity) {
//                //相似搜索
//                //将租户ID作为分类1的值
//                options.put("tags", tenantId.toString() + "," + getTags());
////                JSONObject jsonObject = getInstance().similarAddUrl(imgUrl, code, options);
//                //图片增加水印
//                byte[] bytes = FileUtils.addTextWaterMark(imgUrl, new Color(212, 190, 190), 20, tenantId + "-" + code);
//                JSONObject jsonObject = getInstance().similarAdd(bytes, code, options);
//                return jsonObject.getString("cont_sign");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
    }

    @Override
    public List<Map<String, String>> pictureSearch(byte[] bytes) throws BaseException, IOException {
        return null;
//        //进行图片压缩
//        bytes = ImgHandleUtil.imgThumbnails(bytes);
//        //查询租户信息
//        PonlayTenant tenant = tenantService.selectById(tenantId);
//        //判断是否有以图识物模块
//        if (!tenant.getHasPictureSearchModule()) throw new BaseException(BaseError.PictureSearchCountInsufficient);
//
//        //判断是否选择了入库类型
//        if (tenant.getPictureSearchType() == PictureSearchType.NotSelected) {
//            return new ArrayList<>();
//        }
//        //减少可用次数
//        tenantPictureService.used(tenantId);
//        try {
//            HashMap<String, String> options = new HashMap<String, String>();
//            //检索时tag之间的逻辑， 0：逻辑and，1：逻辑or
//            options.put("tag_logic", "0");
//            //分页功能：起始位置
//            options.put("pn", "0");
//            //分页功能：截取条数
//            options.put("rn", "20");
//            if (tenant.getPictureSearchType() == PictureSearchType.Product) {
//                //商品搜索
//                options.put("class_id1", tenantId.toString());
//                options.put("class_id2", getTags());
//                //将商品SKU编码作为附加信息
//                JSONObject response = getInstance().productSearch(bytes, options);
//                JSONArray resultArrays = response.getJSONArray("result");
//                List<Map<String, String>> briefList = new ArrayList<>();
//                for (int i = 0; i < resultArrays.length(); i++) {
//                    JSONObject item = resultArrays.getJSONObject(i);
//                    if (BigDecimal.valueOf(item.getDouble("score")).compareTo(tenant.getPictureSearchSimilarity()) > -1) {
//                        briefList.add(new HashMap<>() {{
//                            put("code", item.getString("brief"));
//                            put("sign", item.getString("cont_sign"));
//                            put("score", item.getDouble("score") + "");
//                        }});
//                    }
//                }
//                return briefList;
//            } else if (tenant.getPictureSearchType() == PictureSearchType.Similarity) {
//                //相似搜索
//                //将租户ID作为分类1的值
//                options.put("tags", tenantId.toString() + "," + getTags());
//                //将商品SKU编码作为附加信息
//                JSONObject response = getInstance().similarSearch(bytes, options);
//                JSONArray resultArrays = response.getJSONArray("result");
//                List<Map<String, String>> briefList = new ArrayList<>();
//                for (int i = 0; i < resultArrays.length(); i++) {
//                    JSONObject item = resultArrays.getJSONObject(i);
//                    if (BigDecimal.valueOf(item.getDouble("score")).compareTo(tenant.getPictureSearchSimilarity()) > -1) {
//                        briefList.add(new HashMap<>() {{
//                            put("code", item.getString("brief"));
//                            put("sign", item.getString("cont_sign"));
//                            put("score", item.getDouble("score") + "");
//                        }});
//                    }
//                }
//                return briefList;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
    }
}
