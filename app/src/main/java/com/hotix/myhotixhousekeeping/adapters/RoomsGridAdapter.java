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
import com.hotix.myhotixhousekeeping.models.AffectedRoom;
import com.hotix.myhotixhousekeeping.models.Generic;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;

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
        AppCompatImageView imgStartIcon = (AppCompatImageView) grid.findViewById(R.id.img_room_grid_row_icon_start);
        AppCompatImageView imgEndIcon = (AppCompatImageView) grid.findViewById(R.id.img_room_grid_row_icon_end);

        CardView cvRoomBg = (CardView) grid.findViewById(R.id.cv_room_grid_row);

        AppCompatTextView tvRoomNumberSub = (AppCompatTextView) grid.findViewById(R.id.tv_room_grid_row_sub);
        AppCompatTextView tvRoomNumber = (AppCompatTextView) grid.findViewById(R.id.tv_room_grid_row);
        tvRoomNumber.setText(String.valueOf(mRoomList.get(position).getProdNum()));

        imgStartIcon.setImageResource(R.drawable.guests);
        imgEndIcon.setImageResource(R.drawable.trash);


        if (mRoomList.get(position).getAttributed()) {

            for (Generic sp : GLOBAL_LOGIN_DATA.getStatusProduit()) {
                if (sp.getId() == 8) {
                    cvRoomBg.setCardBackgroundColor(parseColor(sp.getName()));
                }
            }
            //Set Icons
            switch (mRoomList.get(position).getStatutId()) {
                case 1:
                    imgStartIcon.setVisibility(View.GONE);
                    imgEndIcon.setVisibility(View.GONE);
                    tvRoomNumberSub.setText("");
                    break;
                case 2:
                    imgStartIcon.setVisibility(View.GONE);
                    imgEndIcon.setVisibility(View.VISIBLE);
                    tvRoomNumberSub.setText("");
                    break;
                case 5:
                    imgStartIcon.setVisibility(View.VISIBLE);
                    imgEndIcon.setVisibility(View.GONE);
                    tvRoomNumberSub.setText(R.string.text_da);
                    break;
                case 6:
                    imgStartIcon.setVisibility(View.VISIBLE);
                    imgEndIcon.setVisibility(View.GONE);
                    tvRoomNumberSub.setText(R.string.text_du);
                    break;
            }

        } else {

            //Set BG
            for (Generic sp : GLOBAL_LOGIN_DATA.getStatusProduit()) {
                if (sp.getId() == mRoomList.get(position).getStatutId()) {
                    cvRoomBg.setCardBackgroundColor(parseColor(sp.getName()));
                }
            }
            tvRoomNumberSub.setText("");
            //Set Icons
            switch (mRoomList.get(position).getStatutId()) {
                case 1:
                    imgStartIcon.setVisibility(View.GONE);
                    imgEndIcon.setVisibility(View.GONE);
                    break;
                case 2:
                    imgStartIcon.setVisibility(View.GONE);
                    imgEndIcon.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    imgStartIcon.setVisibility(View.VISIBLE);
                    imgEndIcon.setVisibility(View.GONE);
                    break;
                case 4:
                    imgStartIcon.setVisibility(View.VISIBLE);
                    imgEndIcon.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    imgStartIcon.setVisibility(View.VISIBLE);
                    imgEndIcon.setVisibility(View.GONE);
                    break;
                case 6:
                    imgStartIcon.setVisibility(View.VISIBLE);
                    imgEndIcon.setVisibility(View.GONE);
                    break;
                case 7:
                    imgStartIcon.setImageResource(R.drawable.close);
                    imgStartIcon.setVisibility(View.VISIBLE);
                    imgEndIcon.setVisibility(View.GONE);
                    break;
                case 8:
                    imgStartIcon.setVisibility(View.GONE);
                    imgEndIcon.setVisibility(View.GONE);
                    break;
            }
        }

        return grid;
    }
}
