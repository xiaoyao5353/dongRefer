package org.xml.translate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.baidu.translate.demo.BaiduTranslater;
import com.test.LogToolDong;

public class TranlateUtil {
    private static String TAG = "TranlateUtil";

    public static Map<String, KeyBean> translatMap(TranslaterInterface[] tis,
            Map<String, KeyBean> map, String to, boolean flag, boolean showError) {
        if (map == null || to == null || tis == null)
            return null;
        Map<String, KeyBean> targetMap = new TreeMap<String, KeyBean>();
        for (Entry<String, KeyBean> entry : map.entrySet()) {
            KeyBean kb = entry.getValue();
            if (kb != null && kb.getItems() == null && kb.getContent() != null) {
                KeyBean cloneKB = translatKeyBean(tis, to, kb);
                if (cloneKB != null) {
                    targetMap.put(kb.getKey(), cloneKB);
                } else if (flag) {
                    try {
                        cloneKB = (KeyBean) kb.clone();
                        if (showError) {
                            cloneKB.setContent(cloneKB.getContent()
                                    + "----translat error");
                        } else {
                            cloneKB.setContent(cloneKB.getContent());
                        }
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                if (cloneKB != null)
                    targetMap.put(kb.getKey(), cloneKB);
            } else if (kb != null && kb.getItems() != null) {
                boolean isNeedAdd = false;
                KeyBean baseCloneBean = null;
                try {
                    baseCloneBean = (KeyBean) kb.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                if (baseCloneBean == null)
                    continue;

                List<KeyBean> list = kb.getItems();
                for (int i = 0; i < list.size(); i++) {
                    KeyBean cloneKB = translatKeyBean(tis, to, list.get(i));
                    if (cloneKB != null) {
                        isNeedAdd = true;
                    } else if (flag) {
                        try {
                            cloneKB = (KeyBean) list.get(i).clone();
                            if (showError) {
                                cloneKB.setContent(cloneKB.getContent()
                                        + "----translat error");
                            } else {
                                cloneKB.setContent(cloneKB.getContent());
                            }
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (cloneKB == null) {
                        isNeedAdd = false;
                    } else {
                        baseCloneBean.getItems().remove(i);
                        baseCloneBean.getItems().add(i, cloneKB);
                    }
                }
                if (isNeedAdd || flag) {
                    if (baseCloneBean != null)
                        targetMap.put(kb.getKey(), baseCloneBean);
                }
            }
        }
        return targetMap;
    }

    private static KeyBean translatKeyBean(TranslaterInterface[] tis,
            String to, KeyBean kb) {
        String str = translats(tis, kb.getContent(), to);
        KeyBean cloneKB = null;
        if (str != null) {
            if(LogToolDong.LogToolEnable) LogToolDong.e(new Exception(), "str=" + str);
            try {
                cloneKB = (KeyBean) kb.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (cloneKB != null) {
                cloneKB.setContent("\"" + str + "\"");
            }
        }
        return cloneKB;
    }

    private static String translats(TranslaterInterface[] tis, String str,
            String to) {
        if (str.contains("@string"))
            return str;
        String result = null;
        for (TranslaterInterface ti : tis) {
            try {
                result = ti.translat(str, null, to);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ti instanceof BaiduTranslater) {
                XmlLog.log(TAG, "########BaiduTranslat:str:" + str + ";to:"
                        + to + ";result:" + result);
            }
            if (result != null)
                break;
        }
        if (result == null) {
            XmlLog.log(TAG, "translat error:" + str + " to:" + to);
        }
        return result;
    }
}
