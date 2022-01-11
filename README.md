# HorizontalScrollWidget
水平滑动组件,适用于app首页金刚区.

### 特点
1.行列数可配置;

2.item布局自定义;

3.指示器(包括滑块)的样式可配置;

4.item默认按分页展示;

### 效果图
<img src="https://github.com/shijia2118/images/blob/main/horizontal_scroll_widget_screen_shot.png?raw=true" width="200px">

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
	implementation 'com.github.shijia2118:HorizontalScrollWidget:1.0.0'
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
	.setAdapter(adapter)
        .addOnHorizontalItemClickListener(onHorizontalItemClickListener)
        .setData(mDataList);
```
具体使用请查看demo.


  
