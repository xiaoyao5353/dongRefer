package org.xml.translate;

public interface ToTranslatXml {
    public void toTranslatXml(String xmlPath, String baseRes,
            String baseLauguage, String[] toLauguage, String fromDir,
            String targetDir);

    public void toTranslatXmlByNetWork(String xmlPath, String baseLauguage,
            String[] toLauguage, String targetDir);
}
