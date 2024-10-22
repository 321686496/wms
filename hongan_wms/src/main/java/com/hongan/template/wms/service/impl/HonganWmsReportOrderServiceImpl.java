package com.hongan.template.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.base.utils.StringUtils;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsMaterialType;
import com.hongan.template.wms.entity.HonganWmsReportOrder;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import com.hongan.template.wms.mapper.HonganWmsReportOrderMapper;
import com.hongan.template.wms.query.QueryReportOrder;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.hongan.template.wms.service.IHonganWmsMaterialTypeService;
import com.hongan.template.wms.service.IHonganWmsReportOrderItemService;
import com.hongan.template.wms.service.IHonganWmsReportOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 报货单表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Service
public class HonganWmsReportOrderServiceImpl extends ServiceImpl<HonganWmsReportOrderMapper, HonganWmsReportOrder> implements IHonganWmsReportOrderService {
    @Autowired
    private IHonganWmsReportOrderItemService itemService;
    @Autowired
    private IHonganAdminService adminService;
    @Autowired
    private IHonganWmsMaterialService materialService;
    @Autowired
    private IHonganWmsMaterialTypeService materialTypeService;
    @Autowired
    private IHonganConfigService configService;

    @Override
    public IPage<HonganWmsReportOrder> queryPage(QueryReportOrder query) throws BaseException {
        IPage<HonganWmsReportOrder> page = new Page<>(query.getCurrent(), query.getPageSize());
        List<HonganWmsReportOrder> records = baseMapper.selectPage(page, query.toWrapper()).getRecords();
        for (HonganWmsReportOrder record : records) {
            List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(record.getId());
            record.setItemList(itemList);
        }
        return page;
    }

    @Override
    public Map<String, Object> queryStatisticsData(QueryReportOrder query) {
        return this.getMap(query.toWrapper()
                .select("ifnull(sum(stock_number_total),0) stockNumberTotal," +
                        "ifnull(sum(report_number_total),0) reportNumberTotal"));
    }

    @Override
    public List<HonganWmsReportOrder> queryListExport(QueryReportOrder query) throws BaseException {
        List<HonganWmsReportOrder> list = baseMapper.selectList(query.toWrapper());
        for (HonganWmsReportOrder data : list) {
            List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(data.getId());
            data.setItemList(itemList);
        }
        return list;
    }

    @Override
    public HonganWmsReportOrder selectById(Long id) throws BaseException {
        if (null == id)
            throw new BaseException(BaseError.BadRequest);
        HonganWmsReportOrder data = baseMapper.selectOne(new QueryWrapper<HonganWmsReportOrder>()
                .eq("id", id));
        if (data == null) throw new BaseException(BaseError.DataNotExist);
        return data;
    }

    @Override
    public HonganWmsReportOrder selectDetailById(Long id) throws BaseException {
        HonganWmsReportOrder data = selectById(id);
        List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(data.getId());
        data.setItemList(itemList);
        return data;
    }

    @Override
    public Map<String, Object> selectMergeDetailById(Long id) throws BaseException {
        Map<String, Object> res = new HashMap<>();
        List<String> tableHead = new ArrayList<>();
        List<List<String>> items = new ArrayList<>();

        tableHead.add("产品");
        tableHead.add("产品类别");
        HonganWmsReportOrder reportOrder = selectById(id);

        //查询不同的店铺列
        List<String> shopNameList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
                        .select("DISTINCT(shop_name),shop_priority")
                        .eq("pid", reportOrder.getId())
                        .orderByDesc("shop_priority")
                )
                .stream()
                .map(HonganWmsReportOrderItem::getShopName)
                .distinct()
                .collect(Collectors.toList());
        //生成表头列
        tableHead.addAll(shopNameList);
        tableHead.add("总计");
        res.put("tableHead", tableHead);

