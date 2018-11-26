package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.Resident;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class ResidentGridAdapter extends BaseAdapter {

    private ArrayList<Resident> mResList;
    private Context mContext;

    public ResidentGridAdapter(Context context, ArrayList<Resident> resList) {
        mContext = context;
        this.mResList = resList;
    }

    @Override
    public int getCount() {
        return mResList.size();
    }

    @Override
    public Object getItem(int position) {
        return mResList.get(position);
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
            grid = inflater.inflate(R.layout.item_resident_grid_sell, null);
        } else {
            grid = (View) convertView;
        }

        AppCompatImageView imgPaxIcon = (AppCompatImageView) grid.findViewById(R.id.img_grid_sell_icon_pax);

        CardView cvRoomBg = (CardView) grid.findViewById(R.id.cv_room_grid_row);

        AppCompatTextView tvPax = (AppCompatTextView) grid.findViewById(R.id.tv_grid_sell_pax);
        AppCompatTextView tvRoomNumber = (AppCompatTextView) grid.findViewById(R.id.tv_room_grid_row);
        tvRoomNumber.setText(String.valueOf(mResList.get(position).getRoom()));

        int i = 0;
        i += mResList.get(position).getNbrA();
        i += mResList.get(position).getNbrE();
        i += mResList.get(position).getNbrB();

        tvPax.setText(String.valueOf(i));

        imgPaxIcon.setVisibility(View.VISIBLE);
        imgPaxIcon.setImageResource(R.drawable.ic_person_white_18dp);

        if (!stringEmptyOrNull(mResList.get(position).getColor())) {
            cvRoomBg.setCardBackgroundColor(parseColor(mResList.get(position).getColor()));
        }


        return grid;
    }
}
