package com.hongan.template.base.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExportDataUtils {

    //获取字体
    public static Font getBigFont(HSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        return font;
    }

    //获取表格头部样式
    public static HSSFCellStyle getTableHeaderStyle(HSSFWorkbook workbook) {
        //仅字体居中样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setFont(getBigFont(workbook));
        return cellStyle;
    }

    //获取仅居中样式
    public static HSSFCellStyle getCenterStyle(HSSFWorkbook workbook) {
        //仅字体居中样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    //获取居中带边框样式
    public static HSSFCellStyle getCenterBorderStyle(HSSFWorkbook workbook) {
        //设置居中带边框
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    /**
     * 表格居中合并放大字体
     */
    public static Integer generaHeadMergedRegion(HSSFWorkbook workbook, HSSFSheet sheet, int startRow, HSSFCellStyle cellStyle, int firstRow, int lastRow, int firstCol, int lastCol, String value) {
        HSSFRow row = sheet.createRow(startRow);//设置第一行，从零开始
        HSSFCell firstRowCell = row.createCell(0);
        // 设置需要合并的单元格
        CellRangeAddress mergedRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(mergedRegion);
        firstRowCell.setCellValue(value);
        firstRowCell.setCellStyle(cellStyle == null ? getTableHeaderStyle(workbook) : cellStyle);
        startRow++;
        return startRow;
    }

    /**
     * 表格居中合并放大字体
     */
    public static Integer generaHeadMergedRegion(HSSFWorkbook workbook, HSSFSheet sheet, int startRow, HSSFCellStyle cellStyle, int firstRow, int lastRow, int firstCol, int lastCol, Integer height, String value) {
        HSSFRow row = sheet.createRow(startRow);//设置第一行，从零开始
        row.setHeight(height.shortValue());
        HSSFCell firstRowCell = row.createCell(0);
        // 设置需要合并的单元格
        CellRangeAddress mergedRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(mergedRegion);
        firstRowCell.setCellValue(value);
        firstRowCell.setCellStyle(cellStyle == null ? getTableHeaderStyle(workbook) : cellStyle);
        startRow++;
        return startRow;
    }

    //合并单元格
    public static void mergedRegion(HSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress mergedRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(mergedRegion);
    }

    /**
     * @param sheet
     * @param cellStyle 单元格样式
     * @param height    单元格高度
     * @param data      表头数据
     */
    public static Integer generaHead(HSSFSheet sheet, HSSFCellStyle cellStyle, Integer startRow, Integer height, List<List<String>> data) {
        HSSFRow row0 = sheet.createRow(startRow);//设置第一行，从零开始
        row0.setHeight(height.shortValue());
        //设置表格宽度
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) != null) {
                sheet.setColumnWidth(i, Integer.parseInt(data.get(i).get(0)) * 256);
                HSSFCell cell = row0.createCell(i);
                cell.setCellValue(data.get(i).get(1));
                cell.setCellStyle(cellStyle);
            }
        }
        startRow++;
        return startRow;
    }


    /**
     * @param sheet
     * @param cellStyle 单元格样式
     * @param height    单元格高度
     * @param data      表头数据
     */
    public static void generaHead(HSSFSheet sheet, HSSFCellStyle cellStyle, Integer height, List<List<String>> data) {
        HSSFRow row0 = sheet.createRow(0);//设置第一行，从零开始
        row0.setHeight(height.shortValue());
        //设置表格宽度
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) != null) {
                sheet.setColumnWidth(i, Integer.parseInt(data.get(i).get(0)) * 256);
                HSSFCell cell = row0.createCell(i);
                cell.setCellValue(data.get(i).get(1));
                cell.setCellStyle(cellStyle);
            }
        }
    }


    public static Integer generaCell(HSSFRow row, HSSFCellStyle cellStyle, Integer column, String data) {
        HSSFCell cell = row.createCell(column);
        if (data != null) {
            cell.setCellValue(data);
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(cellStyle);
        column = column + 1;
        return column;
    }

    public static Integer generaCell(HSSFRow row, HSSFCellStyle cellStyle, Integer column, Integer data) {
        HSSFCell cell = row.createCell(column);
        if (data != null) {
            cell.setCellValue(data);
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(cellStyle);
        column = column + 1;
        return column;
    }

    public static Integer generaCell(HSSFRow row, HSSFCellStyle cellStyle, Integer column, BigDecimal data) {
        HSSFCell cell = row.createCell(column);
        if (data != null) {
            cell.setCellValue(data.doubleValue());
        } else {
            cell.setCellValue(0);
        }
        cell.setCellStyle(cellStyle);
        column = column + 1;
        return column;
    }

    public static Integer generaCell(HSSFRow row, HSSFCellStyle cellStyle, Integer column, LocalDateTime data) {
        HSSFCell cell = row.createCell(column);
        if (data != null) {
            cell.setCellValue(data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(cellStyle);
        column = column + 1;
        return column;
    }


    public static Integer generaCell(HSSFRow row, HSSFCellStyle cellStyle, Integer column, LocalDate data) {
        HSSFCell cell = row.createCell(column);
        if (data != null) {
            cell.setCellValue(data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(cellStyle);
        column = column + 1;
        return column;
    }

    //生成图片
    public static Integer generaImg(HSSFRow row, HSSFCellStyle cellStyle, HSSFPatriarch patriarch, HSSFWorkbook workbook, int start_cell, Integer start, String imgUrl) throws Exception {
        if (StringUtils.isNotEmpty(imgUrl)) {
            try {
                byte[] data = getImg(imgUrl);
                //anchor主要用于设置图片的属性
                //设置图片位置
                // 起始单元格中的x,y坐标. 结束单元格中的x,y坐标 指定起始的单元格，下标从0开始  指定结束的单元格 ，下标从0开始
                HSSFClientAnchor anchor = new HSSFClientAnchor(10, 10, 0, 0, (short) (start_cell), start, (short) (start_cell + 1), start + 1);
                patriarch.createPicture(anchor, workbook.addPicture(data, HSSFWorkbook.PICTURE_TYPE_PNG));
                start_cell = start_cell + 1;
                return start_cell;
            } catch (Exception e) {
                return generaCell(row, cellStyle, start_cell, "");
            }
        } else {
            return generaCell(row, cellStyle, start_cell, "");
        }
    }

    private static byte[] getImg(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(3  * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        return readInputStream(inStream);
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
