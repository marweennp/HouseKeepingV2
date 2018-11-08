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
import com.hotix.myhotixhousekeeping.models.Arrival;
import com.hotix.myhotixhousekeeping.models.Panne;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class ArrivalAdapter extends ArrayAdapter<Arrival> {

    Context mContext;
    private ArrayList<Arrival> dataSet;

    public ArrivalAdapter(ArrayList<Arrival> data, Context context) {
        super(context, R.layout.item_arrival_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Arrival dataModel = getItem(position);

        ArrivalAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ArrivalAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_arrival_row, parent, false);

            //tv
            viewHolder.arrival_title = (TextView) convertView.findViewById(R.id.tv_arrival_row_title);
            viewHolder.arrival_desc = (TextView) convertView.findViewById(R.id.tv_arrival_row_desc);
            viewHolder.arrival_date = (TextView) convertView.findViewById(R.id.tv_arrival_row_date);
            viewHolder.arrival_room = (TextView) convertView.findViewById(R.id.tv_arrival_row_room);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ArrivalAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.arrival_title.setText(dataModel.getClient());
        viewHolder.arrival_desc.setText(dataModel.getComment());
        viewHolder.arrival_date.setText(Html.fromHtml(dateColored(dataModel.getDateArrive(), "#616161", "#1ab394", "dd/MM/yyyy", false)+" "+dateColored(dataModel.getDateDepart(), "#616161", "#1ab394", "dd/MM/yyyy", true)));
        viewHolder.arrival_room.setText(dataModel.getRoom());

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView arrival_title;
        TextView arrival_desc;
        TextView arrival_date;
        TextView arrival_room;
    }

}

