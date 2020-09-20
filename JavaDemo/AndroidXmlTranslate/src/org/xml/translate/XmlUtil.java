package org.xml.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

//import org.apache.bcel.generic.CPInstruction;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.test.LogToolDong;

public class XmlUtil {
    private static String TAG = "XmlUtil";
    private static String tmpStr = "";

    public static Map<String, KeyBean> readStringXmlOut(String xml, String tag,
            String keyName) {
        Map<String, KeyBean> map = new TreeMap<String, KeyBean>();
        Document doc = null;
        SAXReader reader = null;
        try {
            reader = new SAXReader();
            doc = reader.read(new File(xml));
            Element rootElt = doc.getRootElement();
            List<Element> list = rootElt.elements();
            String tempKey;
            String tempValue;
            for (Element el : list) {
                if (!tag.contains(el.getName()))
                    continue;
                tempKey = el.attribute(keyName).getText();
                tempValue = el.getTextTrim();
                if (el.isTextOnly()) {
                    KeyBean kb = new KeyBean(el.getName(), keyName, tempKey);
                    kb.setContent(tempValue);
                    map.put(tempKey, kb);
                } else {
                    KeyBean kb = new KeyBean(el.getName(), keyName, tempKey);
                    List<KeyBean> items = new ArrayList<KeyBean>();
                    List<Element> tempList = el.elements();
                    for (Element el2 : tempList) {
                        items.add(new KeyBean(el2.getName(), null, null, el2
                                .getTextTrim()));
                    }
                    kb.setItems(items);
                    map.put(tempKey, kb);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        if (map.size() == 0) {
            return null;
        }
        return map;
    }

    public static Map<String, KeyBean> getAllStringMap(String path) {
        Map<String, KeyBean> tempMap = new TreeMap<String, KeyBean>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.getName().contains("xml")) {
                    Map<String, KeyBean> readMap = XmlUtil.readStringXmlOut(
                            f.getAbsolutePath(), "string|string-array", "name");
                    if (readMap != null)
                        tempMap.putAll(readMap);
                }
            }
        }
        return tempMap;
    }

