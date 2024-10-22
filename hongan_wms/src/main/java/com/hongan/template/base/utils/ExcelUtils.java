package com.hongan.template.base.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-04-24 14:57
 * @Description: ExcelUtils
 */

@Slf4j
public class ExcelUtils implements Iterator<Row> {

    public static final String EXCEL_XLS = "application/vnd.ms-excel";
    public static final String EXCEL_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private static final String EXCEL_2003 = "xls";
    private static final String EXCEL_2007 = "xlsx";
    private static final int DEFAULT_SHEET_INDEX = 0;
    private int readCurrentRow = 0;
    private int writeCurrentRow = 0;
    private Workbook workbook;
    private List<KeyTitle> headers;
    private Sheet sheet;
    private File file;

    public ExcelUtils(String filepath) throws IOException, InvalidFormatException {
        String tp = filepath.substring(filepath.lastIndexOf('.') + 1);
        if (!(EXCEL_2003.equals(tp) || EXCEL_2007.equals(tp))) {
            throw new IOException(String.format("Filepath is not excel file. [%s]", filepath));
        }
        this.file = new File(filepath);
        switch (tp) {
            case EXCEL_2003:
                if (this.file.exists() && this.file.isFile() && this.file.canRead() && this.file.length() > 0) {
                    POIFSFileSystem poiFile = new POIFSFileSystem(this.file);
                    workbook = new HSSFWorkbook(poiFile);
                } else {
                    workbook = new HSSFWorkbook();
                    workbook.createSheet();
                }
                break;
            case EXCEL_2007:
                if (this.file.exists() && this.file.isFile() && this.file.canRead() && this.file.length() > 0) {
                    workbook = new XSSFWorkbook(new FileInputStream(this.file));
                } else {
                    workbook = new XSSFWorkbook();
                    workbook.createSheet();
                }
                break;
            default:
                log.warn("Excel type must be xls(2003) or xlsx(2007), use default xlsx(2007)");
                workbook = new XSSFWorkbook(new FileInputStream(this.file));
        }
        sheet = workbook.getSheetAt(DEFAULT_SHEET_INDEX);
    }

    public ExcelUtils(String filepath, List<KeyTitle> headers) throws IOException, InvalidFormatException {
        this(filepath);
        this.headers = headers;
    }

    public ExcelUtils(String tp, List<KeyTitle> headers, int sheetIndex) throws IOException, InvalidFormatException {
        this(tp, headers);
        sheet = workbook.getSheetAt(sheetIndex);
    }

    public ExcelUtils(String tp, List<KeyTitle> headers, String sheetName) throws IOException, InvalidFormatException {
        this(tp, headers);
        sheet = workbook.getSheet(sheetName);
    }

    public void setActiveSheet(int index) {
        sheet = workbook.getSheetAt(index);
        readCurrentRow = 0;
        writeCurrentRow = 0;
    }

    public int getActiveSheet() {
        return workbook.getActiveSheetIndex();
    }

    public void writeRowList(List<Object> data) {
        Row row = this.sheet.createRow(this.writeCurrentRow);
        for (int i = 0; i < data.size(); i++) {
            Object o = data.get(i);
            setRowValue(row, i, o);
        }
        this.writeCurrentRow++;
    }

    public List<Object> readRowList(Row row) {
        List<Object> lineValue = new ArrayList<>();
        for (Iterator<Cell> it = row.cellIterator(); it.hasNext(); ) {
            Cell cell = it.next();
            lineValue.add(readCell(cell));
        }
        return lineValue;
    }

    public Map<String, Object> readRowMap(Row row) {
        List<Object> lineValue = readRowList(row);
        Map<String, Object> map = new HashMap<>();
        int size = lineValue.size();
        for (int i = 0; i < size; i++) {
            map.put(getHeaderKey(i), lineValue.get(i));
        }
        return map;
    }

    public String getHeaderKey(int i) {
        if (this.headers != null && this.headers.size() > i) {
            return this.headers.get(i).getKey();
        }
        return String.valueOf(i);
    }

    public void setRowValue(Row row, int i, Object o) {
        if (o == null) {
            Cell cell = row.createCell(i, CellType.BLANK);
        } else if (o instanceof String) {
            Cell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue((String) o);
        } else if (o instanceof Boolean) {
            Cell cell = row.createCell(i, CellType.BOOLEAN);
            cell.setCellValue((Boolean) o);
        } else if (o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float) {
            Cell cell = row.createCell(i, CellType.NUMERIC);
            cell.setCellValue((Double) o);
        } else {
            Cell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue((String) o);
        }
    }

    public CellType getObjectType(Object o) {
        if (o == null) {
            return CellType.BLANK;
        } else if (o instanceof String) {
            return CellType.STRING;
        } else if (o instanceof Boolean) {
            return CellType.BOOLEAN;
        } else if (o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float) {
            return CellType.NUMERIC;
        } else {
            return CellType.STRING;
        }
    }

