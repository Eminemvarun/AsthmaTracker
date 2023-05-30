package com.envy.asthmatracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public class addInhalerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inhaler);

        //Pointing every variable to xml
        EditText date = findViewById(R.id.dateInhaler);
        EditText name = findViewById(R.id.nameInhaler);
        Spinner puffs = findViewById(R.id.spinnerInhaler);
        EditText notes = findViewById(R.id.notesInhaler);
        Button button = findViewById(R.id.saveButtonInhaler);


        final int[] enterDay = new int[1];
        final int[] enterMonth = new int[1];
        final int[] enterYear = new int[1];

        //Spinner Setup

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(addInhalerActivity.this,
                R.array.MyArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        puffs.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(" Add Inhaler Use");
        }

        //Date Picker Setup
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(addInhalerActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                enterYear[0] = year;
                                enterMonth[0] = monthOfYear;
                                enterDay[0] = dayOfMonth;
                                String s = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                date.setText(s);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() - 10000);
                datePickerDialog.show();
            }
        });

        //Submit Button Click Settings
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValues()) {

                    //Preparations
                    String first = new Date(enterYear[0] - 1900, enterMonth[0], enterDay[0]).toString();
                    String second = name.getText().toString().replaceAll("\\s", "^");
                    int third;
                    if (!puffs.getSelectedItem().toString().equals("2+")) {
                        third = Integer.parseInt(puffs.getSelectedItem().toString());
                    } else {
                        third = 3;
                    }
                    String fourth = notes.getText().toString();
                    try {
                        ExaberDB mydb = new ExaberDB(addInhalerActivity.this);
                        mydb.open();
                        Log.i("vlogs", "Entry Created TAble 2: " + mydb.createEntry(first, second, third, fourth));
                        mydb.close();
                    } catch (SQLException e) {
                        Toast.makeText(addInhalerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(addInhalerActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
                    notes.setText("");
                    date.setText("");
                    name.setText("");
                    finish();
                } else {
                    Toast.makeText(addInhalerActivity.this, "Error. Please Enter All Values", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean checkValues() {
                return (!date.getText().toString().isEmpty()) && (!name.getText().toString().trim().isEmpty()) && (!notes.getText().toString().trim().isEmpty());
            }
        });
    }
}