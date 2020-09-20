package com.test;

import org.poi.translate.PoiTranslate;
import org.xml.translate.XmlUtil;

public class OfflineXmlToXls {
    private static String projectName = "hr553";
//    private static String topDir1 = "Y:\\MST538_Base\\MST538_ANDROID\\device\\mstar\\common\\libraries\\tv2\\";
//    private static String topDir1 = "Y:\\HI3751V553\\Android\\device\\hisilicon\\bigfish\\hiapp_Hi3751V553_RT_TEST\\";
    private static String topDir1 = "Y:\\hs553_work1\\HI3751V553\\Android\\device\\hisilicon\\bigfish\\hiapp_Hi3751V553_RT_TEST\\";
//    
//    private static String[] apkNameArray1 = new String[] { "HiTvPlayer", "HiATV", "HiDTV" };
    private static String[] apkNameArray1 = new String[] { "HiRMService", "HiTvPlayer", "HiTvService", "HiTvSetting", /*"MBrowser3",*/ "OtaService", /*"Settings",*/ "TvLauncher" };
    private static String oriValuesName = "values";

    public static void main(String[] args) {
        for (String str : apkNameArray1) {
            translateOne(topDir1, str);
        }
    }

    private static void translateOne(String topDir, String apkName) {
        PoiTranslate
                .writeListToXlsx(
                        "D://" + projectName + "_" + apkName + ".xlsx",
                        XmlUtil.getStringList(topDir + apkName + "\\res\\" + oriValuesName),
                        new String[] { "英语", "阿拉伯语", "波斯语", "希伯来语" });
    }
}
