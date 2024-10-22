package com.hongan.template;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import com.hongan.template.wms.service.IHonganWmsReportOrderItemService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

//@SpringBootTest
public class TestPdf {
    @Autowired
    private IHonganWmsReportOrderItemService itemService;

    @Test
    public void test2() throws IOException {
        String filePath = "/static/assets/tff/simhei.ttf";
        ClassPathResource readFile = new ClassPathResource(filePath);
        // 获取文件对象
        File file = readFile.getFile();
        file.getPath();
        System.out.println(file.getName());
    }

    @Test
    public void test1() throws BaseException {
        String filePath = "F:\\test\\20240627.pdf";

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));

            document.open();

//            Font font = FontFactory.getFont("Simsun", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

//            Font font = FontFactory.getFont("Simsun", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED,16f, Font.NORMAL, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(4); // 创建一个3列的表格

            FontFactory.register("F:\\test\\simhei.ttf", "Alia1s");
            Font font = FontFactory.getFont("Alia1s", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            table.addCell(new PdfPCell(new Paragraph("物料", font))); // 第一列
            table.addCell(new PdfPCell(new Paragraph("店铺", font))); // 第二列
            table.addCell(new PdfPCell(new Paragraph("库存数量", font))); // 第三列
            table.addCell(new PdfPCell(new Paragraph("REPORT", font))); // 第三列

//            // 添加数据行
            table.addCell("Alice");
            table.addCell("25");
            table.addCell("Female");
            table.addCell("Female");

            table.addCell("Bob");
            table.addCell("30");
            table.addCell("Male");
            table.addCell("Male");

            document.add(table);

            document.close();

            System.out.println("PDF exported successfully. Path: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test() throws BaseException {
        String filePath = "F:\\test\\20240625.pdf";

        List<HonganWmsReportOrderItem> items = itemService.queryListByPid(3L);

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));

            document.open();

            PdfPTable table = new PdfPTable(4); // 创建一个3列的表格

            table.addCell(new PdfPCell(new Phrase("物料"))); // 第一列
            table.addCell(new PdfPCell(new Phrase("店铺"))); // 第二列
            table.addCell(new PdfPCell(new Phrase("库存数量"))); // 第三列
            table.addCell(new PdfPCell(new Phrase("报货数量"))); // 第三列

            for (HonganWmsReportOrderItem item : items) {
                table.addCell(item.getMaterialName());
                table.addCell(item.getShopName());
                table.addCell(item.getStockNumber().toPlainString());
                table.addCell(item.getReportNumber().toPlainString());
            }
//
//
//            // 添加数据行
//            table.addCell("Alice");
//            table.addCell("25");
//            table.addCell("Female");
//
//            table.addCell("Bob");
//            table.addCell("30");
//            table.addCell("Male");

            document.add(table);

            document.close();

            System.out.println("PDF exported successfully. Path: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
