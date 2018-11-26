package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.DasbordItem;

import java.util.ArrayList;

public class DashbordGridAdapter extends BaseAdapter {

    private ArrayList<DasbordItem> mItems;
    private Context mContext;

    public DashbordGridAdapter(Context context, ArrayList<DasbordItem> items) {
        mContext = context;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.item_dashbord_grid_sell, null);
        } else {
            grid = (View) convertView;
        }

        AppCompatImageView imgIcon = (AppCompatImageView) grid.findViewById(R.id.img_grid_sell_icon);

        AppCompatTextView tvTitle = (AppCompatTextView) grid.findViewById(R.id.tv_grid_sell_title);

        imgIcon.setImageDrawable(mItems.get(position).getIcon());
        tvTitle.setText(mItems.get(position).getTitle());

        return grid;
    }
}

