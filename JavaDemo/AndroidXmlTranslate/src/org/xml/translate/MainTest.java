package org.xml.translate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.poi.translate.PoiTranslate;

import com.baidu.translate.demo.BaiduTranslater;

/**
 * 
 * 20161102 目前遇到的问题，翻译很多没有用到的字符，需要再优化xml获取。
 * 
 * @author Administrator
 * 
 */
public class MainTest {
    public static void main(String[] args) {
        // Map<String,KeyBean>
        // map=XmlUtil.readStringXmlOut("C:\\res\\values-en\\strings.xml","string|string-array","name");

        Map<String, KeyBean> map = XmlUtil
                .getAllStringMap("C:\\target\\values");

        TranslaterInterface ti = TranslaterFactory
                .createTranslater(TranslaterFactory.DIRECTOR);
        TranslaterInterface baiduTi = TranslaterFactory
                .createTranslater(TranslaterFactory.BAIDU);
        TranslaterInterface poiTi = TranslaterFactory
                .createTranslater(TranslaterFactory.POI);
        /*
         * Map<String,String> baiduCustomMap=new HashMap<String, String>();
         * baiduCustomMap.put("Exit",""); baiduCustomMap.put("Rename","");
         * baiduCustomMap.put("Skip",""); baiduCustomMap.put("Always","");
         * baiduCustomMap.put("Please enter old password","");
         * baiduCustomMap.put("Please enter new password","");
         * baiduCustomMap.put("Please enter password","");
         * baiduCustomMap.put("New Password","");
         * baiduCustomMap.put("Confirm Password","");
         * baiduCustomMap.put("Password Don\'t Match",""); baiduCustomMap.put(
         * "The original password is incorrect. Please enter again.","");
         * baiduCustomMap.put(
         * "The new password and password to confirm do not match.Please enter again."
         * ,""); baiduCustomMap.put("TV Settings","");
         * baiduCustomMap.put("Change Password","");
         * baiduCustomMap.put("Sound",""); baiduCustomMap.put("Device Menu","");
         * ((BaiduTranslater)baiduTi).setCustomList(baiduCustomMap);
         * Map<String,Map<String,String>> customMap=new HashMap<String,
         * Map<String,String>>(); Map<String,String> customItemMap=new
         * HashMap<String, String>(); customItemMap.put("Exit", "退出");
         * customMap.put("zh",customItemMap);
         * ((BaiduTranslater)baiduTi).setCustomTranslat(customMap);
         */
        // String str=ti.translat("Audio Language", null, "zh");
        // System.out.println(str);
        try {
            /*
             * List<String> list=XmlUtil.getAndroidResValuesLaunage("C:\\res");
             * for(String str:list){ if("en".equals(str)){ continue; }
             * if("es".equals(str)||"pt".equals(str)){
             * XmlUtil.writeStringXmlByMap
             * ("C:\\nimei\\wini\\values"+"-"+str+"\\strings.xml",
             * TranlateUtil.translatMap(new TranslaterInterface[]{ti}, map,str,
             * true,false)); } }
             */

            // XmlUtil.writeStringXmlByMap("C:\\nimei\\wini\\values"+"-"+"zh"+"\\strings.xml",
            // TranlateUtil.translatMap(new TranslaterInterface[]{ti,baiduTi},
            // map,"zh", true,false));
            XmlUtil.writeStringXmlByMap(
                    "C:\\nimei\\wini\\values" + "-" + "iw" + "\\strings.xml",
                    TranlateUtil.translatMap(new TranslaterInterface[] { ti,
                            baiduTi }, map, "zh", true, false));

            // PoiTranslate.writeListToXlsx("D://test.xlsx",
            // XmlUtil.getStringList("C:\\target\\test"), new
            // String[]{"英语","西班牙语","葡萄牙语"});

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
