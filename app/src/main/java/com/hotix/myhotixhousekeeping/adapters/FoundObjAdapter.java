package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.FoundObj;

import java.util.ArrayList;

import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;

public class FoundObjAdapter extends ArrayAdapter<FoundObj> {

    Context mContext;
    private ArrayList<FoundObj> dataSet;

    public FoundObjAdapter(ArrayList<FoundObj> data, Context context) {
        super(context, R.layout.item_found_obj_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FoundObj dataModel = getItem(position);

        FoundObjAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new FoundObjAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_found_obj_row, parent, false);

            //tv
            viewHolder.found_obj_date = (TextView) convertView.findViewById(R.id.tv_found_obj_row_date);
            viewHolder.found_obj_time = (TextView) convertView.findViewById(R.id.tv_found_obj_row_time);
            viewHolder.found_obj_location = (TextView) convertView.findViewById(R.id.tv_found_obj_row_location);
            viewHolder.found_obj_desc = (TextView) convertView.findViewById(R.id.tv_found_obj_row_desc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FoundObjAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.found_obj_location.setText(dataModel.getLieu());
        viewHolder.found_obj_desc.setText(dataModel.getDescription());
        viewHolder.found_obj_date.setText(Html.fromHtml(dateColored(dataModel.getDate(), "#616161", "#1ab394", "dd/MM/yyyy hh:mm", true)));
        viewHolder.found_obj_time.setText(dateFormater(dataModel.getDate(), "dd/MM/yyyy hh:mm", "HH:mm"));


        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView found_obj_date;
        TextView found_obj_time;
        TextView found_obj_location;
        TextView found_obj_desc;
    }

}

