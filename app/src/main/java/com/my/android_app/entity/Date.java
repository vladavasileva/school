package com.my.android_app.entity;

public class Date extends Entity{
    private String date;
    private String time_periods;

    public Date(int id, String date, String time_periods) {
        super(id);
        this.date = date;
        this.time_periods = time_periods;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_periods() {
        return time_periods;
    }

    public void setTime_periods(String time_periods) {
        this.time_periods = time_periods;
    }
}
