package com.xiyou.mylibrary.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import com.xiyou.mylibrary.utils.PagingScrollHelper;

public class BaseIndicator extends View implements PagingScrollHelper.onPageChangeListener {

    private int indicatorWidth; //指示器宽度
    private int indicatorSelectedWidth; //指示器选中时的宽度
    private int indicatorHeight; //指示器高度
    private int indicatorSelectedHeight; //指示器选中时的高度
    private int indicatorRadius; //圆角度数
    private int mNormalColor; //未选中的颜色
    private int mSelectedColor; //选中的颜色
    private int indicatorNum; //指示器个数,即分页数
    private int space; //圆点间距
    private boolean openIndicatorScale; //指示器选中后,尺寸是否需要变化
    private int currentPositionOfIndicator = 0; //指示器当前索引

    private RecyclerView mRecyclerView;
    private final RectF rectF = new RectF();
    PagingScrollHelper scrollHelper = new PagingScrollHelper();

    private final Paint mPaint = new Paint();

    public BaseIndicator(Context context) {
        super(context);
    }

    public BaseIndicator(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public BaseIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (indicatorNum <= 1) return; //少于1页,不显示
        // (间距 + 未选中宽度) * (总数-1) + 选中宽度
        int measuredWidth = (space + indicatorWidth) * (indicatorNum - 1) + indicatorSelectedWidth;
        int measuredHeight = Math.max(indicatorHeight,indicatorSelectedHeight);
        setMeasuredDimension(measuredWidth,measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (indicatorNum <= 1) return;
        for (int i = 0; i < indicatorNum; i++) {
            int left ; //左边位置
            int top = 0; //顶边位置
            int right; //右边位置
            int bottom; //底边位置
            int color = mNormalColor; //圆点颜色

            if(i == currentPositionOfIndicator){
                //当前为选中的指示器
                left = (indicatorWidth + space) * i;
                right = (indicatorWidth + space) * i + indicatorSelectedWidth;
                bottom = indicatorSelectedHeight;
                color = mSelectedColor;
            }else if(i > currentPositionOfIndicator){
                //位于所选后面的指示器
                left = space * i + indicatorSelectedWidth + indicatorWidth * (i - 1);
                right = (indicatorWidth + space) * i + indicatorSelectedWidth;
                top = (indicatorSelectedHeight - indicatorHeight) / 2;
                bottom = (indicatorSelectedHeight + indicatorHeight) / 2;
            }else {
                //位于所选前面的指示器
                left = (indicatorWidth + space) * i;
                right = (indicatorWidth + space) * i + indicatorWidth;
                top = (indicatorSelectedHeight - indicatorHeight) / 2;
                bottom = (indicatorSelectedHeight + indicatorHeight) / 2;
            }
            mPaint.setColor(color);
            rectF.set(left,top,right,bottom);
            canvas.drawRoundRect(rectF,indicatorRadius,indicatorRadius,mPaint);
        }
    }

    /**
     * 更新指示器位置
     * @param currentPositionOfIndicator 当前位置
     */
    private void updateCurrentPosition(int currentPositionOfIndicator){
        this.currentPositionOfIndicator = currentPositionOfIndicator;
        invalidate();
    }

    /**
     * 指示器选中后,尺寸是否变化
     * @param openIndicatorScale true 变化,false 不变.默认不变
     */
    public void setOpenIndicatorScale(boolean openIndicatorScale){
        this.openIndicatorScale = openIndicatorScale;
    }

    /**
     * 设置圆角度数
     * @param radius 度数
     */
    public void setIndicatorRadius(int radius){
        this.indicatorRadius = radius;
    }

    /**
     * 设置指示器尺寸
     * @param width 宽
     * @param height 高
     */
    public void setIndicatorSize(int width,int height){
        indicatorWidth = width;
        indicatorHeight = height;
        indicatorSelectedWidth = indicatorWidth;
        indicatorSelectedHeight = indicatorHeight;
        if(openIndicatorScale) {
            //若开启,选中后指示器的宽,高和圆角度数均都增加30%.
            indicatorSelectedWidth = (int) (indicatorSelectedWidth * 1.3);
            indicatorSelectedHeight = (int) (indicatorSelectedHeight * 1.3);
            if(indicatorRadius > 0) indicatorRadius = (int) (indicatorRadius * 1.3);
        }
    }

    /**
     * 设置未选中时指示器颜色
     * @param color 指示器颜色
     */
    public void setNormalColor(int color){ mNormalColor = color; }

    /**
     * 设置选中时指示器颜色
     * @param color 指示器颜色
     */
    public void setSelectedColor(int color){
        mSelectedColor = color;
    }

    /**
     * 指示器个数,即分页数
     * @param indicatorNum 指示器个数
     */
    public void setCount(int indicatorNum){
        this.indicatorNum = indicatorNum;
    }

    /**
     * 指示器间距
     * @param space 间距
     */
    public void setSpace(int space){
        this.space = space;
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

    /**
     * 数据更新时,刷新页面
     * @param indicatorNum 页面数
     */
    public void notifyDataSetChanged(int indicatorNum){
        this.indicatorNum = indicatorNum;
        requestLayout();
        invalidate();
    }

    @Override
    public void onPageChange(int index) {
        updateCurrentPosition(index);
    }

}