    public static Map<String, Map<String, String>> getDirectorMap(String path,
            String baseLanguage) {
        Map<String, Map<String, String>> directorMap = new HashMap<String, Map<String, String>>();
        File file = new File(path);
        File baseFile = new File(path + "\\values-" + baseLanguage);
        if (!baseFile.exists()) {
            baseFile = new File(path + "\\values");
            if (!baseFile.exists()) {
                return null;
            }
        }

        Map<String, KeyBean> baseMap = XmlUtil.getAllStringMap(baseFile
                .getAbsolutePath());
        System.out.println("getAllStringMap_size:" + baseMap.size());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory() && f.getName().contains("values")) {
                    String str = f.getName().replace("values-", "");
                    if (str.equals("values"))
                        continue;
                    if (!str.equals("")) {
                        Map<String, KeyBean> tempMap = XmlUtil
                                .getAllStringMap(f.getAbsolutePath());
                        if (tempMap == null)
                            continue;
                        Map<String, String> changeMap = changeToDirector(
                                baseMap, tempMap);
                        if (changeMap != null)
                            directorMap.put(str, changeMap);
                    }
                }
            }
        }
        return directorMap;
    }

    public static Map<String, Map<String, String>> addDirctorMap(
            Map<String, Map<String, String>> soureMap,
            Map<String, Map<String, String>> copyMap) {
        for (Entry<String, Map<String, String>> entry : soureMap.entrySet()) {
            Map<String, String> map = copyMap.get(entry.getKey());
            if (map == null)
                continue;
            Map<String, String> smap = entry.getValue();
            for (Entry<String, String> entry2 : map.entrySet()) {
                smap.put(entry2.getKey(), entry2.getValue());
            }
        }
        return soureMap;
    }

    private static Map<String, String> changeToDirector(
            Map<String, KeyBean> baseMap, Map<String, KeyBean> targetMap) {
        if (targetMap == null || baseMap == null)
            return null;
        Map<String, String> tempMap = new TreeMap<String, String>();
        for (Entry<String, KeyBean> entry : baseMap.entrySet()) {
            KeyBean kb = entry.getValue();
            if (kb.getItems() == null) {
                if (!targetMap.containsKey(entry.getKey()))
                    continue;
                putDirector(tempMap, kb.getContent(),
                        targetMap.get(entry.getKey()).getContent());
                // tempMap.put(kb.getContent(),
                // targetMap.get(entry.getKey()).getContent());
            } else {
                if (!targetMap.containsKey(entry.getKey()))
                    continue;
                List<KeyBean> targetList = targetMap.get(entry.getKey())
                        .getItems();
                List<KeyBean> baseList = kb.getItems();
                if (targetList == null || targetList.size() != baseList.size())
                    continue;
                for (int i = 0; i < targetList.size(); i++) {
                    KeyBean base = baseList.get(i);
                    KeyBean target = targetList.get(i);
                    if (base.getContent() != null
                            && !base.getContent().equals("")) {
                        // tempMap.put(base.getContent(),target.getContent());
                        putDirector(tempMap, base.getContent(),
                                target.getContent());
                    }
                }
            }
        }
        return tempMap;
    }

    private static void putDirector(Map<String, String> map, String key,
            String content) {
        if (key != null && key.equals(content)) {
            if (!map.containsKey(key)) {
                map.put(key, content);
            }
            return;
        }
        map.put(key, content);
    }

    public static void writeStringXmlByMap(String path, Map<String, KeyBean> map)
            throws IOException {
        if (path == null | map == null)
            return;
        File file = new File(path);
        file.mkdirs();
        if (file.exists()) {
            file.delete();
        }
        Document doc = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("resources");
        for (Entry<String, KeyBean> entry : map.entrySet()) {
            KeyBean kb = entry.getValue();
            List<KeyBean> list = kb.getItems();
            Element element = DocumentHelper.createElement(kb.getTag());
            if (kb.getKeyName() != null) {
                element.addAttribute(kb.getKeyName(), kb.getKeyValue());
            }
            if (list == null) {
                if (kb.getContent() != null) {
                    tmpStr = kb.getContent();
                    if(LogToolDong.LogToolEnable) LogToolDong.e(new Exception(), tmpStr);
                    if (tmpStr.contains("\"")) {
                        element.setText(kb.getContent());
                    } else {
                        element.setText("\"" + kb.getContent() + "\"");
                    }
                }
            } else {
                for (KeyBean kb2 : list) {
                    Element element2 = DocumentHelper.createElement(kb2
                            .getTag());
                    if (kb2.getKeyName() != null) {
                        element2.addAttribute(kb2.getKeyName(),
                                kb2.getKeyValue());
                    }
                    if (kb2.getContent() != null) {
                        System.out.println("dong----XmlUtil.java:212----"
                                + kb2.getContent());
                        tmpStr = kb2.getContent();
                        if(LogToolDong.LogToolEnable) LogToolDong.e(new Exception(), tmpStr);
                        if (tmpStr.contains("\"")) {
                            element2.setText(kb2.getContent());
                        } else {
                            element2.setText("\"" + kb2.getContent() + "\"");
                        }
                    }
                    element.add(element2);
                }
            }
            root.add(element);
        }
        doc.add(root);
        OutputFormat format = new OutputFormat("    ", true);
        format.setEncoding("UTF-8");
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(path),
                    format);
            xmlWriter.write(doc);
            xmlWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

    public static List<String> getAndroidResValuesLaunage(String path) {
        File file = new File(path);
        List<String> list = new ArrayList<String>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory() && f.getName().contains("values")) {
                    String str = f.getName().replace("values-", "");
                    if (str.equals("values"))
                        continue;
                    if (!str.equals("")) {
                        list.add(str);
                    }
                }
            }
        }
        return list;
    }

/*  xls to xml will use the method by offline */
    public static List<String> getStringList(String path) {
        System.out.println("dong----getStringList");
        List<String> list = new ArrayList<String>();
        Map<String, KeyBean> tempMap = new TreeMap<String, KeyBean>();
        Map<String, KeyBean> readMap = getAllStringMap(path);
        for (Entry<String, KeyBean> en : readMap.entrySet()) {
            List<KeyBean> tempList = en.getValue().getItems();
            if (tempList != null) {
                for (KeyBean kb : tempList) {
                    tempMap.put(kb.getContent(), null);
                    System.out.println("dong----kb.getContent()=" + kb.getContent());
                }
            } else {
                tempMap.put(en.getValue().getContent(), null);
                System.out.println("dong----en.getValue().getContent()=" + en.getValue().getContent());
            }
        }

        for (Entry<String, KeyBean> en : tempMap.entrySet()) {
            list.add(en.getKey());
        }
        return list;
    }

}
