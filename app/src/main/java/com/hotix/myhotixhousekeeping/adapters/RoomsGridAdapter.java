package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.AffectedRoom;

import java.util.ArrayList;

public class RoomsGridAdapter extends BaseAdapter {

    private ArrayList<AffectedRoom> mRoomList;
    private Context mContext;

    public RoomsGridAdapter(Context context, ArrayList<AffectedRoom> roomList) {
        mContext = context;
        this.mRoomList = roomList;
    }

    @Override
    public int getCount() {
        return mRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRoomList.get(position);
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
            grid = inflater.inflate(R.layout.item_room_grid_row, null);
        } else {
            grid = (View) convertView;
        }
        AppCompatTextView tvRoomNumber = (AppCompatTextView) grid.findViewById(R.id.tv_room_grid_row);
        tvRoomNumber.setText(String.valueOf(mRoomList.get(position).getProdNum()));

        switch (mRoomList.get(position).getStatutId()) {
            case 1:
                if (mRoomList.get(position).getAttributed()) {
                    tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                } else {
                    tvRoomNumber.setBackgroundResource(R.drawable.vac_clean);
                }
                break;
            case 2:
                if (mRoomList.get(position).getAttributed()) {
                    tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                } else {
                    tvRoomNumber.setBackgroundResource(R.drawable.vac_dirty);
                }
                break;
            case 3:
                if (mRoomList.get(position).getAttributed()) {
                    tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                } else {
                    tvRoomNumber.setBackgroundResource(R.drawable.occ_clean);
                }
                break;
            case 4:
                if (mRoomList.get(position).getAttributed()) {
                    tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                } else {
                    tvRoomNumber.setBackgroundResource(R.drawable.occ_dirty);
                }
                break;
            case 5:
                if (mRoomList.get(position).getAttributed()) {
                    tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                } else {
                    tvRoomNumber.setBackgroundResource(R.drawable.expect_dep);
                }
                break;
            case 6:
                if (mRoomList.get(position).getAttributed()) {
                    tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                } else {
                    tvRoomNumber.setBackgroundResource(R.drawable.day_use);
                }
                break;
            case 7:
                if (mRoomList.get(position).getAttributed()) {
                    tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                } else {
                    tvRoomNumber.setBackgroundResource(R.drawable.out_of_order);
                }
                break;
            case 8:
                tvRoomNumber.setBackgroundResource(R.drawable.attributed);
                break;
        }

        return grid;
    }
}
