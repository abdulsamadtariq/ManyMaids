package com.manymaidsinprovo.Model;

import java.io.Serializable;

public class Area  implements Serializable {

    private int id;
    private String areaName;
    private int indicatorValue;

    public Area() {
    }

    public Area(int id, String areaName, int indicatorValue) {
        this.id = id;
        this.areaName = areaName;
        this.indicatorValue = indicatorValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getIndicatorValue() {
        return indicatorValue;
    }

    public void setIndicatorValue(int indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

}

