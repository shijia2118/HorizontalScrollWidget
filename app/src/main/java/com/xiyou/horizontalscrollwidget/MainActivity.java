package com.xiyou.horizontalscrollwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.xiyou.mylibrary.HorizontalScrollWidget;
import com.xiyou.mylibrary.listener.OnHorizontalItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    HorizontalScrollWidget<ColumnBean, ColumnAdapter> horizontalScrollWidget;
    List<ColumnBean> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化组件
        horizontalScrollWidget = findViewById(R.id.test);
        //实例化适配器
        ColumnAdapter adapter = new ColumnAdapter(MainActivity.this);
        //获取数据
        mDataList = DataFactory.loadData();
        //链式调用
        horizontalScrollWidget
                .setAdapter(adapter)
                .addOnHorizontalItemClickListener(onHorizontalItemClickListener)
                .setData(mDataList);
    }

    /**
     * item点击事件
     */
    private final OnHorizontalItemClickListener onHorizontalItemClickListener = new OnHorizontalItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(MainActivity.this, horizontalScrollWidget.getDataList().get(position).getText(), Toast.LENGTH_SHORT).show();
        }
    };
}