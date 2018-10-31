package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.UserPermissions;

import java.util.ArrayList;

public class UsersSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<UserPermissions> mData;
    LayoutInflater inflter;

    public UsersSpinnerAdapter(Context applicationContext, ArrayList<UserPermissions> mData) {
        this.context = applicationContext;
        this.mData = mData;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_row_item, null);
        AppCompatImageView icon = (AppCompatImageView) view.findViewById(R.id.img_spinner_row_icon);
        AppCompatTextView names = (AppCompatTextView) view.findViewById(R.id.tv_spinner_row_text);
        names.setText(mData.get(i).getLogin());
        return view;
    }
}