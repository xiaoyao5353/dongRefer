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

import com.baidu.translate.demo.BaiduTranslater;

/**
 * 
 * 20161102 目前遇到的问题，翻译很多没有用到的字符，需要再优化xml获取。
 * 
 * @author Administrator
 * 
 */
public class OnlineXmlToXml {
//    public static final String oriValueDirSuffix = "";
    public static final String oriValueDirSuffix = "zh-rTW";

    private static final String TAG = "[OnlineXmlToXml.java]";
    // null is values
    private static String oriApkValueFullPath = "";
    private static File oriApkValueFile;
//    private static final String translaterType = TranslaterFactory.GOOGLE;
    private static final String translaterType = TranslaterFactory.BAIDU;
    private static List<String> xmlList = new ArrayList<String>();

//    private static final String topDirName = "Y:\\RT-MT5659\\release\\android\\n-cn\\vendor\\mediatek\\open\\custom\\mtk\\mtk\\packages\\apps\\";
    private static final String topDirName = "Y:\\hr538_Haier_Taiwan_DVB\\MST538_Base\\MST538_ANDROID\\device\\mstar\\common\\apps\\";
    private static final String[] apkFileFullPathArray = new String[] { topDirName + "MTvPlayer" };
    private static final String[] genValueDirSuffixArray = new String[] { "zh-rCN" };
//    private static final String[] genValueDirSuffixArray = new String[] { "pt" };

    public static void main(String[] args) {
        /*for (String sApkFileFullPath : apkFileFullPathArray) {
            transOneAPK(sApkFileFullPath);
            if (LogTool.LogToolEnable)
                LogTool.e(new Exception(), "sApkFileFullPath="
                        + sApkFileFullPath);
        }*/
//        https://translate.google.cn/#zh-CN/zh-TW/商城模式
    }

    public static void transOneAPK(String apkFileFullPath) {
        oriApkValueFullPath = apkFileFullPath + "\\res\\values";
        if (oriValueDirSuffix != null && !"".endsWith(oriValueDirSuffix)) {
            oriApkValueFullPath += "-" + oriValueDirSuffix;
        }

        oriApkValueFile = new File(oriApkValueFullPath);
        if (!oriApkValueFile.isDirectory()) {
            System.out
                    .println(TAG
                            + "original value directory is not exist or is not a directory: " + oriApkValueFullPath);
            return;
        } else {
            String[] xmlFileArray = oriApkValueFile.list();
            for (String xmlFile : xmlFileArray) {
                if (xmlFile.length() <= 4) {
                    System.out.println(TAG + xmlFile + " isn't xml file");
                    continue;
                }
                if (xmlFile.indexOf(".xml") == (xmlFile.length() - 4)) {
                    xmlList.add(xmlFile);
                }
            }
        }

        for (String sXml : xmlList) {
            if (LogToolDong.LogToolEnable)
                LogToolDong.e(new Exception(), "apkFileFullPath=" + apkFileFullPath
                        + "; sXml=" + sXml);
            transOneXML(apkFileFullPath, sXml);
        }
    }

    public static void transOneXML(String apkFileFullPath, String xmlFileName) {
        System.out.println(TAG
                + "================== start ======================");
        if (LogToolDong.LogToolEnable)
            LogToolDong.e(new Exception(), "apkFileFullPath=" + apkFileFullPath
                    + "; xmlFileName=" + xmlFileName);

        TranslaterInterface mTranslater = TranslaterFactory
                .createTranslater(translaterType);

        File mTopDirFile = new File(topDirName);
        if (!mTopDirFile.isDirectory()) {
            if(LogToolDong.LogToolEnable) LogToolDong.e(new Exception(),"mTopDirFile is not directory!");
            return;
        }

        try {
            Map<String, KeyBean> map;
            map = XmlUtil.readStringXmlOut(oriApkValueFullPath + "\\"
                    + xmlFileName, "string|string-array", "name");

            for (String str2 : genValueDirSuffixArray) {
                XmlUtil.writeStringXmlByMap(oriApkValueFullPath + "\\"
                        + xmlFileName, TranlateUtil.translatMap(
                        new TranslaterInterface[] { mTranslater }, map, str2,
                        true, false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(TAG
                    + "================== end ======================");
        }
    }
}
