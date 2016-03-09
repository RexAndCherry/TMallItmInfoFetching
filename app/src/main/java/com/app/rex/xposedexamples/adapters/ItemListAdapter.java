package com.app.rex.xposedexamples.adapters;/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rex.xposedexamples.R;
import com.app.rex.xposedexamples.domain.Item;
import com.app.rex.xposedexamples.view.RatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by drakeet on 6/20/15.
 */
public class ItemListAdapter
        extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> implements View.OnClickListener {

    public static final String TAG = "ItemListAdapter";

    private List<Item> mList;
    private Context mContext;

    public ItemListAdapter(Context context, List<Item> itemList) {
        mList = itemList;
        mContext = context;
    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Item item = mList.get(position);
        int limit = 48;
        String text = item.title.length() > limit ? item.title.substring(0, limit) +
                "..." : item.title;
        viewHolder.mItem = item;
        viewHolder.titleView.setText(text);
        viewHolder.card.setTag(item.title);
        Picasso.with(mContext).load(item.picUrl).placeholder(R.drawable.placeholder).into(viewHolder.mRatioImageView);

        viewHolder.card.setTag(item.jumpUrl);
    }


    @Override public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }


    @Override public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(String)v.getTag());
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_meizhi)
        RatioImageView mRatioImageView;
        @Bind(R.id.tv_title) TextView titleView;
        View card;
        Item mItem;


        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
            mRatioImageView.setOriginalSize(50, 50);
        }

    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
