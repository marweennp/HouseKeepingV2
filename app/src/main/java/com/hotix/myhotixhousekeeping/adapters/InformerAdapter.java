package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.Informer;

import java.util.ArrayList;

import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;

public class InformerAdapter extends ArrayAdapter<Informer> {

    Context mContext;
    private ArrayList<Informer> dataSet;

    public InformerAdapter(ArrayList<Informer> data, Context context) {
        super(context, R.layout.item_informer_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Informer dataModel = getItem(position);

        InformerAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new InformerAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_informer_row, parent, false);

            //tv
            viewHolder.informer_title = (TextView) convertView.findViewById(R.id.tv_informer_row_title);
            viewHolder.informer_desc = (TextView) convertView.findViewById(R.id.tv_informer_row_desc);
            viewHolder.informer_date = (TextView) convertView.findViewById(R.id.tv_informer_row_date);
            viewHolder.informer_time = (TextView) convertView.findViewById(R.id.tv_informer_row_time);
            viewHolder.informer_login = (TextView) convertView.findViewById(R.id.tv_informer_row_login);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InformerAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.informer_title.setText(mContext.getString(R.string.all_room)+" "+dataModel.getRoom());
        viewHolder.informer_desc.setText(dataModel.getOperation());
        viewHolder.informer_date.setText(Html.fromHtml( dateColored(dataModel.getDate(), "#616161", "#1ab394", "dd/MM/yyyy HH:mm:ss", true)));
        viewHolder.informer_time.setText(dateFormater(dataModel.getDate(),"dd/MM/yyyy HH:mm:ss","HH:mm"));
        viewHolder.informer_login.setText(dataModel.getUser()+":"+dataModel.getPoste());

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView informer_title;
        TextView informer_desc;
        TextView informer_date;
        TextView informer_time;
        TextView informer_login;
    }

}