        //查询不同的商品
        List<HonganWmsReportOrderItem> materialList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
                .select("DISTINCT(material_name),material_priority,type_id,type_name,type_name_fr")
                .eq("pid", reportOrder.getId())
                .orderByAsc("type_id")
                .orderByDesc("material_priority")).stream()
                .map(i->HonganWmsReportOrderItem.builder()
                        .materialName(i.getMaterialName())
                        .typeName(i.getTypeName())
                        .typeNameFr(i.getTypeNameFr())
                        .build())
                .distinct()
                .collect(Collectors.toList());
        //查询自己明细
        List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(id);

        for (HonganWmsReportOrderItem material : materialList) {
            List<String> reportNumberItem = new ArrayList<>();
            reportNumberItem.add(material.getMaterialName());
            reportNumberItem.add(material.getTypeName());
            BigDecimal reportNumberTotal = BigDecimal.ZERO;
            for (String shopName : shopNameList) {
                BigDecimal reportNumber = itemList.stream()
                        .filter(person -> person.getMaterialName().equals(material.getMaterialName()) && person.getShopName().equals(shopName))
                        .map(HonganWmsReportOrderItem::getReportNumber)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                reportNumberItem.add(reportNumber.stripTrailingZeros().toPlainString());
                reportNumberTotal = reportNumberTotal.add(reportNumber);
            }
            reportNumberItem.add(reportNumberTotal.stripTrailingZeros().toPlainString());
            items.add(reportNumberItem);
        }
        res.put("items", items);
        return res;
    }
