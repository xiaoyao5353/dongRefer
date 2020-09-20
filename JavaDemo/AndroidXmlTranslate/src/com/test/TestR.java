package com.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.poi.translate.ExcelBean;
import org.poi.translate.PoiTranslate;
import org.poi.translate.ReadExcel;
import org.xml.translate.XmlUtil;

public class TestR {
    public static void main(String[] args) {

        File file = new File("D:\\fy");
        File[] fs = file.listFiles();
        Map<String, Map<String, String>> directorMap = null;
        for (File f : fs) {
            System.out.println(f.getAbsolutePath());
            if (directorMap == null) {
                directorMap = XmlUtil.getDirectorMap(f.getAbsolutePath(), "");
                continue;
            }
            XmlUtil.addDirctorMap(directorMap,
                    XmlUtil.getDirectorMap(f.getAbsolutePath(), ""));
        }

        ReadExcel re = new ReadExcel();
        List<String> list = new ArrayList<String>();
        try {
            Map<String, ExcelBean> map = re.readXlsx("D:\\hsfy.xls");
            for (Entry<String, ExcelBean> entry : map.entrySet()) {
                if (directorMap.get("fa").containsKey(entry.getKey())) {
                    System.out.println("==" + entry.getKey());
                    continue;
                }
                list.add(entry.getKey());
                System.out.println(entry.getKey());
            }
            // System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(list.size());
        PoiTranslate.writeListToXlsx("D://testList.xlsx", list, new String[] {
                "英语", "波斯", "阿拉伯" });

        // PoiTranslate.writeDirectorToXlsx("D://test.xlsx",directorMap);
    }
}
