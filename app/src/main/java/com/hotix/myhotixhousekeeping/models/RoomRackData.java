package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoomRackData {

    @SerializedName("data")
    @Expose
    private ArrayList<RoomRack> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ArrayList<RoomRack> getData() {
        return data;
    }

    public void setData(ArrayList<RoomRack> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}