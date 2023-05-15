package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    TextView tvMain;
    //
    // On CREATE
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
        }
        final Button button = findViewById(R.id.mainbutton1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("vlogs", "App Created ");
                Intent intent1 = new Intent(MainActivity.this, ExaberationsActivity.class);
                startActivity(intent1);
            }
        });

        Button button3 = findViewById(R.id.mainbutton3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InhalerActivity.class));
            }
        });

        Button button2 = findViewById(R.id.buttonResearch);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LatestResearch.class));
            }
        });
    }

    //
    // On START
    //

    @Override
    protected void onStart() {
        super.onStart();
        tvMain= findViewById(R.id.mainText);

        try{
            ExaberDB exaberDB = new ExaberDB(MainActivity.this);
            exaberDB.open();
            String date1  =  exaberDB.getLatestRecordDateOne();
            Log.i("vlogs", "onStart: Date recieved" + date1);
            if(!date1.isEmpty()) {
                //tvMain.setText(Date1)
                String stringer = Calendar.getInstance().getTime().toString();
                long date2inmills = Calendar.getInstance().getTimeInMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date actualDate1 = sdf.parse(date1);
                long difference = Math.abs( date2inmills- actualDate1.getTime());
                int intdifference = (int) difference/(24*60*60*1000);
                String update =  intdifference +" Days Since Last Exaberation";
                tvMain.setText(update);

            }
            exaberDB.close();
        }catch(SQLException | ParseException e ){
            e.printStackTrace();
        }

    }
}