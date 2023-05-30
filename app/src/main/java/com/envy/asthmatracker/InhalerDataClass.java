package com.envy.asthmatracker;

import android.util.Log;

import java.time.Month;
import java.util.Calendar;

public class InhalerDataClass {
    private String date;
    private String name;
    private final String prettyDate;
    private int puffs;
    private String notes;
    public InhalerDataClass(String date, String name, int puffs, String notes) {
        this.date = date;
        this.name = name;
        this.puffs = puffs;
        this.notes = notes;
        this.prettyDate = convertDate(date);
    }

    public String getPrettyDate() {
        return prettyDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPuffs() {
        return puffs;
    }

    public void setPuffs(int puffs) {
        this.puffs = puffs;
    }


    public void printALL() {
        Log.i("vlogs", "String 1" + this.date);
        Log.i("vlogs", "String 2" + this.name);
        Log.i("vlogs", "String 3" + this.puffs);
        Log.i("vlogs", "String 4" + this.notes);
    }

    private String convertDate(String date) {

        try {
            String[] mystring = date.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(mystring[0]), Integer.parseInt(mystring[1]), Integer.parseInt(mystring[2]));
            Month month = Month.of(Integer.parseInt(mystring[1]));
            return calendar.get(Calendar.DAY_OF_MONTH) + " " + month.toString() + " " + calendar.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
