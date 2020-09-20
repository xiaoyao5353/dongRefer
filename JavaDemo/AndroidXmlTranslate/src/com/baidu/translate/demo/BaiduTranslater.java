package com.baidu.translate.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.translate.TranslaterInterface;
import org.xml.translate.XmlLog;

public class BaiduTranslater implements TranslaterInterface {
    private static final String TAG = "BaiduTranslater";
    private static final String APP_ID = "20161101000031182";
    private static final String SECURITY_KEY = "VT2oYoDIFYY0SAXdnQKQ";
    private static final String DST = "\"dst\":\"";
    private TransApi api;
    private Map<String, String> map = new HashMap<String, String>();

    private Map<String, String> customList;
    private Map<String, Map<String, String>> customTranslaterMap;

    public BaiduTranslater() {
        api = new TransApi(APP_ID, SECURITY_KEY);
        map.put("zh-rCN", "zh");
        map.put("zh", "zh");
        map.put("zh-rTW", "cht");
        map.put("ja", "jp");
        map.put("ko", "kor");
        map.put("fr", "fra");
        map.put("es", "spa");
        map.put("ar", "ara");
        map.put("da", "dan");
        map.put("fi", "fin");
        map.put("ro", "rom");
        map.put("sl", "slo");
        map.put("zh", "zh");
        map.put("en", "en");
        map.put("th", "th");
        map.put("ru", "ru");
        map.put("pt", "pt");
        map.put("it", "it");
        map.put("el", "el");
        map.put("nl", "nl");
        map.put("pl", "pl");
        map.put("hu", "hu");
    }

    public Map<String, String> getCustomList() {
        return customList;
    }

    public void setCustomList(Map<String, String> customList) {
        this.customList = customList;
    }

    public void setCustomTranslat(Map<String, Map<String, String>> map) {
        customTranslaterMap = map;
    }

    @Override
    public String translat(String content, String from, String to) {
        String temp = map.get(to);
        try {
            content = URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("dong--content=" + content + "; from=" + from + "; to=" + to);
        if (temp == null && to.contains("-")) {
            to = to.substring(0, to.indexOf("-"));
            temp = map.get(to);
            if (temp != null) {
                to = temp;
            }
        } else {
            to = temp;
        }
        if (to == null)
            return null;

        String customStr = customTranslater(to, content);
        if (customStr != null) {
            return customStr;
        }

        if (customList != null) {
            if (!customList.containsKey(content))
                return null;
        }
        String str = api.getTransResult(content, "auto", to);
        if (str.contains(DST)) {
            str = str.substring(str.indexOf(DST) + DST.length());
            str = str.substring(0, str.indexOf("\"}"));
        } else {
            XmlLog.log(TAG, "ERROR:" + str);
            str = null;
        }
        if (str == null)
            return null;
        String result = handleIncludeUnicodeStr(str);
        if (result.contains("\'")) {
            result = result.replace("\'", "\\\'");
        }
        System.out.println("dong--content=" + content + "; to=" + to + "; result=" + result);
        return result;
    }

    public static String handleIncludeUnicodeStr(String dataStr) {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < dataStr.length(); i++) {
            char item = dataStr.charAt(i);
            if (item == '\\' && (i + 1) < dataStr.length()
                    && dataStr.charAt(i + 1) == 'u'
                    && (i + 5) < dataStr.length()) {
                String str = dataStr.substring(i, i + 6);
                try {
                    char letter = (char) Integer.parseInt(
                            str.replace("\\u", ""), 16);
                    buffer.append(letter);
                    i += 5;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    buffer.append(str);
                }
            } else {
                buffer.append(item);
            }
        }
        return buffer.toString();
    }

    private String customTranslater(String to, String str) {
        if (customTranslaterMap == null)
            return null;
        if (customTranslaterMap.containsKey(to)) {
            Map<String, String> tempMap = customTranslaterMap.get(to);
            return tempMap.get(str);
        }
        return null;
    }

}
