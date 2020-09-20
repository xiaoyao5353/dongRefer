package org.xml.translate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) {
        // System.out.println(Test.utc2Local("1481624693","yyyyMMddHHmm","yyyyMMddHHmm"));
        Date date = new Date(Long.parseLong("1481624693" + "000"));
        Date date2 = new Date();
        System.out.println("date2:" + date2.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String time = sdf.format(date);
        System.out.println(time);
    }

}
