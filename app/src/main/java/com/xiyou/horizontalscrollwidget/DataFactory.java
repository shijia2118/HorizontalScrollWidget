package com.xiyou.horizontalscrollwidget;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {

    public static List<ColumnBean> loadData() {
        List<ColumnBean> navs = new ArrayList<>();
        navs.add(new ColumnBean(R.mipmap.pic_1, "打车"));
        navs.add(new ColumnBean(R.mipmap.pic_2, "外卖"));
        navs.add(new ColumnBean(R.mipmap.pic_3, "鲜花"));
        navs.add(new ColumnBean(R.mipmap.pic_4, "蔬菜"));
        navs.add(new ColumnBean(R.mipmap.pic_5, "网上超市"));
        navs.add(new ColumnBean(R.mipmap.pic_6, "买药"));
        navs.add(new ColumnBean(R.mipmap.pic_7, "旅游"));
        navs.add(new ColumnBean(R.mipmap.pic_8, "休闲娱乐"));
        navs.add(new ColumnBean(R.mipmap.pic_9, "美容美发"));
        navs.add(new ColumnBean(R.mipmap.pic_10, "酒店"));
        navs.add(new ColumnBean(R.mipmap.pic_11, "淡优选农产品"));
        navs.add(new ColumnBean(R.mipmap.pic_12, "生鲜零售"));
        navs.add(new ColumnBean(R.mipmap.pic_13, "餐饮"));
        navs.add(new ColumnBean(R.mipmap.pic_14, "测试1"));
        navs.add(new ColumnBean(R.mipmap.pic_15, "logo"));
        navs.add(new ColumnBean(R.mipmap.pic_16, "收银"));
        navs.add(new ColumnBean(R.mipmap.pic_17, "流量"));
        navs.add(new ColumnBean(R.mipmap.pic_18, "网络状态"));
        navs.add(new ColumnBean(R.mipmap.pic_19, "电影"));
        navs.add(new ColumnBean(R.mipmap.pic_20, "音乐"));

        return navs;
    }
}
