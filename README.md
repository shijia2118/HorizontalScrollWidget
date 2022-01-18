# HorizontalScrollWidget
水平滑动组件,适用于app首页金刚区.

### 特点
1.行列数可配置;

2.item布局自定义;

3.指示器(包括滑块)的样式可配置;

4.item默认按分页展示;

5.支持圆角矩形和圆点指示器.

### 效果图
<img src="https://github.com/shijia2118/images/blob/main/1642304605488.gif" width="200px">

### 安装
step 1： 项目根目录的build.gradle添加如下配置：
```
allprojects {
    repositories {
			...
        maven { url 'https://jitpack.io' }
    }
}
```
step 2: app下的build.gradle文件中添加依赖:
```
dependencies {
	implementation 'com.github.shijia2118:HorizontalScrollWidget:1.0.4'
}
```

[![](https://jitpack.io/v/shijia2118/HorizontalScrollWidget.svg)](https://jitpack.io/#shijia2118/HorizontalScrollWidget)

### 使用
step 1:xml中添加布局:
```
<com.xiyou.mylibrary.HorizontalScrollWidget
        android:id="@+id/test"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="15dp"
        app:layout_constraintTop_toTopOf="parent"
        cus:row_spacing="15dp"
        cus:background="@drawable/card_bg_r4"
        cus:attach_to_inner="false"
        cus:padding_top="8dp"
        cus:padding_bottom="10dp"/>
```
step 2:自定义item的布局文件,也可以直接使用本库demo中的item_layout.xml.

step 3:自定义适配器adapter,并且继承ColumnBaseAdapter(也可以直接使用本库demo中的CustomAdapter).

step 4:java代码中使用:

```
//实例化组件
HorizontalScrollWidget<ColumnBean, ColumnAdapter> horizontalScrollWidget = findViewById(R.id.test);
//实例化适配器
adapter = new ColumnAdapter(MainActivity.this);
//获取数据
mDataList = DataFactory.loadData();
//链式调用
horizontalScrollWidget
	.setAdapter(adapter) //适配器
        .addOnHorizontalItemClickListener(onHorizontalItemClickListener) //item点击事件
        .setData(mDataList) //获取数据
        .setIndicator(new CircleIndicator(this)) //设置圆点指示器.如果不设,默认为圆角矩形
        .build();
```
本库提供了圆角矩形和圆点指示器的类,可直接使用.
圆角矩形:RoundedLinesIndicator  圆点:CircleIndicator
### 方法
| 方法名                              | 返回类型    | 描述           |
|----------------------------------|---------|--------------|
| setAdapter                       | this    | 设置适配器        |
| addOnHorizontalItemClickListener | this    | item点击事件     |
| setIndicator			   | this    | 设置指示器      |
| getDataList                      | List<T> | 获取数据列表       |
| setData<T>                       | void    | 重新设置column数据 |

### 属性
| Attributes                | format               | describe                                                         |
|---------------------------|----------------------|------------------------------------------------------------------|
| columns                   | integer              | 每页列数                                                             |
| rows                      | integer              | 行数                                                               |
| track\_color              | color\|reference     | 圆角矩形指示器轨道颜色                                                      |
| thumb\_color              | color\|reference     | 圆角矩形指示器滑块颜色                                                      |
| scroll\_bar\_width        | dimension\|reference |  圆角矩形指示器轨道宽度（默认120px）                                       |
| scroll\_bar\_height       | dimension\|reference | 圆角矩形指示器轨道高度（默认9px，同滑块高度）                                         |
| radius                    | dimension\|reference | 圆角矩形指示器圆角度数（默认5px）                                               |
| thumb\_width              | dimension\|reference | 圆角矩形指示器滑块宽度（默认40px）                                              |
| attach\_to\_inner         | boolean              | 滚动条是否在内容里面（默认true）                                               |
| background                | reference            | 背景                                                               |
| row\_spacing              | dimension\|reference | 行间距                                                              |
| indicator\_margin\_top    | dimension\|reference | 指示器顶部边距                                                          |
| indicator\_margin\_bottom | dimension\|reference | 指示器底部距离                                                          |
| padding\_left             | dimension\|reference | 滑动区域左侧内边距                                                        |
| padding\_right            | dimension\|reference | 滑动区域右侧内边距                                                        |
| padding\_top              | dimension\|reference | 滑动区域顶部内边距                                                        |
| padding\_bottom           | dimension\|reference | 滑动区域底部内边距                                                        |
| pageMode                  | dimension\|reference | 分页模式。若为true，数据按行排序，排满1行，再排下一行；若为false，数据按列排序，排满1列，再排下一列。默认为true。 |
| indicator_width           | dimension\|reference | 指示器宽度|
| indicator_height          | dimension\|reference | 指示器高度|
| indicator_radius          | dimension\|reference | 指示器圆角度数|
| indicator_space           | dimension\|reference | 指示器间距|
| open_indicator_scale           | dimension\|reference | 指示器选中后是否需要改变尺寸,默认false|

具体使用请查看demo.


  
