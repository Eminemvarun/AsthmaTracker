package com.envy.asthmatracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        ImageView imageView,icon;
        TextView textView,ago,desc,linkToArticle;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewNews);
            textView = itemView.findViewById(R.id.textViewNews);
            ago = itemView.findViewById(R.id.textViewMinutesAgo);
            desc= itemView.findViewById(R.id.tvDescription);
            linkToArticle = itemView.findViewById(R.id.linkToArticle);
            icon = itemView.findViewById(R.id.arrowHolder);
        }
    }

    @NonNull
    @Override
    public NewsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(parent.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row3_land, parent, false);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row3, parent, false);
        }
        return new Viewholder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.Viewholder holder, int position) {
        holder.itemView.setTag(mdata.get(position));
        holder.desc.setText(mdata.get(position).getDescription());
        holder.linkToArticle.setText(mdata.get(position).getSource());
        holder.textView.setText(mdata.get(position).getTitle());
        if(mdata.get(holder.getAdapterPosition()).isImageAvailable()) {
            holder.imageView.setImageBitmap(mdata.get(position).getImage());
        }
        else{
        }
        holder.textView.setLinksClickable(true);
        holder.linkToArticle.setVisibility(mdata.get(position).isVisible() ? View.VISIBLE: View.GONE);
        holder.desc.setVisibility(mdata.get(position).isVisible() ? View.VISIBLE: View.GONE);

        int residDown = mContext.getResources().getIdentifier("baseline_arrow_downward_24","drawable", mContext.getPackageName());
        int residUp = mContext.getResources().getIdentifier("baseline_arrow_upward_24","drawable", mContext.getPackageName());

        if(mdata.get(holder.getAdapterPosition()).isVisible()){
            holder.icon.setImageResource(residUp);
        }else{
            holder.icon.setImageResource(residDown);
        }

        //Link to article listener
        holder.linkToArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mdata.get(holder.getAdapterPosition()).getLink()));
                mContext.startActivity(i);
            }
        });
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mdata.get(holder.getAdapterPosition()).setVisible(!mdata.get(holder.getAdapterPosition()).isVisible());
                    notifyItemChanged(holder.getAdapterPosition());
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
