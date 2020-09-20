package com.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.poi.translate.Common;
import org.poi.translate.ExcelBean;
import org.poi.translate.PoiTranslate;
import org.poi.translate.ReadExcel;
import org.xml.translate.KeyBean;
import org.xml.translate.TranlateUtil;
import org.xml.translate.TranslaterFactory;
import org.xml.translate.TranslaterInterface;
import org.xml.translate.XmlUtil;

/**
 * 
 * 20161102 目前遇到的问题，翻译很多没有用到的字符，需要再优化xml获取。
 * 
 * @author Administrator
 * 
 */
public class OfflineXlsToXml {
    public static final String dictionaryDir = "D:\\taget";

    private static final boolean DEBUG = true;
    private static String basicApkValueName = "values";

    private static final String topDirName1 = "Y:\\MST538_Base\\MST538_ANDROID\\packages\\apps\\";
    private static final String[] apkNameArray1 = new String[] { "TvLauncher" };
    private static final String[] genValueDirSuffixArray = new String[] { "fr" };

    public static void main(String[] args) {
        for (String str : apkNameArray1) {
            translateOneApk(topDirName1, str);
        }
    }

    private static void translateOneApk(String topDirName, String apkName) {
        for (String str : genValueDirSuffixArray) {
            translateOneApkValues(topDirName, apkName, str);
        }
    }

    private static void translateOneApkValues(String topDirName,
            String apkName, String genValueDirSuffix) {
        String genValueFullPath = topDirName + apkName + "\\res\\values-"
                + genValueDirSuffix;

        File mGenValueFile = new File(genValueFullPath);
        if (!mGenValueFile.isDirectory()) {
            if (LogToolDong.LogToolEnable)
                LogToolDong.e(new Exception(), "mGenValueFile is not directory!");
            return;
        } else {
            for (String str : mGenValueFile.list()) {
                translateOneApkValuesXml(topDirName, apkName,
                        genValueDirSuffix, str);
            }
        }
    }

    private static void translateOneApkValuesXml(String topDirName,
            String apkName, String genValueDirSuffix, String xmlName) {
        String genValueDir = "values-" + genValueDirSuffix;
        String basicValueFullPath = topDirName + apkName + "\\res\\"
                + basicApkValueName;
        String genValueFullPath = topDirName + apkName + "\\res\\"
                + genValueDir;
        String basicXmlFullPath = basicValueFullPath + "\\" + xmlName;
        String genXmlFullPath = genValueFullPath + "\\" + xmlName;

        File mTopDirFile = new File(topDirName);
        if (!mTopDirFile.isDirectory()) {
            if (LogToolDong.LogToolEnable)
                LogToolDong.e(new Exception(), "mTopDirFile is not directory!");
            return;
        }
        File mBasicValueFile = new File(basicValueFullPath);
        if (!mBasicValueFile.isDirectory()) {
            if (LogToolDong.LogToolEnable)
                LogToolDong.e(new Exception(), "mBasicValueFile is not directory!");
            return;
        }
        File mGenValueFile = new File(genValueFullPath);
        if (!mGenValueFile.isDirectory()) {
            if (LogToolDong.LogToolEnable)
                LogToolDong.e(new Exception(), "mGenValueFile is not directory!");
            return;
        }
        File mXmlFile = new File(basicXmlFullPath);
        if (!mXmlFile.exists()) {
            if (LogToolDong.LogToolEnable)
                LogToolDong.e(new Exception(), "mXmlFile is not exist!");
            return;
        }

        System.out.println("=============start=================");
        TranslaterInterface poiTi = TranslaterFactory
                .createTranslater(TranslaterFactory.POI);
        try {
            Map<String, KeyBean> map;
            if (LogToolDong.LogToolEnable)
                LogToolDong.e(new Exception(), "genValueFullPath="
                        + genValueFullPath);
            map = XmlUtil.readStringXmlOut(basicXmlFullPath,
                    "string|string-array", "name");
            XmlUtil.writeStringXmlByMap(genXmlFullPath, TranlateUtil
                    .translatMap(new TranslaterInterface[] { poiTi }, map,
                            genValueDirSuffix, true, false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("============= end =================");

    }
}
