package com.example.testapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class inhalerAdapter extends RecyclerView.Adapter<inhalerAdapter.ViewHolder>{

    ArrayList<InhalerDataClass> data;
    Context ourContext;

    public inhalerAdapter(Context context, ArrayList<InhalerDataClass> data) {
        this.data = data;
        this.ourContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvdate,tvName,tvPuffs,tvNotes;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            tvdate = itemView.findViewById(R.id.tvDateInhaler);
            tvName = itemView.findViewById(R.id.tvNameInhaler);
            tvPuffs = itemView.findViewById(R.id.tvPuffs);
            tvNotes = itemView.findViewById(R.id.tvNotes);
        }

    }

    @NonNull
    @Override
    public inhalerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull inhalerAdapter.ViewHolder holder, int i) {
        holder.itemView.setTag(data.get(i));
        holder.tvdate.setText(data.get(i).getPrettyDate());
        holder.tvName.setText(data.get(i).getName());
        String text;
        if(data.get(i).getPuffs() == 1){
            text =  data.get(i).getPuffs() + " Puff";
        }
        else
        {
            text =  data.get(i).getPuffs() + " Puffs";
        }

        if(data.get(i).getPuffs() == 3){
            holder.tvPuffs.setText("2+ Puffs");
        }
        else{
            holder.tvPuffs.setText(text); //Keep in mind
        }
        holder.tvNotes.setText(data.get(i).getNotes());
        Log.i("vlogs", "onBindViewHolder: Get Notes value is" + data.get(i).getNotes());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
