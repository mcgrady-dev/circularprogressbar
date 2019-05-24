package com.mcgrady.xui.linkagerecyclerview.bean;

import java.io.Serializable;

/**
 * Created by mcgrady on 2019/5/20.
 */
public abstract class BaseLinkageGroupItem<T extends BaseLinkageGroupItem.ItemInfo> implements Serializable {

    public boolean isHeader;
    public  T itemInfo;
    public String header;

    public BaseLinkageGroupItem(boolean isHeader, String header, T itemInfo) {
        this.isHeader = isHeader;
        this.header = header;
        this.itemInfo = itemInfo;
    }

    public BaseLinkageGroupItem(T itemInfo) {
        this(false, null, itemInfo);
    }

    public BaseLinkageGroupItem(boolean isHeader, String header) {
        this(isHeader, header, null);
    }

    public static class ItemInfo {
        private String group;
        private String title;

        public ItemInfo(String title, String group) {
            this.title = title;
            this.group = group;
        }

        public String getTitle() {
            return title;
        }

        public String getGroup() {
            return group;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setGroup(String group) {
            this.group = group;
        }

    }

}
