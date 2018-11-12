package com.hotix.myhotixhousekeeping.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AffectedRoomData {

    @SerializedName("data")
    @Expose
    private ArrayList<AffectedRoom> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ArrayList<AffectedRoom> getData() {
        return data;
    }

    public void setData(ArrayList<AffectedRoom> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
