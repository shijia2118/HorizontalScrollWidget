package com.xiyou.horizontalscrollwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.xiyou.mylibrary.HorizontalScrollWidget;
import com.xiyou.mylibrary.indicator.CircleIndicator;
import com.xiyou.mylibrary.listener.OnHorizontalItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    HorizontalScrollWidget<ColumnBean, ColumnAdapter> horizontalScrollWidget1;
    HorizontalScrollWidget<ColumnBean, ColumnAdapter> horizontalScrollWidget2;

    List<ColumnBean> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化组件
        horizontalScrollWidget1 = findViewById(R.id.test1);
        horizontalScrollWidget2 = findViewById(R.id.test2);

        //实例化适配器
        ColumnAdapter adapter1 = new ColumnAdapter(this);
        ColumnAdapter adapter2 = new ColumnAdapter(this);

        //获取数据
        mDataList = DataFactory.loadData();
        //链式调用
        horizontalScrollWidget1
                .setAdapter(adapter1)
                .addOnHorizontalItemClickListener(onHorizontalItemClickListener)
                .setData(mDataList)
                .build();
        horizontalScrollWidget2
                .setAdapter(adapter2)
                .addOnHorizontalItemClickListener(onHorizontalItemClickListener)
                .setData(mDataList)
                .setIndicator(new CircleIndicator(this))
                .build();
    }

    /**
     * item点击事件
     */
    private final OnHorizontalItemClickListener onHorizontalItemClickListener = new OnHorizontalItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(MainActivity.this, horizontalScrollWidget1.getDataList().get(position).getText(), Toast.LENGTH_SHORT).show();
        }
    };
}