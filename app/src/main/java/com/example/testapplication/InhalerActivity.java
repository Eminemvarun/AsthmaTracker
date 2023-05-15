package com.example.testapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;

public class InhalerActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhaler);

        // Floating +Add Button Click Setup
        final FloatingActionButton addButton = findViewById(R.id.floatingActionButton2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent second = new Intent(InhalerActivity.this, addInhalerActivity.class);
                startActivity(second);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inhaler, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clearDataInhaler) {
            try {
                ExaberDB exaberDB = new ExaberDB(InhalerActivity.this);
                exaberDB.open();
                exaberDB.deleteData(2);
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

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<InhalerDataClass> myArrayList = new ArrayList<>();
        inhalerAdapter myAdapter;
        TextView tvInhalerUse,tvStats1,tvStats2,tvStats3;
        LinearLayout llStats;



        TextView textView = findViewById(R.id.tvInhalerTop);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewInhaler);
        tvInhalerUse = findViewById(R.id.textViewLastInhalerUse);
        tvStats1 = findViewById(R.id.textViewInhalerStats1);
        tvStats2 = findViewById(R.id.textViewInhalerStats2);
        tvStats3 = findViewById(R.id.textViewInhalerStats3);
        llStats = findViewById(R.id.llInhalerStats);


        RecyclerView.LayoutManager layoutManager;
        //layoutManager =  new LinearLayoutManager(InhalerActivity.this,LinearLayoutManager.VERTICAL,false);
        layoutManager = new GridLayoutManager(InhalerActivity.this,1,GridLayoutManager.VERTICAL,false);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                // Remove item from backing list here
                if(swipeDir == ItemTouchHelper.LEFT) {
                    InhalerDataClass inhalerDataClass = (InhalerDataClass) viewHolder.itemView.getTag();

                            AlertDialog.Builder builder = new AlertDialog.Builder(InhalerActivity.this);
                            builder.setMessage("Delete this entry?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try{
                                            ExaberDB exaberDB =  new ExaberDB(InhalerActivity.this);
                                            exaberDB.open();
                                            String colon = "\"";
                                            Log.i("vlogs", "onClick: "  + colon+ inhalerDataClass.getDate()+ colon);
                                            exaberDB.deleteThisData(2,colon + inhalerDataClass.getDate() + colon);
                                            exaberDB.close();
                                            recreate();
                                        }catch (SQLException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            recreate();
                                        }
                                    });
                            builder.show();
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //Reading Recycler View

        try {
            ExaberDB exaberDB = new ExaberDB(InhalerActivity.this);
            exaberDB.open();
            String stringer = exaberDB.getData(2);
            Log.i("vlogs", "Data Returned is "+ stringer);
            if(!stringer.isEmpty()) {
                String[] string = stringer.split("\n");
                for (String s : string) {
                    String[] second = s.split(" ");
                    String mystring = second[1];
                    second[1] = mystring.replaceAll("\\^"," ");
                    for(int i=4;i<second.length;i++){
                        second[3] = second[3] + " "+ second[i];
                    }
                    InhalerDataClass inhalerDataClass = new InhalerDataClass(second[0], second[1],Integer.parseInt(second[2]), second[3]);
                    myArrayList.add(inhalerDataClass);
                }
                myAdapter = new inhalerAdapter(InhalerActivity.this, myArrayList);
                recyclerView.setAdapter(myAdapter);
                textView.setText("Recent Inhaler Uses");
                itemTouchHelper.attachToRecyclerView(recyclerView);
                llStats.setVisibility(View.VISIBLE);
                int a = exaberDB.getThisWeekCount(2);
                int b = exaberDB.getLastWeekCount(2);
                Log.i("vlogs", "Week Count Returned " + a + b );
                tvStats1.setText(String.valueOf(a));
                float c = (float) a/7;
                tvStats2.setText(String.format("%.02f",c));
                tvStats3.setText(b + " -> " + a);
            }
            exaberDB.close();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }


}
