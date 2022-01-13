package com.xiyou.mylibrary.itemDecoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 定义行间距
 */
public class RowSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;//行间距

    public RowSpacingItemDecoration(int space, Context mContext) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (parent.getLayoutManager() instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            int rows = gridLayoutManager.getSpanCount(); //行数
            int rowPos = position % rows; //所在的行

            //均分垂直间距
            float avg = (rows - 1) * space * 1.0f / rows;
            outRect.top = (int) (rowPos * (space - avg));
            outRect.bottom = (int) (avg - (rowPos * (space - avg)));
        }
    }
}

