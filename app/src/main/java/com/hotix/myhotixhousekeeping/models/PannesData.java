package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PannesData {

    @SerializedName("data")
    @Expose
    private ArrayList<Panne> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ArrayList<Panne> getData() {
        return data;
    }

    public void setData(ArrayList<Panne> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}