    public Object readCell(Cell cell) {
        CellType cellType = cell.getCellType();

        Object value = null;
        switch (cellType) {
            case STRING: // 字符串类型
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:// boolean类型
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:// 数字类型(包含日期和普通数字)
                // 日期：
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    // 数字
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: // 公式类型
                value = cell.getCellFormula();
                break;
            default:
                break;
        }

        return value;
    }

    public <T> T readCell(Cell cell, Class<T> classz) throws IOException {
        Object value = readCell(cell);
        if (value == null) return null;
        if (value.getClass() == classz) {
            return (T) value;
        }
        throw new IOException("Excel 单元格 格式有误");
    }

    public void writeFile(boolean append) throws IOException {
        OutputStream stream = new FileOutputStream(this.file, append);
        workbook.write(stream);
        workbook.close();
        stream.close();
    }

    @Override
    public boolean hasNext() {
        return readCurrentRow < sheet.getLastRowNum();
    }

    @Override
    public Row next() {
        return sheet.getRow(readCurrentRow++);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public List<KeyTitle> getHeaders() {
        return headers;
    }

    public void setHeaders(List<KeyTitle> headers) {
        this.headers = headers;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Data
    @AllArgsConstructor
    public static class KeyTitle {
        String key;
        String title;
    }


    public static String getValue(HSSFCell cell) {
        if (cell != null) {
            // 注意：一定要设成这个，否则可能会出现乱码
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            switch (cell.getCellType().ordinal()) {
                case 1:
                    BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
                    return bigDecimal.toPlainString();
                case 2:
                    return cell.getStringCellValue();
                default:
                    return "";
            }
        }
        return "";
    }

    public static String getValue(Cell cell) {
        if (cell != null) {
            // 注意：一定要设成这个，否则可能会出现乱码
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            switch (cell.getCellType().ordinal()) {
                case 1:
                    BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
                    return bigDecimal.toPlainString();
                case 2:
                    return cell.getStringCellValue();
                default:
                    return "";
            }
        }
        return "";
    }

//    public static String getStringValue(HSSFCell cell) {
//        if (cell != null) {
//            // 注意：一定要设成这个，否则可能会出现乱码
//            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//            switch (cell.getCellType().ordinal()) {
//                case 1:
////                    return bigDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString();
////                    Double cellValue = cell.getNumericCellValue();
////                    return cellValue.intValue()+"";
//                    BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
////                    String s = bigDecimal.toPlainString();
////                    System.out.println(s);
//                    return bigDecimal.toPlainString();
//                case 2:
//                    return cell.getStringCellValue();
//                default:
//                    return "";
//            }
//        }
//        return "";
//    }

    public static String getStringValue(Cell cell) {
        if (cell != null) {
            // 注意：一定要设成这个，否则可能会出现乱码
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            switch (cell.getCellType().ordinal()) {
                case 1:
//                    return bigDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString();
//                    Double cellValue = cell.getNumericCellValue();
//                    return cellValue.intValue()+"";
                    BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
//                    String s = bigDecimal.toPlainString();
//                    System.out.println(s);
                    return bigDecimal.toPlainString();
                case 2:
                    return cell.getStringCellValue();
                default:
                    return "";
            }
        }
        return "";
    }

    public static BigDecimal getBigDecimalValue(HSSFCell cell) {
        if (cell != null) {
            // 注意：一定要设成这个，否则可能会出现乱码
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            try {
                switch (cell.getCellType().ordinal()) {
                    case 1:
                        BigDecimal bigDecimal = BigDecimal.valueOf(cell.getNumericCellValue());
                        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
                    case 2:
                        return new BigDecimal(cell.getStringCellValue());
                    default:
                        return BigDecimal.ZERO;
                }
            } catch (Exception e) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal getBigDecimalValue(Cell cell) {
        if (cell != null) {
            // 注意：一定要设成这个，否则可能会出现乱码
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            try {
                switch (cell.getCellType().ordinal()) {
                    case 1:
                        BigDecimal bigDecimal = BigDecimal.valueOf(cell.getNumericCellValue());
                        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
                    case 2:
                        return new BigDecimal(cell.getStringCellValue());
                    default:
                        return BigDecimal.ZERO;
                }
            } catch (Exception e) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public static Integer getIntegerValue(HSSFCell cell) {
        if (cell != null) {
            // 注意：一定要设成这个，否则可能会出现乱码
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            switch (cell.getCellType().ordinal()) {
                case 1:
                    Double d = cell.getNumericCellValue();
                    return d.intValue();
                default:
                    return 0;
            }
        }
        return 0;
    }

    public static Integer getIntegerValue(Cell cell) {
        if (cell != null) {
            // 注意：一定要设成这个，否则可能会出现乱码
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            switch (cell.getCellType().ordinal()) {
                case 1:
                    Double d = cell.getNumericCellValue();
                    return d.intValue();
                default:
                    return 0;
            }
        }
        return 0;
    }
}

