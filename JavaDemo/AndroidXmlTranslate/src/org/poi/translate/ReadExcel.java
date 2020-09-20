package org.poi.translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
    private Map<String, ExcelBean> map;

    public Map<String, ExcelBean> readXlsx(String path) throws IOException {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        Workbook xssfWorkbook = null;
        if (path.contains("xlsx")) {
            xssfWorkbook = new XSSFWorkbook(is);
        } else if (path.contains("xls")) {
            xssfWorkbook = new HSSFWorkbook(is);
        } else {
            return null;
        }

        ExcelBean excelBean = null;

        map = new HashMap<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            Sheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            int indexSpanish = 1;
            int indexPortuga = 2;

            Row initXSS = xssfSheet.getRow(0);
            if (initXSS == null)
                continue;
            // System.out.println("initXSS.getLastCellNum():"+initXSS.getLastCellNum());
            for (int cellIndex = 1; cellIndex < initXSS.getLastCellNum(); cellIndex++) {
                Cell indexCell = initXSS.getCell(cellIndex);
                String title = String.valueOf(indexCell.getStringCellValue());
                // System.out.println("title:"+title);
                if (title != null && title.contains("rc_PT")) {
                    indexPortuga = cellIndex;
                } else if (title != null && title.contains("rc_ES")) {
                    indexSpanish = cellIndex;
                }
            }
            // System.out.println("indexSpanish:"+indexSpanish+";indexPortuga:"+indexPortuga);
            // System.out.println("xssfSheet.getLastRowNum():"+xssfSheet.getLastRowNum());
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = xssfSheet.getRow(rowNum);

                if (xssfRow != null) {
                    excelBean = new ExcelBean();
                    Cell english = xssfRow.getCell(0);
                    Cell spanish = xssfRow.getCell(indexSpanish);
                    Cell Portugal = xssfRow.getCell(indexPortuga);

                    if (english != null) {
                        excelBean.setEnglish(getValue(english));
                    }
                    if (spanish != null) {
                        excelBean.setSpanish(getValue(spanish));
                    }

                    if (Portugal != null) {
                        excelBean.setPortugal(getValue(Portugal));
                    }

                    if (english != null && spanish != null && Portugal != null) {
                        if (map.containsKey(excelBean.getEnglish())) {
                            // System.out.println(excelBean.getEnglish());
                        }
                        if (excelBean.getEnglish().equals("Settings")) {
                            System.out.println("###############:" + path
                                    + ";rowNum:" + rowNum);
                            System.out.println(excelBean);
                        }

                        if (excelBean.getEnglish().equals("Inputs")) {
                            System.out.println("###############:" + path
                                    + ";rowNum:" + rowNum);
                            System.out.println(excelBean);
                        }

                        if (excelBean.getEnglish().equals("Apps")) {
                            System.out.println("###############:" + path
                                    + ";rowNum:" + rowNum);
                            System.out.println(excelBean);
                        }
                        map.put(excelBean.getEnglish(), excelBean);
                    }

                }
            }
        }
        is.close();
        return map;
    }

    private String getValue(Cell xssfRow) {
        try {
            if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
                return String.valueOf(xssfRow.getBooleanCellValue());
            } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
                return String.valueOf(xssfRow.getNumericCellValue());
            } else {
                return String.valueOf(xssfRow.getStringCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Map<String, Map<String, String>> getDirectorMap(String path,
            String baseLanguage) throws IOException {
        Map<String, Map<String, String>> mapDir = new HashMap<String, Map<String, String>>();

        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        Workbook xssfWorkbook = null;
        if (path.contains("xlsx")) {
            xssfWorkbook = new XSSFWorkbook(is);
        } else if (path.contains("xls")) {
            xssfWorkbook = new HSSFWorkbook(is);
        } else {
            return null;
        }

        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            Sheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            int baseIndex = 0;
            Row initXSS = xssfSheet.getRow(0);
            if (initXSS == null)
                continue;

            // System.out.println("initXSS.getLastCellNum():"+initXSS.getLastCellNum());
            for (int cellIndex = 0; cellIndex < initXSS.getLastCellNum(); cellIndex++) {
                Cell indexCell = initXSS.getCell(cellIndex);
                String title = String.valueOf(indexCell.getStringCellValue());
                // System.out.println("title:"+title);
                if (title != null && !title.trim().equals("")
                        && title.equals(baseLanguage)) {
                    baseIndex = cellIndex;
                    break;
                }
            }

            for (int cellIndex = 0; cellIndex < initXSS.getLastCellNum(); cellIndex++) {
                Cell indexCell = initXSS.getCell(cellIndex);
                String title = String.valueOf(indexCell.getStringCellValue());
                System.out.println("title:" + title);
                if (title != null && !title.trim().equals("")
                        && !title.equals(baseLanguage)) {
                    mapDir.put(title, getMap(xssfSheet, baseIndex, cellIndex));
                } else if (title != null && title.equals(baseLanguage)) {
                }
            }
        }
        is.close();
        System.out.println("mapDir:" + mapDir.size());
        return mapDir;
    }

    public Map<String, Map<String, String>> getAllDirectorMap(String path,
            String baseLanguage) {
        Map<String, Map<String, String>> mapDir = new HashMap<String, Map<String, String>>();
        File fs = new File(path);
        File[] fss = fs.listFiles();
        for (File f : fss) {
            if (f.getName().contains(".xls") || f.getName().contains(".xlsx")) {
                try {
                    mapDir = addDirctorMap(mapDir,
                            getDirectorMap(f.getAbsolutePath(), baseLanguage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mapDir;
    }

    public Map<String, Map<String, String>> addDirctorMap(
            Map<String, Map<String, String>> soureMap,
            Map<String, Map<String, String>> copyMap) {
        if (soureMap.size() == 0) {
            return copyMap;
        }
        for (Entry<String, Map<String, String>> entry : soureMap.entrySet()) {
            Map<String, String> map = copyMap.get(entry.getKey());
            if (map == null)
                continue;
            Map<String, String> smap = entry.getValue();
            for (Entry<String, String> entry2 : map.entrySet()) {
                smap.put(entry2.getKey(), entry2.getValue());
            }
        }
        return soureMap;
    }

    public Map<String, String> getMap(Sheet xssfSheet, int baseIndex,
            int targeIndex) {
        Map<String, String> mapStr = new HashMap<String, String>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            Row xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                Cell base = xssfRow.getCell(baseIndex);
                Cell target = xssfRow.getCell(targeIndex);
                // System.out.println(getValue(base)+":"+ getValue(target));
                mapStr.put(getValue(base), getValue(target));
            }
        }
        System.out.println("mapStr:" + mapStr.size());
        return mapStr;
    }

}
