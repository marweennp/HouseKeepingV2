package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.Arrival;

import java.util.ArrayList;

import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;

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


            viewHolder.arrival_rl_adult = (RelativeLayout) convertView.findViewById(R.id.rl_arrival_row_adult);
            viewHolder.arrival_adult = (TextView) convertView.findViewById(R.id.tv_arrival_row_adult);
            viewHolder.arrival_rl_kid = (RelativeLayout) convertView.findViewById(R.id.rl_arrival_row_kid);
            viewHolder.arrival_kid = (TextView) convertView.findViewById(R.id.tv_arrival_row_kid);
            viewHolder.arrival_rl_bb = (RelativeLayout) convertView.findViewById(R.id.rl_arrival_row_bb);
            viewHolder.arrival_bb = (TextView) convertView.findViewById(R.id.tv_arrival_row_bb);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ArrivalAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.arrival_title.setText(dataModel.getClient());
        viewHolder.arrival_desc.setText(dataModel.getComment());
        viewHolder.arrival_date.setText(Html.fromHtml(dateColored(dataModel.getDateArrive(), "#616161", "#1ab394", "dd/MM/yyyy", false) + " " + dateColored(dataModel.getDateDepart(), "#616161", "#1ab394", "dd/MM/yyyy", true)));
        viewHolder.arrival_room.setText(dataModel.getRoom());

        if (dataModel.getA() > 0) {
            viewHolder.arrival_rl_adult.setVisibility(View.VISIBLE);
            viewHolder.arrival_adult.setText(String.valueOf(dataModel.getA()));
        }else{
            viewHolder.arrival_rl_adult.setVisibility(View.GONE);
            viewHolder.arrival_adult.setText("");
        }

        if (dataModel.getE() > 0) {
            viewHolder.arrival_rl_kid.setVisibility(View.VISIBLE);
            viewHolder.arrival_kid.setText(String.valueOf(dataModel.getE()));
        }else{
            viewHolder.arrival_rl_kid.setVisibility(View.GONE);
            viewHolder.arrival_kid.setText("");
        }

        if (dataModel.getB() > 0) {
            viewHolder.arrival_rl_bb.setVisibility(View.VISIBLE);
            viewHolder.arrival_bb.setText(String.valueOf(dataModel.getB()));
        }else{
            viewHolder.arrival_rl_bb.setVisibility(View.GONE);
            viewHolder.arrival_bb.setText("");
        }

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView arrival_title;
        TextView arrival_desc;
        TextView arrival_date;
        TextView arrival_room;

        RelativeLayout arrival_rl_adult;
        TextView arrival_adult;
        RelativeLayout arrival_rl_kid;
        TextView arrival_kid;
        RelativeLayout arrival_rl_bb;
        TextView arrival_bb;


    }

}

