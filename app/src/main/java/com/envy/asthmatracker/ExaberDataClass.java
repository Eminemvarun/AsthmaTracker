package com.envy.asthmatracker;

import java.time.Month;
import java.util.Calendar;

public class ExaberDataClass {

    public String mDate;
    public String prettyDate;
    public int mIntensity;
    public String mIntensity_String;
    public String mNotes;
    public ExaberDataClass(String date,int intensity, String notes){
        this.mDate = date;
        this.mIntensity = intensity;

        switch (intensity) {
            case 1:
                this.mIntensity_String = "Low";
                break;
            case 2:
                this.mIntensity_String = "Medium";
                break;
            case 3:
                this.mIntensity_String = "High";
                break;
            default:
                this.mIntensity_String = "Unknown";
        }
        this.mNotes = notes;
        this.prettyDate = convertDate(date);
    }

    private String convertDate(String date) {

        try {
            String[] mystring = date.split("-");
            Calendar calendar =  Calendar.getInstance();
            calendar.set(Integer.parseInt(mystring[0]),Integer.parseInt(mystring[1]),Integer.parseInt(mystring[2]));
            Month month = Month.of(Integer.parseInt(mystring[1]));
            return calendar.get(Calendar.DAY_OF_MONTH) + " " +month.toString() +" " + calendar.get(Calendar.YEAR);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void logEverything (String date){

    }
}
