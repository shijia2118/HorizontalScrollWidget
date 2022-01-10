package com.xiyou.mylibrary.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.xiyou.mylibrary.listener.OnHorizontalScrollListener;

public class RoundedLinesIndicator extends View {
    private static final int SCROLL_LOCATION_START  = 1;
    private static final int SCROLL_LOCATION_MIDDLE = 2;
    private static final int SCROLL_LOCATION_END    = 3;

    private final Paint mPaint = new Paint();
    private final RectF trackRectF = new RectF();
    private final RectF thumbRectF = new RectF();

    private float radius; //轨道圆角
    private int trackColor; //轨道颜色
    private int thumbColor; //滑块颜色
    private int scrollBarWidth; //滚动条宽度
    private int scrollBarHeight; //滚动条高度(同滑块高度)
    private int mThumbWidth;//滑块宽度
    private float canScrollDistance;

    private float mThumbScale = 0f;
    private float mScrollScale = 0f;
    private float mScrollOffset;
    //当前滚动条位置：起点、滚动中、终点
    private int mScrollLocation = SCROLL_LOCATION_START;

    private RecyclerView mRecyclerView;
    private OnHorizontalScrollListener onHorizontalScrollListener;

    public RoundedLinesIndicator(Context context) {
        super(context);
        initPaint();
    }

    public RoundedLinesIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public RoundedLinesIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundedLinesIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果传了滚动条的宽(高)度值,则使用所传值;否则使用默认值(即布局widget_layout文件中设定的值).
        scrollBarHeight = scrollBarHeight == 0 ? MeasureSpec.getSize(heightMeasureSpec) : scrollBarHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
        drawThumb(canvas);
    }

    /**
     * 设置画笔
     */
    private void initPaint() {
        mPaint.setAntiAlias(true);//设置画笔为无锯齿
        mPaint.setDither(true); //防抖动
        mPaint.setStyle(Paint.Style.FILL); //实心效果
    }

    /**
     * 绘制轨道
     * @param canvas:画布
     */
    private void drawTrack(Canvas canvas) {
        initPaint();
        mPaint.setColor(trackColor);
        trackRectF.set(0, 0,scrollBarWidth, scrollBarHeight);
        canvas.drawRoundRect(trackRectF, radius, radius, mPaint);
    }

    /**
     * 绘制滑块
     * @param canvas:画布
     */
    private void drawThumb(Canvas canvas) {
        initPaint();
        mPaint.setColor(thumbColor);
        float left = (scrollBarWidth - mThumbWidth) / canScrollDistance * mScrollOffset;
        thumbRectF.set(left,0,left + mThumbWidth,scrollBarHeight);
        canvas.drawRoundRect(thumbRectF,radius,radius,mPaint);
    }

    /**
     * 滑动监听
     */
    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
//            if(onHorizontalScrollListener != null) onHorizontalScrollListener
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            computeScrollScale();
            if (onHorizontalScrollListener != null){
                onHorizontalScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }
    };

    /**
     * 滚动距离计算
     */
    private void computeScrollScale() {
        if (mRecyclerView == null) return;
        //RecyclerView已显示宽度(屏幕宽度)
        float mScrollExtent = mRecyclerView.computeHorizontalScrollExtent();
        //RecyclerView实际宽度
        float mScrollRange = mRecyclerView.computeHorizontalScrollRange();
        if (mScrollRange != 0){
            mThumbScale = mScrollExtent / mScrollRange;
        }

        //RecyclerView可以滚动的距离
        canScrollDistance = mScrollRange - mScrollExtent;

        //RecyclerView已经滚动的距离
        mScrollOffset = mRecyclerView.computeHorizontalScrollOffset();
        if (mScrollRange != 0){
            mScrollScale = mScrollOffset / mScrollRange;
        }
        if (mScrollOffset == 0){
            mScrollLocation = SCROLL_LOCATION_START;
        }else if (canScrollDistance == mScrollOffset){
            mScrollLocation = SCROLL_LOCATION_END;
        }else{
            mScrollLocation = SCROLL_LOCATION_MIDDLE;
        }
        postInvalidate();
    }

    /**
     * 设置圆角
     * @param radius:角度
     */
    public void setRadius(float radius){
        this.radius = radius;
    }

    /**
     * 设置轨道颜色
     * @param color:颜色
     */
    public void setTrackColor(@ColorInt int color) {
        this.trackColor = color;
    }

    /**
     * 设置滑块颜色
     * @param color:颜色
     */
    public void setThumbColor(@ColorInt int color) {
        this.thumbColor = color;
    }

    /**
     * 设置滚动条宽度
     * @param width:宽度
     */
    public void setScrollBarWidth(int width){
        this.scrollBarWidth = width;
    }

    /**
     * 设置滚动条高度
     * @param height:高度
     */
    public void setScrollBarHeight(int height){
        this.scrollBarHeight = height;
    }

    /**
     * 设置滑块宽度
     * @param width:滑块宽度
     */
    public void setThumbWidth(int width) {
        this.mThumbWidth = width;
    }

    /**
     * 滑动监听
     * @param listener 监听器
     */
    public void setOnHorizontalScrollListener(OnHorizontalScrollListener listener){
        this.onHorizontalScrollListener = listener;
    }

    /**
     * 匹配recyclerView
     * @param recyclerView:
     */
    public void attachRecyclerView(RecyclerView recyclerView){
        if (mRecyclerView == recyclerView) return;
        mRecyclerView = recyclerView;
        if (mRecyclerView != null){
            mRecyclerView.removeOnScrollListener(onScrollListener);
            mRecyclerView.addOnScrollListener(onScrollListener);
        }
    }
}
