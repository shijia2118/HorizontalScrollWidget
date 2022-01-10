package com.xiyou.horizontalscrollwidget;

/**
 * @Author: Zaaach
 * @Date: 2019/11/22
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class ColumnBean {
    private int icon;
    private String text;

    public ColumnBean(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
