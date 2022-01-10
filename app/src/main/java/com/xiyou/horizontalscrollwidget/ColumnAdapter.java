package com.xiyou.horizontalscrollwidget;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiyou.mylibrary.adapter.ColumnBaseAdapter;
import com.xiyou.mylibrary.holder.MyViewHolder;

import java.util.List;

public class ColumnAdapter extends ColumnBaseAdapter<ColumnBean> {
    private final Context mContext;
    private List<ColumnBean> mList;

    public ColumnAdapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_layout;
    }

    @Override
    public void onBind(MyViewHolder holder, int position) {
        ImageView icon = holder.itemView.findViewById(R.id.iv_menu_icon);
        TextView text = holder.itemView.findViewById(R.id.tv_menu_text);

        text.setText(mList.get(position).getText());
        Glide.with(mContext)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(mList.get(position).getIcon())
                .into(icon);
    }

    @Override
    public void setData(List<ColumnBean> list){
        super.setData(list);
        this.mList = list;
    }

}
