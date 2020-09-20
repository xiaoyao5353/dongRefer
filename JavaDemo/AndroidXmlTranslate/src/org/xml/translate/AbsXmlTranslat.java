package org.xml.translate;

import java.util.Map;

public class AbsXmlTranslat implements ToTranslatXml {

    @Override
    public void toTranslatXml(String xmlPath, String baseRes,
            String baseLauguage, String[] toLauguage, String fromDir,
            String targetDir) {

    }

    @Override
    public void toTranslatXmlByNetWork(String xmlPath, String baseLauguage,
            String[] toLauguage, String targetDir) {

    }

    private Map<String, String> getBaseMap(String xmlPath) {

        return null;
    }

    private Map<String, String> getTranslatMap(String laungage) {
        return null;
    }

}
