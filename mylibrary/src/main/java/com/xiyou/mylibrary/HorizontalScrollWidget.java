package com.xiyou.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiyou.mylibrary.adapter.ColumnBaseAdapter;
import com.xiyou.mylibrary.indicator.BaseIndicator;
import com.xiyou.mylibrary.indicator.CircleIndicator;
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
    private boolean pageMode; //分页模式,默认true
    private boolean openIndicatorScale; //指示器选中后,是否改变尺寸
    private int indicatorWidth; //指示器宽度
    private int indicatorHeight; //指示器高度
    private int indicatorRadius; //指示器圆角度数
    private int indicatorSpace; //指示器间距


    private final Context mContext;
    private List<T> mList;
    RecyclerView recyclerView;
    View mIndicator;
    CBA adapter; //金刚区适配器

    public HorizontalScrollWidget(Context context) {
        super(context);
        this.mContext = context;
    }

    public HorizontalScrollWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        parseAttrs(attrs);
        initView();
    }

    public HorizontalScrollWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        parseAttrs(attrs);
        initView();
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.HorizontalScrollWidget);
        rows = array.getInteger(R.styleable.HorizontalScrollWidget_rows,DEFAULT_ROWS);
        columns = array.getInteger(R.styleable.HorizontalScrollWidget_columns,DEFAULT_COLUMNS);
        pageMode = array.getBoolean(R.styleable.HorizontalScrollWidget_pageMode,true);
        background = array.getResourceId(R.styleable.HorizontalScrollWidget_background,-1);
        rowSpacing = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_row_spacing,0);
        isAttachToInner = array.getBoolean(R.styleable.HorizontalScrollWidget_attach_to_inner,true);

        indicatorSpace = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_space,12);
        indicatorWidth = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_width,45);
        indicatorHeight = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_height,6);
        indicatorRadius = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_radius,3);
        openIndicatorScale = array.getBoolean(R.styleable.HorizontalScrollWidget_open_indicator_scale,false);

        paddingTop = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_top,0);
        paddingLeft = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_left,0);
        paddingRight = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_right,0);
        paddingBottom = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_padding_bottom,0);

        radius = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_radius,DEFAULT_SCROLL_BAR_RADIUS);
        thumbWidth = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_thumb_width,DEFAULT_THUMB_WIDTH);
        scrollBarWidth = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_scroll_bar_width,DEFAULT_SCROLL_BAR_WIDTH);
        scrollBarHeight = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_scroll_bar_height,DEFAULT_SCROLL_BAR_HEIGHT);
        trackColor = array.getInteger(R.styleable.HorizontalScrollWidget_track_color,mContext.getResources().getColor(R.color.default_track_color));
        thumbColor = array.getInteger(R.styleable.HorizontalScrollWidget_thumb_color,mContext.getResources().getColor(R.color.default_thumb_color));

        indicatorMarginTop = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_margin_top,DEFAULT_INDICATOR_MARGIN_TOP);
        indicatorMarginBottom = array.getDimensionPixelSize(R.styleable.HorizontalScrollWidget_indicator_margin_bottom,DEFAULT_INDICATOR_MARGIN_BOTTOM);

        array.recycle();
    }

    /**
     * 初始化视图
     */
    private void initView(){
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new LayoutParams(-1, -2));

        recyclerView = new RecyclerView(mContext);
        //为recyclerView适配宽高
        LayoutParams recyclerViewParams = new LayoutParams(-1,-2);
        recyclerView.setLayoutParams(recyclerViewParams);

        //设置行间距
        recyclerView.addItemDecoration(new RowSpacingItemDecoration(rowSpacing,mContext));
        //设置内边距
        setPadding();
        //水平网格布局
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,rows,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //滑动到顶部或底部时,蓝色背景效果
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

    }

    /**
     * 设置滑动区域背景
     */
    private void setBackground(){
        if(background==-1) return;
        if(mList == null || mList.isEmpty()) return;
        if(isAttachToInner) setBackground(mContext.getResources().getDrawable(background));
        else recyclerView.setBackground(mContext.getResources().getDrawable(background));
    }

    /**
     * 设置内边距
     */
    private void setPadding(){
        if(isAttachToInner) {
            if(background != -1 && paddingBottom == 0) paddingBottom = 30;
            setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        }
        else {
            recyclerView.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        }
    }

    /**
     * 配置圆角矩形滚动条
     */
    private void setRoundedLinesIndicator(){
        RoundedLinesIndicator indicator = (RoundedLinesIndicator) mIndicator;

        LayoutParams indicatorParams = new LayoutParams(scrollBarWidth,scrollBarHeight);
        indicatorParams.topMargin = indicatorMarginTop;
        indicatorParams.bottomMargin = indicatorMarginBottom;
        indicator.setLayoutParams(indicatorParams);

        indicator.setRadius(radius);
        indicator.setTrackColor(trackColor);
        indicator.setThumbColor(thumbColor);
        indicator.setThumbWidth(thumbWidth);
        indicator.setScrollBarWidth(scrollBarWidth);
        indicator.setScrollBarHeight(scrollBarHeight);
        indicator.attachToRecyclerView(recyclerView);
    }

    /**
     * 设置圆点滚动条
     */
    private void setCircleIndicator(){
        BaseIndicator indicator = (BaseIndicator) mIndicator;
        LayoutParams indicatorParams = new LayoutParams(-2,-2);
        indicatorParams.topMargin = indicatorMarginTop;
        indicatorParams.bottomMargin = indicatorMarginBottom;
        indicator.setLayoutParams(indicatorParams);

        indicator.setCount(getPageNum());
        indicator.setNormalColor(mContext.getResources().getColor(R.color.default_track_color));
        indicator.setSelectedColor((mContext.getResources().getColor(R.color.default_thumb_color)));
        indicator.setOpenIndicatorScale(openIndicatorScale);
        indicator.setIndicatorSize(indicatorWidth,indicatorHeight);
        indicator.setSpace(indicatorSpace);
        indicator.setIndicatorRadius(indicatorRadius);
        indicator.attachToRecyclerView(recyclerView);
    }

    /**
     * 分页数
     * @return int
     */
    private int getPageNum(){
        if(mList == null || mList.isEmpty()) return 0;
        int pageSize = rows * columns;
        int totalSize = mList.size();
        return totalSize % pageSize == 0 ?totalSize/pageSize : (totalSize/pageSize) + 1;
    }

    private void setAdapter(){
        adapter.setColumns(columns);
        adapter.setData(mList);
        adapter.setRecyclerView(recyclerView);
    }

    /**
     * item数量小于1页时,隐藏指示器
     */
    private void setIndicatorVisibility(){
        if(mIndicator == null) return;
        int pageSize = rows * columns;
        if(mList.size() <= pageSize) mIndicator.setVisibility(GONE);
        else mIndicator.setVisibility(VISIBLE);
    }

    /**
     * 重新排列数据，使数据转换成分页模式
     * 原始数据：
     * 1 3 5 7 9   11 13 15
     * 2 4 6 8 10  12 14 16
     * ==============================
     * 转换之后：
     * 1 2 3 4 5   11 12 13 14 15
     * 6 7 8 9 10  16
     */
    private List<T> rearrange(List<T> data) {
        if (rows <= 1) return data;
        if (data == null || data.isEmpty()) return data;

        int pageSize = rows * columns;//每页item有多少个
        int size = data.size(); //原数据个数
        //如果数据少于一行
        if (size <= columns) return new ArrayList<>(data);
        List<T> newList = new ArrayList<>();
        int newSize = pageSize * (int) Math.ceil(size / (float)pageSize); //转换后的总数量，包括空数据

        //类似置换矩阵
        for (int i = 0; i < newSize; i++) {
            int pageIndex = i / pageSize;
            int columnIndex = (i - pageSize * pageIndex) / rows;
            int rowIndex = (i - pageSize * pageIndex) % rows;
            int destIndex = (rowIndex * columns + columnIndex) + pageIndex * pageSize;

            if (destIndex >= 0 && destIndex < size) {
                newList.add(data.get(destIndex));
            } else {
                newList.add(null);
            }
        }
        return newList;
    }

    /**
     * item数据
     * @param list item数组
     */
    public HorizontalScrollWidget<T,CBA> setData(List<T> list){
        if(list == null) mList = new ArrayList<>();
        else this.mList = list;
        //设置背景
        setBackground();
        setIndicatorVisibility();
        if(pageMode) mList = rearrange(mList); //分页模式下,需要重新排序
        adapter.setData(mList);
        return this;
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
        adapter.setOnHorizontalItemClickListener(listener);
        return this;
    }

    /**
     * 获取列表数据
     * @return List
     */
    public List<T> getDataList(){
        return mList;
    }

    public HorizontalScrollWidget<T,CBA> setIndicator(@Nullable View view){
        mIndicator = view;
        if(view instanceof RoundedLinesIndicator){
            setRoundedLinesIndicator();
        }else if(mIndicator instanceof BaseIndicator){
            setCircleIndicator();
        }else {throw new RuntimeException("The indicator is null, or the types do not match!");}
        return this;
    }

    public void build(){
        if(mIndicator == null) {
            mIndicator = new RoundedLinesIndicator(mContext);
            setRoundedLinesIndicator();
        }
        addView(recyclerView);
        addView(mIndicator);
    }


}
