package com.hongan.template.batch;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hongan.template.BaseApplication;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.hongan.template.wms.service.impl.HonganWmsMaterialServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@SpringBootTest(classes= BaseApplication.class)
public class TestMaterialBatch {

    @Autowired
    private IHonganWmsMaterialService materialService;

    @Test
    void testMain(){
    }

    @Test
    void testBatchAddMaterial() throws Exception {
        // 读取xlsx文件
        String path = "E:/parttime_project/wms_vegetable/OLIVES.xlsx";
        XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(path));
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        Iterator<Row> iterator = sheetAt.iterator();
        List<HonganWmsMaterial> list=new ArrayList<>();

        // 获取最后一个物料的权重数
        List<HonganWmsMaterial> listed = materialService.list(new LambdaQueryWrapper<HonganWmsMaterial>());
        HonganWmsMaterial one = materialService.list(new LambdaQueryWrapper<HonganWmsMaterial>()
                .orderByDesc(HonganWmsMaterial::getPriority)
        ).get(0);
        System.out.println("max priority: "+one.getPriority());
        int maxPriority=one.getPriority();
        int count=0;
        while (iterator.hasNext()) {
            Row next = iterator.next();
            // 忽略标题行
//            if(count++<=1)continue;
            Iterator<Cell> cellIterator = next.cellIterator();
            int index=0;
            HonganWmsMaterial material = new HonganWmsMaterial();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if(index==0){
                    material.setName(cell.getStringCellValue());
                }
                if(index==1){
                    material.setCode(cell.getStringCellValue());
                }
                index++;
            }
            material.setTypeId(6l);
            material.setPriority(++maxPriority);
            list.add(material);
        }
        for (HonganWmsMaterial honganWmsMaterial : list) {
            System.out.println(honganWmsMaterial);
            try{
                materialService.create(honganWmsMaterial);
            }catch (BaseException e){
                if(e.getRemark().equals("编码重复")){
                    // 修改物料类型
                    HonganWmsMaterial material = materialService.getOne(new LambdaQueryWrapper<HonganWmsMaterial>()
                            .eq(HonganWmsMaterial::getCode, honganWmsMaterial.getCode())
//                            .eq(HonganWmsMaterial::getName, honganWmsMaterial.getName())
                    );
                    material.setTypeId(6l);
                    materialService.update(material);
                }
            }
        }
        System.out.println("total count: "+list.size());
    }

    @Test
    void testBatchModifyTypeOfMaterial() throws BaseException {
        // 要修改的物料的权重范围
        int[] range=new int[]{1,186};
        // 修改的物料类型编号
        long typeID=3L;

        // 获取该权重范围内的所有物料
        List<HonganWmsMaterial> list = materialService.list(new LambdaQueryWrapper<HonganWmsMaterial>()
                .ge(HonganWmsMaterial::getPriority,range[0])
                .le(HonganWmsMaterial::getPriority, range[1])
        );
        for (HonganWmsMaterial honganWmsMaterial : list) {
            // 修改物料类型
            honganWmsMaterial.setTypeId(typeID);
            materialService.update(honganWmsMaterial);
        }
    }
}
