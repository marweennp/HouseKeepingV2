package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArrivalData {

    @SerializedName("data")
    @Expose
    private ArrayList<Arrival> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ArrayList<Arrival> getData() {
        return data;
    }

    public void setData(ArrayList<Arrival> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}