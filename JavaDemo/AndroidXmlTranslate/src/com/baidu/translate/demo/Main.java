package com.baidu.translate.demo;

import java.io.UnsupportedEncodingException;
import java.util.Stack;

/*
 */

public class Main {

    // 在平台申请的APP_ID 详见
    // http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20161101000031182";
    private static final String SECURITY_KEY = "VT2oYoDIFYY0SAXdnQKQ";
    private static final String DST = "\"dst\":\"";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "112你24234好";
        String str = api.getTransResult(query, "auto", "kor");
        System.out.println(str);
        if (str.contains(DST)) {
            str = str.substring(str.indexOf(DST) + DST.length());
            str = str.substring(0, str.indexOf("\"}"));

            System.out.println(handleIncludeUnicodeStr(str));
            // System.out.println(decodeUnicode(str));
            try {
                String buffer = new String(str.getBytes("UTF-8"), "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            str = null;
        }
        System.out.println(str);
    }

    public static String handleIncludeUnicodeStr(String dataStr) {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < dataStr.length(); i++) {
            char item = dataStr.charAt(i);
            if (item == '\\' && (i + 1) < dataStr.length()
                    && dataStr.charAt(i + 1) == 'u'
                    && (i + 5) < dataStr.length()) {
                String str = dataStr.substring(i, i + 6);
                System.out.println(str);
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

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]); // 转换为16进制整型字符串
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }

}
