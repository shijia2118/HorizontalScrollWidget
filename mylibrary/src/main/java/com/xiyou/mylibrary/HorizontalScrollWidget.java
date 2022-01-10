package com.xiyou.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiyou.mylibrary.adapter.ColumnBaseAdapter;
import com.xiyou.mylibrary.indicator.RoundedLinesIndicator;
import com.xiyou.mylibrary.itemDecoration.RowSpacingItemDecoration;
import com.xiyou.mylibrary.listener.OnHorizontalItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 潇洒的然然
 * @Date: 2022/01/01
 * @Email: 228749551@qq.com
 * @Description: 水平滑动组件,一般适用于app首页金刚区布局
 */
public class HorizontalScrollWidget<T,CBA extends ColumnBaseAdapter<T>> extends LinearLayout {

    private final static int DEFAULT_COLUMNS = 5; //默认列数
    private final static int DEFAULT_ROWS = 2;  //默认行数
    private final static int DEFAULT_THUMB_WIDTH = 40; //滑块默认宽度
    private final static int DEFAULT_SCROLL_BAR_RADIUS = 5; //滚动条默认圆角
    private final static int DEFAULT_SCROLL_BAR_WIDTH = 120; //滚动条默认宽度
    private final static int DEFAULT_SCROLL_BAR_HEIGHT = 9; //滚动条默认高度(滑块高度同)
    private final static int DEFAULT_INDICATOR_MARGIN_TOP = 30; //指示器距离顶部默认距离
    private final static int DEFAULT_INDICATOR_MARGIN_BOTTOM = 0; //指示器距离底部默认距离

    private  int columns; //列数
    private int rows; //行数
    private int trackColor; //轨道颜色
    private int thumbColor; //滑块颜色
    private int thumbWidth; //滑块宽度
    private int scrollBarWidth; //滚动条宽度
    private int scrollBarHeight; //滚动条高度
    private float radius; //滚动条圆角度数
    private boolean isAttachToInner; //滚动条是否在布局内部
    private int background; //水平滑动区域背景图
    private int indicatorMarginTop; //指示器距离顶部距离
    private int indicatorMarginBottom; //指示器距离底部距离
    private int rowSpacing; //行间距
    private int paddingLeft; //左侧内边距
    private int paddingRight; //右侧内边距
    private int paddingTop; //顶部内边距
    private int paddingBottom; //底部内边距

    private List<T> mList;
    RecyclerView recyclerView;
    RoundedLinesIndicator roundedLinesIndicator;
    private OnHorizontalItemClickListener onHorizontalItemClickListener;
    CBA adapter; //金刚区适配器

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
        rows = array.getInteger(R.styleable.HorizontalScrollWidget_rows,DEFAULT_ROWS);
        columns = array.getInteger(R.styleable.HorizontalScrollWidget_columns,DEFAULT_COLUMNS);
        background = array.getResourceId(R.styleable.HorizontalScrollWidget_background,-1);
        rowSpacing = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_row_spacing,0);
        isAttachToInner = array.getBoolean(R.styleable.HorizontalScrollWidget_attach_to_inner,true);
        paddingTop = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_top,0);
        paddingLeft = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_left,0);
        paddingRight = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_right,0);
        paddingBottom = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_bottom,0);
        radius = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_radius,DEFAULT_SCROLL_BAR_RADIUS);
        thumbWidth = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_thumb_width,DEFAULT_THUMB_WIDTH);
        scrollBarWidth = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_scroll_bar_width,DEFAULT_SCROLL_BAR_WIDTH);
        scrollBarHeight = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_scroll_bar_height,DEFAULT_SCROLL_BAR_HEIGHT);
        indicatorMarginTop = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_margin_top,DEFAULT_INDICATOR_MARGIN_TOP);
        trackColor = array.getInteger(R.styleable.HorizontalScrollWidget_track_color,context.getResources().getColor(R.color.default_track_color));
        thumbColor = array.getInteger(R.styleable.HorizontalScrollWidget_thumb_color,context.getResources().getColor(R.color.default_thumb_color));
        indicatorMarginBottom = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_margin_bottom,DEFAULT_INDICATOR_MARGIN_BOTTOM);

        array.recycle();
    }

    /**
     * 初始化视图
     * @param context:上下文
     */
    private void initView(Context context){
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new LayoutParams(-1, -2));

        recyclerView = new RecyclerView(context);
        //为recyclerView适配宽高
        LayoutParams recyclerViewParams = new LayoutParams(-1,-2);
        recyclerView.setLayoutParams(recyclerViewParams);
        //设置背景
        setBackground(context);
        //设置行间距
        recyclerView.addItemDecoration(new RowSpacingItemDecoration(rowSpacing,context));
        //设置内边距
        recyclerView.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        //水平网格布局
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,rows,RecyclerView.HORIZONTAL,false);
        gridLayoutManager.isAutoMeasureEnabled();
        recyclerView.setLayoutManager(gridLayoutManager);
        //滑动到顶部或底部时,蓝色背景效果
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //圆角矩形指示器
        roundedLinesIndicator = new RoundedLinesIndicator(context);
        setScrollBar();

        addView(recyclerView);
        addView(roundedLinesIndicator);

    }

    /**
     * 设置滑动区域背景
     * @param context:上下文
     */
    private void setBackground(Context context){
        if(background==-1) return;
        if(isAttachToInner) setBackground(context.getResources().getDrawable(background));
        else recyclerView.setBackground(context.getResources().getDrawable(background));
    }

    /**
     * 设置滚动条
     */
    private void setScrollBar(){
        LayoutParams indicatorParams = new LayoutParams(scrollBarWidth,scrollBarHeight);
        indicatorParams.topMargin = indicatorMarginTop;
        indicatorParams.bottomMargin = indicatorMarginBottom;
        roundedLinesIndicator.setLayoutParams(indicatorParams);

        roundedLinesIndicator.setRadius(radius);
        roundedLinesIndicator.setTrackColor(trackColor);
        roundedLinesIndicator.setThumbColor(thumbColor);
        roundedLinesIndicator.setThumbWidth(thumbWidth);
        roundedLinesIndicator.setScrollBarWidth(scrollBarWidth);
        roundedLinesIndicator.setScrollBarHeight(scrollBarHeight);
        roundedLinesIndicator.attachRecyclerView(recyclerView);
    }

    private void setAdapter(){
        adapter.setColumns(columns);
        adapter.setData(mList);
        adapter.setRecyclerView(recyclerView);
    }


    /**
     * item数据
     * @param list item数组
     */
    public void setData(List<T> list){
        if(list == null) mList = new ArrayList<>();
        else this.mList = list;
        adapter.setData(mList);
    }

    /**
     * 设置适配器
     * @param adapter 适配器
     */
    public HorizontalScrollWidget<T,CBA> setAdapter(CBA adapter){
        this.adapter = adapter;
        setAdapter();
        recyclerView.setAdapter(adapter);
        return this;
    }

    /**
     * item点击事件
     * @param listener:监听器
     * @return HorizontalScrollWidget
     */
    public HorizontalScrollWidget<T,CBA> addOnHorizontalItemClickListener(OnHorizontalItemClickListener listener){
        this.onHorizontalItemClickListener = listener;
        adapter.setOnHorizontalItemClickListener(onHorizontalItemClickListener);
        return this;
    }



}
