package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.Panne;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;


public class OrderAdapter extends ArrayAdapter<Panne> {

    Context mContext;
    private ArrayList<Panne> dataSet;

    public OrderAdapter(ArrayList<Panne> data, Context context) {
        super(context, R.layout.item_order_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Panne dataModel = getItem(position);

        OrderAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new OrderAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_order_row, parent, false);

            //tv
            viewHolder.order_date = (TextView) convertView.findViewById(R.id.tv_order_row_date);
            viewHolder.order_time = (TextView) convertView.findViewById(R.id.tv_order_row_time);
            viewHolder.order_location = (TextView) convertView.findViewById(R.id.tv_order_row_location);
            viewHolder.order_desc = (TextView) convertView.findViewById(R.id.tv_order_row_desc);
            viewHolder.order_priority = (TextView) convertView.findViewById(R.id.tv_order_row_priority);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.order_location.setText(dataModel.getLieu());
        viewHolder.order_date.setText(Html.fromHtml(dateColored(dataModel.getDate(), "#616161", "#1ab394", "dd/MM/yyyy hh:mm", true)));
        viewHolder.order_time.setText(dateFormater(dataModel.getDate(), "dd/MM/yyyy hh:mm", "HH:mm"));

        if (stringEmptyOrNull(dataModel.getDescription()) || dataModel.getDescription().equals("NULL")) {
            viewHolder.order_desc.setText("");
        } else {
            viewHolder.order_desc.setText(dataModel.getDescription());
        }

        if (dataModel.getUrgent()) {
            viewHolder.order_priority.setText(R.string.text_order_urgent);
            viewHolder.order_priority.setVisibility(View.VISIBLE);
        } else {
            viewHolder.order_priority.setText("");
            viewHolder.order_priority.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView order_date;
        TextView order_time;
        TextView order_location;
        TextView order_desc;
        TextView order_priority;
    }

}

