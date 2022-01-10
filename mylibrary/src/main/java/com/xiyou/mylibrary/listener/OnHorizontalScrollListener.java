package com.xiyou.mylibrary.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface OnHorizontalScrollListener {

    void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy);
}
