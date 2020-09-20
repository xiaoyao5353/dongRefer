package org.poi.translate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.xml.translate.KeyBean;
import org.xml.translate.ToTranslatXml;
import org.xml.translate.TranslaterInterface;

import com.test.OfflineXlsToXml;

public class PoiTranslate implements TranslaterInterface {

    private boolean isInit = false;
    private Map<String, Map<String, String>> map;

    public PoiTranslate() {
        if (!isInit) {
            map = new ReadExcel().getAllDirectorMap(OfflineXlsToXml.dictionaryDir, "en");
            System.out.println("map size:" + map.size());
            isInit = true;
        }
    }

    public static void writeDirectorToXlsx(String path,
            Map<String, Map<String, String>> directorMap) {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // create head
        Row headerRow = sheet.createRow(0);
        Cell headercell = headerRow.createCell(0);
        // 0 cell is ""
        headercell.setCellValue("en");
        int i = 1;
        String firsLanguage = null;
        List<String> languageList = new ArrayList<>();
        for (Entry<String, Map<String, String>> entry : directorMap.entrySet()) {
            headercell = headerRow.createCell(i++);
            // set every cell language
            headercell.setCellValue(entry.getKey());
            if (firsLanguage == null) {
                firsLanguage = entry.getKey();
            }
            languageList.add(entry.getKey());
        }
        System.out.println("wubi:" + languageList);

        Map<String, String> firstMap = directorMap.get(firsLanguage);
        int row = 1;
        for (Entry<String, String> entry : firstMap.entrySet()) {
            Row contentRow = sheet.createRow(row++);
            headercell = contentRow.createCell(0);
            headercell.setCellValue(entry.getKey());
            int j = 1;
            for (String str : languageList) {
                headercell = contentRow.createCell(j++);
                headercell.setCellValue(directorMap.get(str)
                        .get(entry.getKey()));
            }
        }

        OutputStream outs = null;
        try {
            outs = new FileOutputStream(new File(path));
            workbook.write(outs);
            outs.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void writeListToXlsx(String path, List<String> list,
            String[] head) {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < head.length; i++) {
            Cell headercell = headerRow.createCell(i);
            headercell.setCellValue(head[i]);
        }

        for (int i = 0; i < list.size(); i++) {
            Row contentRow = sheet.createRow(i + 1);
            Cell headercell = contentRow.createCell(0);
            headercell.setCellValue(list.get(i));
        }

        OutputStream outs = null;
        try {
            outs = new FileOutputStream(new File(path));
            workbook.write(outs);
            outs.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("w112sdfasdfasdfasdfasdfasdfasdfasdfsadfasdfasfsafasfsafsafsfsfsfsaf");
        list.add("w1");
        list.add("w1");
        list.add("w1");
        list.add("w1");
        list.add("w1");
        PoiTranslate.writeListToXlsx("D://test.xlsx", list, new String[] {
                "英语", "西班牙语", "葡萄牙语" });
    }*/

    @Override
    public String translat(String content, String from, String to) {
        System.out.println("content=" + content + "; to=" + to);
        // set str null, if str cannot be translated, will retain it
        String str = null;
        if (map != null && map.get(to).containsKey(content)) {
            str = map.get(to).get(content);
        }
        return str;
    }

}
