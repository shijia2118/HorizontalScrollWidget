package com.xiyou.mylibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiyou.mylibrary.holder.MyViewHolder;
import com.xiyou.mylibrary.listener.OnHorizontalItemClickListener;

import java.util.List;


public abstract class ColumnBaseAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {

    List<T> mList;
    Context mContext;
    private int mWidth;
    private int columns; //金刚区列数
    RecyclerView mRecyclerView;
    private OnHorizontalItemClickListener onHorizontalItemClickListener;

    public ColumnBaseAdapter(Context mContext){
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = getLayoutId();
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        mWidth = mWidth == 0 ? mRecyclerView.getMeasuredWidth() : mWidth;
        params.width = mWidth / columns;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        onBind(holder,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHorizontalItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList==null ? 0 : mList.size();
    }

    /**
     * item数据
     * @param list item数组
     */
    public void setData(List<T> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    /**
     * 设置recyclerView
     * @param mRecyclerView :
     */
    public void setRecyclerView(RecyclerView mRecyclerView){
        this.mRecyclerView = mRecyclerView;
    }

    /**
     * 设置列数
     * @param columns :列数
     */
    public void setColumns(int columns){
        this.columns = columns;
    }

    public void setOnHorizontalItemClickListener(OnHorizontalItemClickListener listener){
        this.onHorizontalItemClickListener = listener;
    }

    /**
     * item布局
     * @return layoutId
     */
    public abstract int getLayoutId();

    /**
     * 绑定布局
     */
    public abstract void onBind(MyViewHolder holder,int position);
}
