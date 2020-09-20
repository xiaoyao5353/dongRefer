package org.xml.translate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class XmlDirectorTranslater implements TranslaterInterface {
    private static String TAG = "XmlDirectorTranslater";
    private Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

    public XmlDirectorTranslater(String resPath, String baseLanguage) {
        initDirector(resPath, baseLanguage);
    }

    protected void initDirector(String resPath, String baseLanguage) {
        File file = new File(resPath);
        File baseFile = new File(resPath + "\\values-" + baseLanguage);
        if (!baseFile.exists()) {
            baseFile = new File(resPath + "\\values");
            if (!baseFile.exists()) {
                XmlLog.log(TAG, "NOT FOUND BASE VALUE");
                return;
            }
        }

        Map<String, KeyBean> baseMap = XmlUtil.getAllStringMap(baseFile
                .getAbsolutePath());

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory() && f.getName().contains("values")) {
                    String str = f.getName().replace("values-", "");
                    if (str.equals("values"))
                        continue;
                    if (!str.equals("")) {
                        XmlLog.log(TAG, "DOING :" + f.getAbsolutePath());
                        Map<String, KeyBean> tempMap = XmlUtil
                                .getAllStringMap(f.getAbsolutePath());
                        if (tempMap == null)
                            continue;
                        // XmlLog.log(TAG, "tempMap :"+tempMap.toString());
                        Map<String, String> changeMap = changeToDirector(
                                baseMap, tempMap);
                        if (changeMap != null)
                            map.put(str, changeMap);
                    }
                }
            }
        }
    }

    private Map<String, String> changeToDirector(Map<String, KeyBean> baseMap,
            Map<String, KeyBean> targetMap) {
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
                XmlLog.log(TAG, "entry.getKey():" + entry.getKey());
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

    private void putDirector(Map<String, String> map, String key, String content) {
        if (key != null && key.equals(content)) {
            if (!map.containsKey(key)) {
                map.put(key, content);
            }
            return;
        }
        map.put(key, content);
    }

    private void initDatabase() {

    }

    private String query(String str, String to) {
        Map<String, String> tempMap = map.get(to);
        if (tempMap == null)
            return null;
        String result = tempMap.get(str);

        if (result == null) {
            if (str.contains(":")) {
                String tempStr = str;
                tempStr = tempStr.replace(":", "").trim();
                if (tempMap.containsKey(tempStr)) {
                    result = str.replace(tempStr, tempMap.get(tempStr));
                    XmlLog.log(TAG, "##########str:" + str + ";to:" + to
                            + ";resualt:" + result);
                }
            }
        }

        XmlLog.log(TAG, "str:" + str + ";to:" + to + ";resualt:" + result);
        return result;
    }

    @Override
    public String translat(String content, String from, String to) {
        return query(content, to);
    }

}
