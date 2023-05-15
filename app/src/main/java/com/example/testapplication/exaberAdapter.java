package com.example.testapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.SQLException;

public class exaberAdapter extends ArrayAdapter<ExaberDataClass> {

    Context mContext;
    int mLayoutResourceId;
    ExaberDataClass[] mData = null;

    public exaberAdapter(@NonNull Context context, int resource, @NonNull ExaberDataClass[] objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = objects;

    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        //Time to inflate
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        row = layoutInflater.inflate(mLayoutResourceId,parent,false);

        //get reference of different views to inflate

        TextView tvDate = row.findViewById(R.id.dateExaber);
        TextView tvIntensity = row.findViewById(R.id.intensity);
        TextView tvNotes = row.findViewById(R.id.notes);

        //Get Data to inflate

        ExaberDataClass mydata = this.mData[position];

        //The magic

        tvDate.setText(mydata.prettyDate);
        tvIntensity.setText(mydata.mIntensity_String);
        switch(mydata.mIntensity){
            case 1:
                tvIntensity.setTextColor(Color.GREEN);
                break;
            case 2:
                tvIntensity.setTextColor(Color.YELLOW);
                break;
            case 3:
                tvIntensity.setTextColor(Color.RED);
                break;
            default:
                Log.i("vlogs", "getView: Intensity wrong as " + mydata.mIntensity);
                break;
        }
        tvNotes.setText(mydata.mNotes);

        row.setTag(mydata);
        return row;
    }
}
