package com.xiyou.mylibrary.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CircleIndicator extends View {

    private static int DISTANCE_OF_BOUNDS; //超过这个距离后翻页
    private static int VISIBLE_SLIDING_DISTANCE; //可见滑动距离

    private int mNormalRadius; //未选中圆点半径
    private int mSelectedRadius; //选中圆点半径
    private int maxRadius; //最大半径
    private int mNormalWidth; //未选中圆点直径
    private int mSelectedWidth; //选中圆点直径
    private int mNormalColor; //未选中圆点颜色
    private int mSelectedColor; //选中圆点颜色
    private int dotsNum; //圆点个数,即分页数
    private int space; //圆点间距
    private int currentPositionOfIndicator = 0; //指示器当前位置

    private RecyclerView mRecyclerView;
    private float accumulatedSlipDistance = 0; // 累计滑动距离
    private float currentSlipDistance = 0; // 本次滑动距离
    private int slidingState = 0; // 滚动状态

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
        VISIBLE_SLIDING_DISTANCE = MeasureSpec.getSize(widthMeasureSpec);
        DISTANCE_OF_BOUNDS = VISIBLE_SLIDING_DISTANCE / 3; //滑动超过1/3页面距离时翻页有效;否则滚回原位置.
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
     * 滑动监听
     */
    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //0: 停止滚动且手指移开; 1: 开始滚动; 2: 手指做了抛的动作（手指离开屏幕前，带着滑了一下）
            switch (newState) {
                case 2:
                    slidingState = 2;
                    break;
                case 1:
                    slidingState = 1;
                    break;
                case 0:
                    if (currentSlipDistance == 0) break;
                    slidingState = 0;
                    if (currentSlipDistance < 0) { // 上页
                        currentPositionOfIndicator = (int) Math.ceil(accumulatedSlipDistance / VISIBLE_SLIDING_DISTANCE);
                        if (currentPositionOfIndicator * VISIBLE_SLIDING_DISTANCE - accumulatedSlipDistance < DISTANCE_OF_BOUNDS) {
                            currentPositionOfIndicator += 1;
                        }
                    } else { // 下页
                        currentPositionOfIndicator = (int) Math.ceil(accumulatedSlipDistance / VISIBLE_SLIDING_DISTANCE) + 1;
                        if (currentPositionOfIndicator <= dotsNum) {
                            if (accumulatedSlipDistance - (currentPositionOfIndicator - 2) * VISIBLE_SLIDING_DISTANCE < DISTANCE_OF_BOUNDS) {
                                // 如果这一页滑出距离不足，则定位到前一页
                                currentPositionOfIndicator -= 1;
                            }
                        } else {
                            currentPositionOfIndicator = dotsNum;
                        }
                    }
                    // 执行自动滚动
                    mRecyclerView.smoothScrollBy((int) ((currentPositionOfIndicator - 1) * VISIBLE_SLIDING_DISTANCE - accumulatedSlipDistance), 0);
                    // 修改指示器选中项
                    currentPositionOfIndicator = currentPositionOfIndicator -1;
                    currentSlipDistance = 0;
                    invalidate();
                    break;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            accumulatedSlipDistance += dx;
            if (slidingState == 1) {
                currentSlipDistance += dx;
            }

        }
    };

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
    public void attachRecyclerView(RecyclerView recyclerView){
        if (mRecyclerView == recyclerView) return;
        mRecyclerView = recyclerView;
        if (mRecyclerView != null){
            mRecyclerView.removeOnScrollListener(onScrollListener);
            mRecyclerView.addOnScrollListener(onScrollListener);
        }
    }

}