//    public Map<String, Object> selectMergeDetailById(Long id) throws BaseException {
//        Map<String, Object> res = new HashMap<>();
//        List<String> tableHead = new ArrayList<>();
//        List<List<String>> items = new ArrayList<>();
//
//        tableHead.add("产品");
//        HonganWmsReportOrder reportOrder = selectById(id);
//
//        //查询不同的店铺列
//        List<String> shopNameList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
//                .select("DISTINCT(shop_name)")
//                .eq("pid", reportOrder.getId())).stream()
//                .map(HonganWmsReportOrderItem::getShopName)
//                .distinct()
//                .collect(Collectors.toList());
//        //生成表头列
//        tableHead.addAll(shopNameList);
//        tableHead.add("总计");
//        res.put("tableHead", tableHead);
//
//        //查询不同的商品
//        List<String> materialNameList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
//                .select("DISTINCT(material_name)")
//                .eq("pid", reportOrder.getId())).stream()
//                .map(HonganWmsReportOrderItem::getMaterialName)
//                .distinct()
//                .collect(Collectors.toList());
//        //查询自己明细
//        List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(id);
//
//        for (String materialName : materialNameList) {
//            List<String> stockNumberItem = new ArrayList<>();
////            List<String> reportNumberItem = new ArrayList<>();
//            stockNumberItem.add(materialName);
////            reportNumberItem.add(materialName);
//            BigDecimal stockNumberTotal = BigDecimal.ZERO;
//            BigDecimal reportNumberTotal = BigDecimal.ZERO;
//            for (String shopName : shopNameList) {
//                BigDecimal stockNumber = itemList.stream()
//                        .filter(person -> person.getMaterialName().equals(materialName) && person.getShopName().equals(shopName))
//                        .map(HonganWmsReportOrderItem::getStockNumber)
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                BigDecimal reportNumber = itemList.stream()
//                        .filter(person -> person.getMaterialName().equals(materialName) && person.getShopName().equals(shopName))
//                        .map(HonganWmsReportOrderItem::getReportNumber)
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                stockNumberItem.add(stockNumber.stripTrailingZeros().toPlainString());
////                reportNumberItem.add(reportNumber.stripTrailingZeros().toPlainString());
//                stockNumberTotal = stockNumberTotal.add(stockNumber);
//                reportNumberTotal = reportNumberTotal.add(reportNumber);
//            }
//            stockNumberItem.add(stockNumberTotal.stripTrailingZeros().toPlainString());
////            reportNumberItem.add(reportNumberTotal.stripTrailingZeros().toPlainString());
//            items.add(stockNumberItem);
////            items.add(reportNumberItem);
//        }
//        res.put("items", items);
//        return res;
//    }

    @Override
    public String getBillCode() {
        Integer count = baseMapper.getTodayBillCount();
        return "BH" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%06d", count + 1);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Long saveBill(HonganWmsReportOrder data, TokenUser user) throws BaseException {
        if (StringUtils.isEmpty(data.getBillCode())) {
            data.setBillCode(getBillCode());
        }
        HonganAdmin admin = adminService.getById(user.getId());
        //如果没有填写单据日期，默认为当前
        if (data.getBillDate() == null) data.setBillDate(LocalDate.now());
        //判断单据明细是否为空
        if (data.getItemList() == null || data.getItemList().size() < 1)
            throw new BaseException(BaseError.BillItemCannotEmpty);
        data.setIsMerge(false);
        // TODO 测试使用
//        data.setCreateAdminId(admin.getId());
//        data.setCreateAdminName(admin.getName());
        //新增或修改
        data.insertOrUpdate();
        //计算总数量/总成本
        BigDecimal stockNumberTotal = BigDecimal.ZERO;
        BigDecimal reportNumberTotal = BigDecimal.ZERO;

        if (data.getDeleteIds() != null) {
            //删除无用数据
            for (Long id : data.getDeleteIds()) {
                itemService.delete(data.getId(), id);
            }
        }
        for (HonganWmsReportOrderItem item : data.getItemList()) {
            // 通过物料编号查询出商品信息，而不再使用前端传递过来的冗余物料信息
            item.setPid(data.getId());
            if (item.getStockNumber() == null || item.getStockNumber().compareTo(BigDecimal.ZERO) < 1)
                item.setStockNumber(BigDecimal.ZERO);
            if (item.getReportNumber() == null || item.getReportNumber().compareTo(BigDecimal.ZERO) < 1)
                item.setReportNumber(BigDecimal.ZERO);
            //判断是否选择商品
            if (item.getMaterialId() == null)
                throw new BaseException(BaseError.PleaseSelectMaterial);
            HonganWmsMaterial wmsMaterial = materialService.selectById(item.getMaterialId());
            HonganWmsMaterialType wmsMaterialType = materialTypeService.selectById(wmsMaterial.getTypeId());
            item.setMaterialName(wmsMaterial.getName());
            item.setMaterialCode(wmsMaterial.getCode());
            // 新增物料类型冗余字段
            item.setTypeId(wmsMaterialType.getId());
            item.setTypeName(wmsMaterialType.getClassifyName());
            item.setTypeNameFr(wmsMaterialType.getClassifyNameFr());

            item.setMaterialPriority(wmsMaterial.getPriority());
            item.setShopName(admin.getShopName());
            item.setShopPriority(admin.getPriority());
            item.insertOrUpdate();
            //汇总数据
            stockNumberTotal = stockNumberTotal.add(item.getStockNumber());
            reportNumberTotal = reportNumberTotal.add(item.getReportNumber());
        }
        data.setStockNumberTotal(stockNumberTotal);
        data.setReportNumberTotal(reportNumberTotal);
        data.updateById();
        return data.getId();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void deleteBill(Long id) throws BaseException {
        HonganWmsReportOrder bill = selectById(id);
        //删除单据信息
        baseMapper.deleteById(id);
        //删除子单据信息
        itemService.deleteByPid(id);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Long mergeReportOrder(List<Long> ids, TokenUser user) throws BaseException {
        if (ids.size() < 1) throw new BaseException(BaseError.BadRequest);
        HonganWmsReportOrder data = new HonganWmsReportOrder();
        data.setBillDate(LocalDate.now());
        data.setBillCode(getBillCode());
        data.setCreateAdminId(user.getId());
        data.setCreateAdminName(user.getRealName());
        data.setIsMerge(true);
        //新增或修改
        data.insertOrUpdate();
        //计算总数量/总成本
        BigDecimal stockNumberTotal = BigDecimal.ZERO;
        BigDecimal reportNumberTotal = BigDecimal.ZERO;
        for (Long id : ids) {
            List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(id);
            for (HonganWmsReportOrderItem item : itemList) {
                item.setId(null);
                item.setPid(data.getId());
                item.insert();
                //汇总数据
                stockNumberTotal = stockNumberTotal.add(item.getStockNumber());
                reportNumberTotal = reportNumberTotal.add(item.getReportNumber());
            }
        }
        data.setStockNumberTotal(stockNumberTotal);
        data.setReportNumberTotal(reportNumberTotal);
        data.updateById();
        return data.getId();
    }

    @Override
    public String getPdf(Long id, String language) throws BaseException {
        HonganWmsReportOrder reportOrder = selectById(id);
        if (reportOrder.getIsMerge()) {
            return getMergePdf(id, language, reportOrder);
        } else {
            return getNotMergePdf(id, language, reportOrder);
        }
    }

    // 创建不合并的pdf表格基础内容
    private PdfPTable createNotMergePdfTable(String language){
        Font headFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14F);
        PdfPTable table = new PdfPTable(4); // 创建一个5列的表格
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (language.equals("fr")) {
            PdfPCell cell = new PdfPCell(new Paragraph("Nom de la boutique", headFont));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
//            table.addCell(new PdfPCell(new Paragraph("Shopping Name", headFont)));
            table.addCell(new PdfPCell(new Paragraph("Produits", headFont)));
//            table.addCell(new PdfPCell(new Paragraph("Produit Type", headFont)));
            table.addCell(new PdfPCell(new Paragraph("Quantité en stock", headFont)));
            table.addCell(new PdfPCell(new Paragraph("Nombre de marchandises déclarées", headFont)));
        } else {
            table.addCell(new PdfPCell(new Paragraph("店铺名称", headFont)));
            table.addCell(new PdfPCell(new Paragraph("商品", headFont)));
//            table.addCell(new PdfPCell(new Paragraph("产品类型", headFont)));
            table.addCell(new PdfPCell(new Paragraph("库存数量", headFont)));
            table.addCell(new PdfPCell(new Paragraph("报货数量", headFont)));
        }
        return table;
    }

    // 创建合并的pdf表格基础内容
    private PdfPTable createMergePdfTable(String language,List<String> shopNameList){
        Font headFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14F);

        PdfPTable table = new PdfPTable(shopNameList.size() + 2); // 创建一个n列的表格
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        // 添加标题行
        if (language.equals("fr")) {
            table.addCell(new PdfPCell(new Paragraph("Produits", headFont)));
//            table.addCell(new PdfPCell(new Paragraph("Produits Type", headFont)));
            for (String shopName : shopNameList) {
                table.addCell(new PdfPCell(new Paragraph(shopName, headFont)));
            }
            table.addCell(new PdfPCell(new Paragraph("Total", headFont)));
        } else {
            table.addCell(new PdfPCell(new Paragraph("产品", headFont)));
//            table.addCell(new PdfPCell(new Paragraph("产品类别", headFont)));
            for (String shopName : shopNameList) {
                table.addCell(new PdfPCell(new Paragraph(shopName, headFont)));
            }
            table.addCell(new PdfPCell(new Paragraph("总计", headFont)));
        }
        return table;
    }

    // 添加分类标识
    private void addTypeName(Document document,String typeName) throws DocumentException {
//        String typeName="fr".equals(language)?honganWmsReportOrderItem.getTypeNameFr():honganWmsReportOrderItem.getTypeName();
        Font frFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Paragraph elements = new Paragraph(typeName,frFont);
        elements.setAlignment(Element.ALIGN_LEFT);
        // 设置底部边距为10
        elements.setSpacingAfter(20);
        Font font = new Font();
        font.setSize(20);
        elements.setFont(font);
        document.add(elements);
    }

    private String getNotMergePdf(Long id, String language, HonganWmsReportOrder reportOrder) throws BaseException {
        if (StringUtils.isEmpty(language)) language = "zh";

        FontFactory.register("/static/assets/tff/simhei.ttf", "Alias");
        FontFactory.register("/static/assets/tff/msyh.ttc", "Alias2");
        Font frFont = FontFactory.getFont("Alias2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font zhFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14F);
        Map<String, String> values = configService.getValues(new ArrayList<String>() {{
            add("system_upload_path");
            add("system_upload_publicPath");
            add("system_upload_imgUrl");
            add("system_upload_copyPath");
            add("system_domain");
        }});
        String fileName = "";
        if (language.equals("fr")) {
            fileName = "(法)" + reportOrder.getBillCode() + ".pdf";
        } else {
            fileName = "(中)" + reportOrder.getBillCode() + ".pdf";
        }
        String directionPath=values.get("system_upload_path") + values.get("system_upload_publicPath");
        File file = new File(directionPath);
        if(!file.exists()||!file.isDirectory()){
            file.mkdirs();
        }
        String filePath = directionPath + fileName;
        List<HonganWmsReportOrderItem> items = itemService.queryListByPid(id);
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));

            document.open();

            // 添加头部，添加日期
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Paragraph header = new Paragraph(format);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            Font font = new Font();
            font.setSize(20);
            header.setFont(font);
            document.add(header);

            // 创建表格
            PdfPTable table = createNotMergePdfTable(language);

            // 上一条数据的商品类型
            Long lastTypeId = null;
            HonganWmsReportOrderItem honganWmsReportOrderItem = items.get(0);
            // 添加分类
            addTypeName(document, "fr".equals(language)?honganWmsReportOrderItem.getTypeNameFr():honganWmsReportOrderItem.getTypeName());
            for (HonganWmsReportOrderItem item : items.stream().
                    sorted(Comparator.comparing(HonganWmsReportOrderItem::getTypeId)).collect(Collectors.toList())
            ) {
                if(Objects.nonNull(lastTypeId)&&lastTypeId!=item.getTypeId()){
                    // 换页
                    document.add(table);
                    document.newPage();

                    // 添加分类
                    addTypeName(document, "fr".equals(language)?item.getTypeNameFr():item.getTypeName());
                    // 创建表格
//                    table = new PdfPTable(5); // 创建一个5列的表格
//                    table.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table = createNotMergePdfTable(language);
                }
                table.addCell(new PdfPCell(new Paragraph(item.getShopName(), zhFont)));
                table.addCell(new PdfPCell(new Paragraph(item.getMaterialName(), frFont)));
//                table.addCell(new PdfPCell(new Paragraph("fr".equals(language)?item.getTypeNameFr():item.getTypeName(), zhFont)));
                table.addCell(new PdfPCell(new Paragraph(item.getStockNumber().stripTrailingZeros().toPlainString(), zhFont)));
                table.addCell(new PdfPCell(new Paragraph(item.getReportNumber().stripTrailingZeros().toPlainString(), zhFont)));
                lastTypeId=item.getTypeId();
            }


            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values.get("system_domain") + values.get("system_upload_imgUrl") + fileName;
    }



    private String getMergePdf(Long id, String language, HonganWmsReportOrder reportOrder) throws BaseException {
        if (StringUtils.isEmpty(language)) language = "zh";
        FontFactory.register("/static/assets/tff/simhei.ttf", "Alias");
        FontFactory.register("/static/assets/tff/msyh.ttc", "Alias2");
//        FontFactory.register("/static/assets/tff/msyh.ttc", "Alias2");
//        FontFactory.register("/static/assets/tff/msyhbd.ttc", "Alias");
//        FontFactory.register("/static/assets/tff/msyhl.ttc", "Alias");
        Font frFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font zhFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14F);
        Font headFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14F);
        Map<String, String> values = configService.getValues(new ArrayList<String>() {{
            add("system_upload_path");
            add("system_upload_publicPath");
            add("system_upload_imgUrl");
            add("system_upload_copyPath");
            add("system_domain");
        }});
        String fileName = "";
        if (language.equals("fr")) {
            fileName = "(法)" + reportOrder.getBillCode() + ".pdf";
        } else {
            fileName = "(中)" + reportOrder.getBillCode() + ".pdf";
        }
        String filePath = values.get("system_upload_path") + values.get("system_upload_publicPath") + fileName;
        List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(id);
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));

            document.open();
            //查询不同的店铺列
            List<String> shopNameList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
                    .select("DISTINCT(shop_name),shop_priority")
                    .eq("pid", reportOrder.getId())
                    .orderByDesc("shop_priority")).stream()
                    .map(HonganWmsReportOrderItem::getShopName)
                    .distinct()
                    .collect(Collectors.toList());
            //查询不同的商品
            List<HonganWmsReportOrderItem> materialList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
                    .select("DISTINCT(material_name),type_id,type_name,type_name_fr,material_priority")
                    .eq("pid", reportOrder.getId())
                    .orderByAsc("type_id")
                    .orderByDesc("material_priority")).stream()
                    .map(i->HonganWmsReportOrderItem.builder()
                            .materialName(i.getMaterialName())
                            .typeName(i.getTypeName())
                            .typeNameFr(i.getTypeNameFr())
                            .typeId(i.getTypeId())
                            .build())
                    .distinct()
                    .collect(Collectors.toList());

            // 添加头部，添加日期
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Paragraph header = new Paragraph(format);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            Font font = new Font();
            font.setSize(20);
            header.setFont(font);
            document.add(header);

            // 创建表格
            PdfPTable table = createMergePdfTable(language,shopNameList);

            // 添加数据行
            // 上一条数据的商品类型
            Long lastTypeId = null;
            HonganWmsReportOrderItem honganWmsReportOrderItem = materialList.get(0);
            // 创建分类名称文本
            addTypeName(document, "fr".equals(language)?honganWmsReportOrderItem.getTypeNameFr():honganWmsReportOrderItem.getTypeName());
            for (HonganWmsReportOrderItem material : materialList.stream()
                    .sorted(Comparator.comparing(HonganWmsReportOrderItem::getTypeId))
                    .collect(Collectors.toList())
            ) {
                // 如果上一条数据是为空（第一次添加）并且当前数据类型与上一条数据类型不同，则另起一页
                if (lastTypeId != null && !lastTypeId.equals(material.getTypeId())) {
                    // 换页
                    document.add(table);
                    document.newPage();

                    // 创建分类名称文本
                    addTypeName(document, "fr".equals(language)?material.getTypeNameFr():material.getTypeName());

                    // 创建表格
                    table = createMergePdfTable(language,shopNameList);
//                    table = new PdfPTable(shopNameList.size() + 3); // 创建一个n列的表格
                }
                List<String> reportNumberItem = new ArrayList<>();
                reportNumberItem.add(material.getMaterialName());
//                reportNumberItem.add("fr".equals(language)? material.getTypeNameFr():material.getTypeName());
                System.out.println("materialName: "+material.getMaterialName()+"======typeName: "+material.getTypeName());
                BigDecimal reportNumberTotal = BigDecimal.ZERO;
                for (String shopName : shopNameList) {
                    BigDecimal reportNumber = itemList.stream()
                            .filter(person -> person.getMaterialName().equals(material.getMaterialName()) && person.getShopName().equals(shopName))
                            .map(HonganWmsReportOrderItem::getReportNumber)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    reportNumberItem.add(reportNumber.stripTrailingZeros().toPlainString());
                    reportNumberTotal = reportNumberTotal.add(reportNumber);
                }
                reportNumberItem.add(reportNumberTotal.stripTrailingZeros().toPlainString());

                // 如果总数为0，则不添加
                if(reportNumberTotal.compareTo(BigDecimal.ZERO) == 0){
                    continue;
                }

                for (String reportItem : reportNumberItem) {
//                    table.addCell(new PdfPCell(new Paragraph(reportItem, headFont)));
                    table.addCell(new PdfPCell(new Paragraph(reportItem, frFont)));
                }

                lastTypeId = material.getTypeId();
            }
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values.get("system_domain") + values.get("system_upload_imgUrl") + fileName;
    }
