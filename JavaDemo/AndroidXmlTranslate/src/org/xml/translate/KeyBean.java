package org.xml.translate;

import java.util.ArrayList;
import java.util.List;

public class KeyBean implements Cloneable {
    private String tag;
    private String keyValue;
    private String keyName;
    private String content;
    private List<KeyBean> items;

    public KeyBean(String tag, String keyName, String keyValue) {
        super();
        this.tag = tag;
        this.keyValue = keyValue;
        this.keyName = keyName;
    }

    public KeyBean(String tag, String keyName, String keyValue, String content) {
        super();
        this.tag = tag;
        this.keyValue = keyValue;
        this.keyName = keyName;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return keyValue;
    }

    public void setKey(String keyName, String keyValue) {
        this.keyValue = keyValue;
        this.keyName = keyName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<KeyBean> getItems() {
        return items;
    }

    public void setItems(List<KeyBean> items) {
        this.items = items;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "KeyBean [tag=" + tag + ", key=" + keyValue + ", content="
                + content + ", items=" + items + "]";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        KeyBean kb = new KeyBean(tag, keyName, keyValue, content);
        if (items != null) {
            ArrayList<KeyBean> list = new ArrayList<KeyBean>();
            for (KeyBean tbk : items) {
                list.add((KeyBean) tbk.clone());
            }
            kb.setItems(list);
        }
        return kb;

    }
}
