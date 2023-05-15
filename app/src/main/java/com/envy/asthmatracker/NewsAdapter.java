package com.envy.asthmatracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.envy.asthmatracker.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        TextView ago;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewNews);
            textView = itemView.findViewById(R.id.textViewNews);
            ago = itemView.findViewById(R.id.textViewMinutesAgo);
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(mdata.get(position).date);
        } catch (ParseException e) {
            Log.i("vlogs", "onBindViewHolder: Error Parsing date");
            e.printStackTrace();
        }

        PrettyTime p  = new PrettyTime();
        String datetime= p.format(convertedDate);
        holder.ago.setText(datetime);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
