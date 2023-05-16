package com.envy.asthmatracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.envy.asthmatracker.R;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public class addExaberActivity extends AppCompatActivity {
    EditText date1;
    LinearLayout mylayout;
    EditText notes;
    RadioGroup radiogroup1;
    RadioButton r1,r2,r3;
    Button submit;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exaber);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(" Add Exaberation Occurrence");

        }

        //Pointing every variable to xml
        date1 = findViewById(R.id.editTextDate);
        radiogroup1 = findViewById(R.id.myRadioGroup);
        notes = findViewById(R.id.editTextNote);
        submit = findViewById(R.id.submitbutton);
        r1 = findViewById(R.id.radioButton1);
        r2 = findViewById(R.id.radioButton2);
        r3 = findViewById(R.id.radioButton3);

        final int[] enterDay = new int[1];
        final int[] enterMonth = new int[1];
        final int[] enterYear = new int[1];


        //Date Picker Setup

        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(addExaberActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                enterYear[0] = year; enterMonth[0] = monthOfYear; enterDay[0] = dayOfMonth;
                                date1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() - 10000);
                datePickerDialog.show();
            }
        });

        //Submit Button Click Settings
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValues()) {

                    //Preparations
                    String first = new Date(enterYear[0] -1900,enterMonth[0],enterDay[0]).toString();
                    int second = 0;
                    int id1 = r1.getId();
                    int id2 = r2.getId();
                    int id3 = r3.getId();
                    if(radiogroup1.getCheckedRadioButtonId() == id1) {
                        second = 1;
                    } else if (radiogroup1.getCheckedRadioButtonId() == id2) {
                        second = 2;
                    } else if (radiogroup1.getCheckedRadioButtonId() == id3) {
                        second = 3;
                    }else {
                        Log.i("vlogs", "Error Radio ID Nnot Match");
                    }

                    String third = notes.getText().toString();
                    try {
                        ExaberDB mydb = new ExaberDB(addExaberActivity.this);
                        mydb.open();
                        Log.i("vlogs", "Entry Created Table 1: " + mydb.createEntry(first,second,third) + second);
                        mydb.close();
                    }catch (SQLException e){
                        Toast.makeText(addExaberActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(addExaberActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
                    notes.setText("");
                    date1.setText("");
                    radiogroup1.clearCheck();
                    finish();
                } else {
                    Toast.makeText(addExaberActivity.this, "Error. Please Enter All Values", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean checkValues() {
                return (!date1.getText().toString().isEmpty()) && (radiogroup1.getCheckedRadioButtonId() != -1) && (!notes.getText().toString().trim().isEmpty());
            }
        });
    }
}