//    private String getMergePdf(Long id, String language, HonganWmsReportOrder reportOrder) throws BaseException {
//        if (StringUtils.isEmpty(language)) language = "zh";
//        FontFactory.register("/static/assets/tff/simhei.ttf", "Alias");
//        FontFactory.register("/static/assets/tff/msyh.ttc", "Alias2");
////        FontFactory.register("/static/assets/tff/msyh.ttc", "Alias2");
////        FontFactory.register("/static/assets/tff/msyhbd.ttc", "Alias");
////        FontFactory.register("/static/assets/tff/msyhl.ttc", "Alias");
//        Font font = FontFactory.getFont("Alias2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        Font headFont = FontFactory.getFont("Alias", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14F);
//        Map<String, String> values = configService.getValues(new ArrayList<String>() {{
//            add("system_upload_path");
//            add("system_upload_publicPath");
//            add("system_upload_imgUrl");
//            add("system_upload_copyPath");
//            add("system_domain");
//        }});
//        String fileName = "";
//        if (language.equals("fr")) {
//            fileName = "(法)" + reportOrder.getBillCode() + ".pdf";
//        } else {
//            fileName = "(中)" + reportOrder.getBillCode() + ".pdf";
//        }
//        String filePath = values.get("system_upload_path") + values.get("system_upload_publicPath") + fileName;
//        List<HonganWmsReportOrderItem> itemList = itemService.queryListByPid(id);
//        try {
//            Document document = new Document(PageSize.A4);
//            PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
//
//            document.open();
//            //查询不同的店铺列
//            List<String> shopNameList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
//                    .select("DISTINCT(shop_name)")
//                    .eq("pid", reportOrder.getId())).stream()
//                    .map(HonganWmsReportOrderItem::getShopName)
//                    .distinct()
//                    .collect(Collectors.toList());
//            //查询不同的商品
//            List<String> materialNameList = itemService.list(new QueryWrapper<HonganWmsReportOrderItem>()
//                    .select("DISTINCT(material_name)")
//                    .eq("pid", reportOrder.getId())).stream()
//                    .map(HonganWmsReportOrderItem::getMaterialName)
//                    .distinct()
//                    .collect(Collectors.toList());
//            PdfPTable table = new PdfPTable(shopNameList.size() + 2); // 创建一个n列的表格
//            if (language.equals("fr")) {
//                table.addCell(new PdfPCell(new Paragraph("Produits", headFont)));
////                table.addCell(new PdfPCell(new Paragraph("Type", headFont)));
//                for (String shopName : shopNameList) {
//                    table.addCell(new PdfPCell(new Paragraph(shopName, headFont)));
//                }
//                table.addCell(new PdfPCell(new Paragraph("Total", headFont)));
//            } else {
//                table.addCell(new PdfPCell(new Paragraph("产品", headFont)));
////                table.addCell(new PdfPCell(new Paragraph("类型", headFont)));
//                for (String shopName : shopNameList) {
//                    table.addCell(new PdfPCell(new Paragraph(shopName, headFont)));
//                }
//                table.addCell(new PdfPCell(new Paragraph("总计", headFont)));
//            }
//
//            for (String materialName : materialNameList) {
//                List<String> stockNumberItem = new ArrayList<>();
//                List<String> reportNumberItem = new ArrayList<>();
//                stockNumberItem.add(materialName);
//                reportNumberItem.add(materialName);
////                if (language.equals("fr")) {
////                    stockNumberItem.add("Stock");
////                } else {
////                    stockNumberItem.add("库存量");
////                }
////                if (language.equals("fr")) {
////                    reportNumberItem.add("Déclarées");
////                } else {
////                    reportNumberItem.add("报货量");
////                }
//                BigDecimal stockNumberTotal = BigDecimal.ZERO;
//                BigDecimal reportNumberTotal = BigDecimal.ZERO;
//                for (String shopName : shopNameList) {
//                    BigDecimal stockNumber = itemList.stream()
//                            .filter(person -> person.getMaterialName().equals(materialName) && person.getShopName().equals(shopName))
//                            .map(HonganWmsReportOrderItem::getStockNumber)
//                            .reduce(BigDecimal.ZERO, BigDecimal::add);
//                    BigDecimal reportNumber = itemList.stream()
//                            .filter(person -> person.getMaterialName().equals(materialName) && person.getShopName().equals(shopName))
//                            .map(HonganWmsReportOrderItem::getReportNumber)
//                            .reduce(BigDecimal.ZERO, BigDecimal::add);
//                    stockNumberItem.add(stockNumber.stripTrailingZeros().toPlainString());
//                    reportNumberItem.add(reportNumber.stripTrailingZeros().toPlainString());
//                    stockNumberTotal = stockNumberTotal.add(stockNumber);
//                    reportNumberTotal = reportNumberTotal.add(reportNumber);
//                }
//                stockNumberItem.add(stockNumberTotal.stripTrailingZeros().toPlainString());
//                reportNumberItem.add(reportNumberTotal.stripTrailingZeros().toPlainString());
//                int index = 0;
//                for (String reportItem : reportNumberItem) {
//                    table.addCell(new PdfPCell(new Paragraph(reportItem, font)));
//                    index++;
//                }
//
////                int reportIndex = 0;
////                for (String stockItem : stockNumberItem) {
////                    if (reportIndex == 0) {
////                        Paragraph paragraph = new Paragraph(stockItem, font);
////                        PdfPCell pdfPCell = new PdfPCell(paragraph);
////                        pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
////                        pdfPCell.setRowspan(2);
////                        table.addCell(pdfPCell);
////                    } else {
////                        table.addCell(new PdfPCell(new Paragraph(stockItem, font)));
////                    }
////                    reportIndex++;
////                }
////                int index = 0;
////                for (String reportItem : reportNumberItem) {
////                    if (index > 0) {
////                        table.addCell(new PdfPCell(new Paragraph(reportItem, font)));
////                    }
////                    index++;
////                }
//            }
//            document.add(table);
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return values.get("system_domain") + values.get("system_upload_imgUrl") + fileName;
//    }
}
