package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.Client;

import java.util.ArrayList;

import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class ClientAdapter extends ArrayAdapter<Client> {

    Context mContext;
    private ArrayList<Client> dataSet;

    public ClientAdapter(ArrayList<Client> data, Context context) {
        super(context, R.layout.item_residents_client_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Client dataModel = getItem(position);

        ClientAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ClientAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_residents_client_row, parent, false);

            viewHolder.client_name = (TextView) convertView.findViewById(R.id.tv_client_row_name);
            viewHolder.client_arreng = (TextView) convertView.findViewById(R.id.tv_client_row_arreng);
            viewHolder.client_fl = (TextView) convertView.findViewById(R.id.tv_client_row_fl_serv);
            viewHolder.client_date = (TextView) convertView.findViewById(R.id.tv_client_row_date);
            viewHolder.date = (RelativeLayout) convertView.findViewById(R.id.rl_client_row_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ClientAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.client_name.setText(dataModel.getName());

        if (!stringEmptyOrNull(dataModel.getArrangement())) {
            viewHolder.client_arreng.setVisibility(View.VISIBLE);
            viewHolder.client_arreng.setText(dataModel.getArrangement());
        }

        if (!stringEmptyOrNull(dataModel.getPremierService())) {
            viewHolder.client_fl.setVisibility(View.VISIBLE);
            viewHolder.client_fl.setText(dataModel.getPremierService() + " - " + dataModel.getDernierService());
        }

        if (!stringEmptyOrNull(dataModel.getDate())) {
            viewHolder.date.setVisibility(View.VISIBLE);
            viewHolder.client_date.setText(dateFormater(dataModel.getDate(), "dd/MM/yyyy", "dd MMM yyyy"));
        }

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView client_name;
        TextView client_arreng;
        TextView client_fl;
        TextView client_date;
        RelativeLayout date;
    }

}

