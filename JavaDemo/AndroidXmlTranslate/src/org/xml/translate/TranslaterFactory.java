package org.xml.translate;

import org.poi.translate.PoiTranslate;

import com.baidu.translate.demo.BaiduTranslater;
import com.google.translate.demo.GoogleTranslate;

public class TranslaterFactory {
    public static String DIRECTOR = "DIRECTOR";
    public static String NETWORK = "NETWORK";
    public static String BAIDU = "BAIDU";
    public static String POI = "poi";
    public static String GOOGLE = "GOOGLE";

    public static TranslaterInterface createTranslater(String type) {
        TranslaterInterface ti = null;
        if (DIRECTOR.equals(type)) {
            return ti = new XmlDirectorTranslater("C:\\res", "");
        } else if (NETWORK.equals(type)) {
            return ti = new NetWorkTranslater();
        } else if (BAIDU.equals(type)) {
            return ti = new BaiduTranslater();
        } else if (POI.equals(type)) {
            return ti = new PoiTranslate();
        } else if (GOOGLE.equals(type)) {
            return ti = new GoogleTranslate();
        }
        return ti;
    }
}
