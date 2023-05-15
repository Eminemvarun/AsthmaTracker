package com.envy.asthmatracker;

public class zMyDataType {

    NewsDataClass data;
    int position;

    public zMyDataType(NewsDataClass data, int position) {
        this.data = data;
        this.position = position;
    }

    public NewsDataClass getData() {
        return data;
    }

    public void setData(NewsDataClass data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
