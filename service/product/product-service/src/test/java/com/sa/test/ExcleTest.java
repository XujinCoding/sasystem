package com.sa.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sa.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcleTest {
    @Test
    public void xxx() {
        EasyExcel.read(new File("E:\\product.xlsx"), Product.class, new DemoDataListener()).sheet().doRead();
//        EasyExcel.read("E:\\product.xlsx", Product.class, new DemoDataListener()).sheet().doRead();
    }


    @Test
    public void testTxt() throws IOException {
        File file = new File("E://product.txt");
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        List<Product> list = new ArrayList<>();
        while ((str = bufferedReader.readLine()) != null) {
            String[] s = str.split("/");
            Product product = new Product(Long.parseLong(s[0]), s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3]), s[4]);
            list.add(product);
            System.out.println(str);
        }
        System.out.println(JSON.toJSONString(list));
        //close
        inputStream.close();
        bufferedReader.close();
    }

    @Test
    public void testText1() {
        StringBuffer content = new StringBuffer("");// 文档内容
        try {
            FileReader reader = new FileReader("E:\\product.xlsx");
            BufferedReader br = new BufferedReader(reader);
            String s1 = null;

            while ((s1 = br.readLine()) != null) {
                content.append(s1 + " ");
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content.toString().trim());
    }

    @Test
    public void testExcel() throws IOException {
        List<String> list = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream("E://product.xlsx");
        XSSFWorkbook sheets = (XSSFWorkbook) XSSFWorkbookFactory.create(inputStream);
        inputStream.close();
        Sheet sheetAt = sheets.getSheetAt(0);
        int lastRowNum = sheetAt.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheetAt.getRow(i);
            int lastCellNum = row.getLastCellNum();
            Map<String, Object> map = new HashMap<>();
            for (int cellNo = 0; cellNo < lastCellNum; cellNo++) {
                Cell cell = row.getCell(cellNo);
                CellType cellType = cell.getCellType();
                Object cellValue = null;
                if (cellType.equals("STRING")){
                     cellValue= cell.getStringCellValue();
                }else if(cellType.equals("NUMBER")){
                    cellValue = cell.getNumericCellValue();
                }
                map.put(cell + "", cellValue);
            }
            list.add(JSON.toJSONString(map));
        }
        System.out.println(JSON.toJSONString(list));
    }
}
