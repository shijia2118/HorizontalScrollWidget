package com.xiyou.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalScrollWidget extends RelativeLayout {

    private final static int DEFAULT_COLUMNS = 5;
    private final static int DEFAULT_ROWS = 2;

    RecyclerView recyclerView;
    ColumnAdapter adapter;
    private  int columns;
    private int rows;

    public HorizontalScrollWidget(Context context) {
        super(context);
    }

    public HorizontalScrollWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context,attrs);
        initView(context);
    }

    public HorizontalScrollWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context,attrs);
        initView(context);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizontalScrollWidget);
        columns = array.getInteger(R.styleable.HorizontalScrollWidget_columns,DEFAULT_COLUMNS);
        rows = array.getInteger(R.styleable.HorizontalScrollWidget_rows,DEFAULT_ROWS);
        array.recycle();

    }


    /**
     * 初始化视图
     * @param context:上下文
     */
    public void initView(Context context){
        View.inflate(context,R.layout.widget_layout,this);
        recyclerView = findViewById(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,rows,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER); //滑动到顶部或底部时,蓝色背景效果
        adapter = new ColumnAdapter(context,DataFactory.loadData(),recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
