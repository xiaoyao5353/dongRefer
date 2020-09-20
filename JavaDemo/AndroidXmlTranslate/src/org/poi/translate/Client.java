package org.poi.translate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    public static void main(String[] args) {

        try {
            Map<String, ExcelBean> hashMap = new HashMap<String, ExcelBean>();
            File file = new File(Common.LIB_PATH);
            if (file.isDirectory()) {
                String[] fils = file.list();
                for (String str : fils) {
                    File f = new File(str);

                    if (f.getName().contains(Common.OFFICE_EXCEL_2003_POSTFIX)) {
                        System.out
                                .println("start=================================f.getName():"
                                        + f.getName());
                        Map<String, ExcelBean> map = new ReadExcel()
                                .readXlsx(Common.LIB_PATH + "/" + f.getName());
                        hashMap.putAll(map);
                        System.out
                                .println("end=================================f.getName():"
                                        + f.getName());
                    }

                }
            }
            System.out.println(hashMap.size());
            ExcelBean mapitem = hashMap.get("RAW");
            System.out.println("##Portugal:" + mapitem.toString());
            /*
             * mapitem = hashMap.get(
             * "Forbid to do channel tune under OP mode.\\nPlease exit OP mode at first."
             * ); System.out.println("##Portugal:"+mapitem.getPortugal());
             * mapitem =
             * hashMap.get("File name or Folder name can not be null");
             * System.out.println("##Portugal:"+mapitem.getPortugal()); mapitem
             * = hashMap.get("Estonia");
             * System.out.println("##Portugal:"+mapitem.getPortugal()); mapitem
             * = hashMap.get("Settings");
             * System.out.println("##Portugal:"+mapitem); mapitem =
             * hashMap.get("Apps"); System.out.println("##Portugal:"+mapitem);
             * mapitem = hashMap.get("Inputs");
             * System.out.println("##Portugal:"+mapitem);
             */

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}