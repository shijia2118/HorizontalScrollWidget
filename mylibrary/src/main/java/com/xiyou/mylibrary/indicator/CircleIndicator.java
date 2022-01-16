package com.xiyou.mylibrary.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import com.xiyou.mylibrary.listener.PagingScrollHelper;

public class CircleIndicator extends View implements PagingScrollHelper.onPageChangeListener {

    private int mNormalRadius; //未选中圆点半径
    private int mSelectedRadius; //选中圆点半径
    private int mNormalWidth; //未选中圆点直径
    private int mSelectedWidth; //选中圆点直径
    private int mNormalColor; //未选中圆点颜色
    private int mSelectedColor; //选中圆点颜色
    private int dotsNum; //圆点个数,即分页数
    private int space; //圆点间距
    private int currentPositionOfIndicator = 0; //指示器当前位置

    private RecyclerView mRecyclerView;

    PagingScrollHelper scrollHelper = new PagingScrollHelper();


    private final Paint mPaint = new Paint();

    public CircleIndicator(Context context) {
        super(context);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (dotsNum <= 1) return; //少于1页,不显示
        // (间距 + 未选中圆点宽度) * (总数-1) + 选中圆点的宽度
        int measuredWidth = (space + mNormalWidth) * (dotsNum - 1) + mSelectedWidth;
        int measuredHeight = Math.max(mNormalWidth,mSelectedWidth);
        setMeasuredDimension(measuredWidth,measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dotsNum <= 1) return;
        for (int i = 0; i < dotsNum; i++) {
            int x ; //圆心x轴的值
            int y = Math.max(mSelectedRadius, mNormalRadius); //圆心y轴的值
            int radius = mNormalRadius; //所需绘制半径
            int color = mNormalColor; //圆点颜色

            if(i < currentPositionOfIndicator){
                //前面的圆点
                x = (space + mNormalWidth) * i + mNormalRadius;
            }else if(i > currentPositionOfIndicator){
                //后面的圆点
                x = space * i + mNormalWidth * (i - 1) + mSelectedWidth + mNormalRadius;
            }else {
                //当前圆点
                x = (space + mNormalWidth) * i + mSelectedRadius;
                color = mSelectedColor;
                radius = mSelectedRadius;
            }
            mPaint.setColor(color);
            canvas.drawCircle(x, y, radius, mPaint);
        }
    }

    /**
     * 设置未选中圆点大小
     * @param normalSize 未选中圆点大小
     */
    public void setNormalSize(int normalSize){
        mNormalWidth = normalSize;
        mNormalRadius = (int) Math.ceil(normalSize/2f);
    }

    /**
     * 设置选中圆点大小
     * @param selectedSize 圆点大小
     */
    public void setSelectedSize(int selectedSize){
        mSelectedWidth = selectedSize;
        mSelectedRadius = (int) Math.ceil(selectedSize/2f);
    }

    /**
     * 设置未选中圆点颜色
     * @param color 圆点颜色
     */
    public void setNormalColor(int color){
        mNormalColor = color;
    }

    /**
     * 设置选中圆点颜色
     * @param color 圆点颜色
     */
    public void setSelectedColor(int color){
        mSelectedColor = color;
    }

    /**
     * 圆点个数,即分页数
     * @param dotsNum 圆点个数
     */
    public void setCount(int dotsNum){
        this.dotsNum = dotsNum;
    }

    /**
     * 圆点间距
     * @param space 间距
     */
    public void setSpace(int space){
        this.space = space;
    }

    /**
     * 更新指示器位置
     * @param currentPositionOfIndicator 当前位置
     */
    public void updateCurrentPosition(int currentPositionOfIndicator){
        this.currentPositionOfIndicator = currentPositionOfIndicator;
        invalidate();
    }

    /**
     * 匹配recyclerView
     * @param recyclerView:
     */
    public void attachToRecyclerView(RecyclerView recyclerView){
        if (mRecyclerView == recyclerView) return;
        mRecyclerView = recyclerView;
        if (mRecyclerView != null){
            scrollHelper.setUpRecycleView(recyclerView);
            scrollHelper.setOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageChange(int index) {
        updateCurrentPosition(index);
    }
}
