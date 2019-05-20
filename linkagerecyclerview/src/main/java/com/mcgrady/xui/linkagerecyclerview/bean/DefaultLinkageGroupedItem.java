package com.mcgrady.xui.linkagerecyclerview.bean;

/**
 * Created by mcgrady on 2019/5/20.
 */
public class DefaultLinkageGroupedItem extends BaseLinkageGroupItem<DefaultLinkageGroupedItem.ItemInfo> {

    public DefaultLinkageGroupedItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public DefaultLinkageGroupedItem(ItemInfo item) {
        super(item);
    }

    public static class ItemInfo extends BaseLinkageGroupItem.ItemInfo {
        private String content;

        public ItemInfo(String title, String group, String content) {
            super(title, group);
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
