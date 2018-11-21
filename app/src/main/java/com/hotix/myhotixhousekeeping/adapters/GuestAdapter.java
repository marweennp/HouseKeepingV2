package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.Generic;

import java.util.ArrayList;

public class GuestAdapter extends ArrayAdapter<Generic> {

    Context mContext;
    private ArrayList<Generic> dataSet;

    public GuestAdapter(ArrayList<Generic> data, Context context) {
        super(context, R.layout.item_housekeeper_row, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Generic dataModel = getItem(position);

        GuestAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new GuestAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_guest_row, parent, false);

            //tv
            viewHolder.guest_name = (TextView) convertView.findViewById(R.id.tv_guest_row_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GuestAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.guest_name.setText(dataModel.getName());

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView guest_name;
    }

}