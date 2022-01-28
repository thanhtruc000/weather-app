package com.example.doanandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {
    Context context;
    private List<ThoitietHourly> mData;

    // data is passed into the constructor
    public HourlyWeatherAdapter(@NonNull Context context,@NonNull List<ThoitietHourly> data) {
        this.context = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hourly_list_item, parent, false);
        ViewHolder viewHolder = new  ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Hourly.setText(mData.get(position).getHourly());
        Picasso.with(context)
                .load("http://openweathermap.org/img/w/" + mData.get(position).getImage()+ ".png")
                .into(holder.Image);
        holder.Temp.setText(mData.get(position).getTemp()+ "°C");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView Image;
        TextView Temp ;
        TextView Hourly ;


        ViewHolder(View itemView) {
            super(itemView);
            Hourly = itemView.findViewById(R.id.txtHourly);
            Image = itemView.findViewById(R.id.imgIcon);
            Temp = itemView.findViewById(R.id.txttemp);
        }
    }
}
