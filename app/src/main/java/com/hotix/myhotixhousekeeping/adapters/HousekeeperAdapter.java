package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.AffectedRoom;
import com.hotix.myhotixhousekeeping.models.FemmesMenage;

import java.util.ArrayList;

public class HousekeeperAdapter extends ArrayAdapter<FemmesMenage> {

    Context mContext;
    private ArrayList<FemmesMenage> dataSet;
    private ArrayList<AffectedRoom> roomsSet;

    public HousekeeperAdapter(ArrayList<FemmesMenage> data, ArrayList<AffectedRoom> rooms, Context context) {
        super(context, R.layout.item_housekeeper_row, data);
        this.dataSet = data;
        this.roomsSet = rooms;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FemmesMenage dataModel = getItem(position);

        HousekeeperAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new HousekeeperAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_housekeeper_row, parent, false);

            //tv
            viewHolder.housekeeper_name = (TextView) convertView.findViewById(R.id.tv_housekeeper_row_name);
            viewHolder.housekeeper_rooms = (TextView) convertView.findViewById(R.id.tv_housekeeper_row_rooms);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HousekeeperAdapter.ViewHolder) convertView.getTag();
        }

        int x = dataModel.getId();
        int i = 0;
        for (AffectedRoom ar : roomsSet) {
            if (ar.getId() == x) {
                i++;
            }
        }

        viewHolder.housekeeper_name.setText(dataModel.getName());
        viewHolder.housekeeper_rooms.setText(String.valueOf(i));

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView housekeeper_name;
        TextView housekeeper_rooms;
    }

}