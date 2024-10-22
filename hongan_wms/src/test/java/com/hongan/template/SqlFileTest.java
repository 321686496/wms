package com.hongan.template;

import org.junit.jupiter.api.Test;

import java.io.*;

public class SqlFileTest {
    @Test
    public void test2() throws IOException {
        String path = System.getProperty("user.dir");
        //定义输出目录
//        String FileOut = path + "\\sql_all\\sql_all.sql";
        String FileOut = "C:\\Users\\Administrator\\Desktop\\sql_all.sql";
        BufferedWriter bw = new BufferedWriter(new FileWriter(FileOut));

        //读取目录下的每个文件或者文件夹，并读取文件的内容写到目标文字中去
        File[] list = new File(path + "\\sql").listFiles();
        int fileCount = 0;
        int folderConut = 0;
        for (File file : list) {
            if (file.isFile()) {
                fileCount++;
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                }
                br.close();
            } else {
                folderConut++;
            }
        }
        bw.close();
    }
}
