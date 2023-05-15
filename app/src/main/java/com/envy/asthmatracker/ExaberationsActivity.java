package com.envy.asthmatracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.envy.asthmatracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;

public class ExaberationsActivity extends AppCompatActivity {


    TextView myTextView,tvStats1,tvStats2,tvStats3;
    LinearLayout llStats;
    ListView myListView;
    ArrayAdapter<String> myArrayAdapter;
    exaberAdapter myfirstadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exaberations);

        // Floating +Add Button Click Setup
        final FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent second = new Intent(ExaberationsActivity.this, addExaberActivity.class);
                startActivity(second);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        myTextView = findViewById(R.id.myTextView);
        myListView = findViewById(R.id.myListView);

        tvStats1 = findViewById(R.id.textViewStats1);
        tvStats2 = findViewById(R.id.textViewStats2);
        tvStats3 = findViewById(R.id.textViewStats3);
        llStats = findViewById(R.id.llStats);
        //Display List View at ONStart()

        try {
            // Open SQL data fetch
            ExaberDB exaberDB = new ExaberDB(ExaberationsActivity.this);
            exaberDB.open();
            String data = exaberDB.getData(1);

            // Getting strings ready without ID
            if (!data.isEmpty()) {
                String[] Second;
                String[] myString = data.split("\n");
                ExaberDataClass[] myArrayofExaber = new ExaberDataClass[myString.length];

                // Splitting string into words and creating new objects of ExaberDataClass
                for (int i = 0; i < myString.length; i++) {
                    Second = myString[i].split(" ");
                    for (int j = 4; j < Second.length; j++) {
                        Second[3] = Second[3] + " " + Second[j];
                    }
                    myArrayofExaber[i] = new ExaberDataClass(Second[1], Integer.parseInt(Second[2]), Second[3]);
                }
                Log.i("vlogs", String.valueOf(myArrayofExaber.length));
                myfirstadapter = new exaberAdapter(ExaberationsActivity.this, R.layout.row, myArrayofExaber);

                // Setting adapter to ListView
                if (myListView != null) {
                    myListView.setAdapter(myfirstadapter);
                } else {
                    Toast.makeText(this, "isnull", Toast.LENGTH_SHORT).show();
                }

                myTextView.setText("Top Recent Exaberations");
                llStats.setVisibility(View.VISIBLE);
                int thisWeekCount = exaberDB.getThisWeekCount(1);
                int lastWeekCount = exaberDB.getLastWeekCount(1);
                float v = (float)thisWeekCount/7;
                Log.i("vlogs", "FLoat Value: " + v);
                tvStats1.setText(String.valueOf(thisWeekCount));
                tvStats2.setText(String.format("%.02f", v));
                tvStats3.setText(lastWeekCount + " -> " + thisWeekCount);

            }

            // Close database connection
            exaberDB.close();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Long click listener for ListView items
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ExaberDataClass exaberDataClass = (ExaberDataClass) view.getTag();

                // Showing AlertDialog for confirmation of deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(ExaberationsActivity.this);
                builder.setMessage("Delete this entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try{
                                    ExaberDB exaberDB =  new ExaberDB(ExaberationsActivity.this);
                                    exaberDB.open();
                                    Log.i("vlogs", "Long click code ran");
                                    String colon = "\"";
                                    exaberDB.deleteThisData(1,colon + exaberDataClass.mDate + colon);
                                    exaberDB.close();
                                    myfirstadapter.notifyDataSetChanged();
                                    recreate();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exaberations, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //CLEAR DATA CODE
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cleardata) {
            try {
                ExaberDB exaberDB = new ExaberDB(ExaberationsActivity.this);
                exaberDB.open();
                exaberDB.deleteData(1);
                exaberDB.close();
                Log.i("vlogs", "onOptionsItemSelected: Code ran");
                recreate();
            } catch (SQLException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "All Data Cleared", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Match Found", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}