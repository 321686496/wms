package com.hongan.template;

import com.alipay.api.domain.Material;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.ExcelExportDataUtils;
import com.hongan.template.base.utils.ExcelUtils;
import com.hongan.template.base.utils.FileUtils;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.hongan.template.wms.service.impl.HonganWmsMaterialServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = BaseApplication.class)
public class TestMaterial {

    @Data
    @AllArgsConstructor
    static class Header {
        private String chinese;
        private String key;
    }

    // 表头行
    public static final Header[] headers= new Header[]{
            new Header("编号", "id"),
            new Header("名称", "name"),
            new Header("编码", "code"),
            new Header("是否可用", "enable"),
            new Header("备注", "remark"),
            new Header("权重", "priority"),
            new Header("创建时间", "createdAt"),
            new Header("修改时间", "updatedAt"),
            new Header("是否删除", "deleted"),
            new Header("类型编号", "typeId"),
    };



    @Autowired
    private IHonganWmsMaterialService honganWmsMaterialService;


    // 从excel表格中读取数据
    @Test
    void testListMaterialFromExcel() throws IOException, BaseException {
        String path = "E:/template.xlsx";
        XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(path));
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        Iterator<Row> iterator = sheetAt.iterator();
        List<HonganWmsMaterial> list=new ArrayList<>();
        int count=0;
        while (iterator.hasNext()) {
            Row next = iterator.next();
//            count++;
            if(count++<=1)continue;
            Iterator<Cell> cellIterator = next.cellIterator();
            int index=0;
            HonganWmsMaterial material = new HonganWmsMaterial();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if(index==1){
                    material.setName(cell.getStringCellValue());
                }
                if(index==2){
                    material.setCode(cell.getStringCellValue());
                }
                if(index==9){
                    material.setTypeId(Long.valueOf(cell.getStringCellValue()));
                }
                index++;
            }
            list.add(material);
        }
        for (HonganWmsMaterial honganWmsMaterial : list) {
            System.out.println(honganWmsMaterial);
            honganWmsMaterialService.create(honganWmsMaterial);
        }

    }


    // 添加数据到excel表格
    @Test
    void testListMaterial() throws IllegalAccessException, IOException {
//        ExcelExportDataUtils.gener
//        ExcelExportDataUtils
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet("sheet1");

        // 表头样式
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int rowCount = 0;
        int columnCount = 0;
        // 中文行
        XSSFRow chineseRow = sheet1.createRow(rowCount++);
        // 英文字段行
        XSSFRow keyRow = sheet1.createRow(rowCount++);
        for (Header header : headers) {

            Cell chineseRowCell = chineseRow.createCell(columnCount);
            chineseRowCell.setCellStyle(headerCellStyle);
            chineseRowCell.setCellValue(header.getChinese());
            XSSFCell keyRowCell = keyRow.createCell(columnCount++);
            keyRowCell.setCellStyle(headerCellStyle);
            keyRowCell.setCellValue(header.getKey());

//            rowCount++;
        }

        // 获取数据
        List<HonganWmsMaterial> list = honganWmsMaterialService.list();

        // 获取字段（父类BaseEntity也获取）
        List<Field> fields = fields(HonganWmsMaterial.class, 2);

        // headers转为list
        List<Header> headerList = Arrays.stream(headers).collect(Collectors.toList());

        // 添加数据
        for (HonganWmsMaterial material : list) {
            columnCount=0;
            XSSFRow row = sheet1.createRow(rowCount++);
            for (Header header : headerList) {
                String key = header.key;
                Optional<Field> first = fields.stream().filter(field -> field.getName().equals(key)).findFirst();
                if (first.isPresent()) {
                    Field field = first.get();
                    field.setAccessible(true);
                    XSSFCell cell = row.createCell(columnCount++);
                    cell.setCellValue(field.get(material).toString());
                }
            }
        }

        // 设置表宽度，跟据最长内容自动调整
        for (int i = 0; i < headerList.size(); i++) {
            sheet1.autoSizeColumn(i);
            sheet1.setColumnWidth(i, sheet1.getColumnWidth(i) + 256);
        }


        // 写入表格
        String path="E:/";
//        InputStream inputStream=new FileInputStream(path);
        OutputStream outputStream=new FileOutputStream("E:/template.xlsx");
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Test
    void testFields(){
        System.out.println("字段数： "+fields(HonganWmsMaterial.class, 2).size());
        for (Field field : fields(HonganWmsMaterial.class, 2)) {
            System.out.println("field name: "+field.getName());
        }
    }

    // 获取某个对象的所有字段
    List<Field> fields(Class<?> clazz, int depth){
//        Field[] fields = clazz.getDeclaredFields();
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
        if(depth>1){
            fields.addAll(fields(clazz.getSuperclass(), depth-1));
            return fields;
        }
        return fields;
    }

    @Test
    void testClass() throws IllegalAccessException {
        // 获取数据
        List<HonganWmsMaterial> list = honganWmsMaterialService.list();

        // 获取该类的所有字段
        Class<HonganWmsMaterial> materialClass = HonganWmsMaterial.class;
        Class<? super HonganWmsMaterial> superclass = materialClass.getSuperclass();
        Field[] superFields = superclass.getDeclaredFields();
        Field[] fields = materialClass.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(superFields).collect(Collectors.toList());
        fieldList.addAll(Arrays.asList(fields));
        System.out.println("field count: "+fieldList.size());

        // headers转为list
        List<Header> headerList = Arrays.stream(headers).collect(Collectors.toList());

        String row="";
        for (Field field : fieldList) {
//            headerList.stream().any
            // 如果headers中不存在该字段
            if(headerList.stream().noneMatch(header -> header.getKey().equals(field.getName())))continue;
            row+="\t"+field.getName();
        }
        System.out.println(row);
        // 添加数据
        for (HonganWmsMaterial material : list) {
            row="";
            for (Field field : fieldList) {
                // 如果headers中不存在该字段
                if(headerList.stream().noneMatch(header -> header.getKey().equals(field.getName())))continue;

                field.setAccessible(true);
                row+="\t"+field.get(material).toString();
            }
            System.out.println(row);
            // serialVersionUID	id	createdAt	updatedAt	deleted	serialVersionUID	name	code	typeId	enable	remark	priority
        }
        System.out.println("总条数: "+list.size());
    }

    @Test
    void testClassColumn(){
        Class<HonganWmsMaterial> materialClass = HonganWmsMaterial.class;
        Class<? super HonganWmsMaterial> superclass = materialClass.getSuperclass();
        Field[] superFields = superclass.getDeclaredFields();
        Field[] fields = materialClass.getDeclaredFields();

        // 合并superFields与fields数组
        // 将superFields转化为List
        List<Field> fieldList = Arrays.stream(superFields).collect(Collectors.toList());
        System.out.println("super field count: "+fieldList.size());
        fieldList.addAll(Arrays.asList(fields));
        System.out.println("field count: "+fieldList.size());
        String row="";
        for (Field field : fieldList) {
            row+="\t"+field.getName();
        }
        System.out.println(row);
    }
}
