package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Viewholder>{

    Context mContext;
    ArrayList<NewsDataClass> mdata;

    public NewsAdapter(Context context, ArrayList<NewsDataClass> data){
        this.mContext= context;
        this.mdata = data;
    }

    public void addData(NewsDataClass params){
        this.mdata.add(params);
    }

    public void clearAll(){
        this.mdata.clear();
    }

    public void addAll(ArrayList<NewsDataClass> mylist) {
        this.mdata.addAll(mylist);
    }


    public class Viewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewNews);
            textView = itemView.findViewById(R.id.textViewNews);
        }
    }

    @NonNull
    @Override
    public NewsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row3,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.Viewholder holder, int position) {
        holder.itemView.setTag(mdata.get(position));

        holder.textView.setText(mdata.get(position).getTitle());
        if(mdata.get(position).getImage()!= null){
            holder.imageView.setImageBitmap(mdata.get(position).getImage());
        }
        holder.textView.setLinksClickable(true);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mdata.get(holder.getAdapterPosition()).getLink()));
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
