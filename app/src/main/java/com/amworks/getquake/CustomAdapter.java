package com.amworks.getquake;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<EarthquakeData> dataSet;

    public CustomAdapter(Context context, List<EarthquakeData> dataSet) {
        this.context=context;
        this.dataSet=dataSet;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //all views in layout for a single row of RecyclerView
        public TextView place;
        public TextView date;
        public TextView mag;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.mainText);
            date = itemView.findViewById(R.id.date);
            mag = itemView.findViewById(R.id.mag);
        }
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout, parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.place.setText(dataSet.get(position).getPlace());
        holder.date.setText(dataSet.get(position).getDate());
        holder.mag.setText(String.valueOf(dataSet.get(position).getMagnitude()));

        holder.place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri earthquakeUri = Uri.parse("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10");
//
//                // Create a new intent to view the earthquake URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
//
//                // Send the intent to launch a new activity
//                startActivity(websiteIntent);
                Toast.makeText(context, dataSet.get(position).getPlace(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(dataSet.get(position).getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

    }

    private int getMagnitudeColor(float magnitude) {

        int ret;
        if(magnitude>10.0)
            ret = R.color.magnitude10plus;
        else if(magnitude>9.0 && magnitude<10.0)
            ret=R.color.magnitude9;
        else if(magnitude>8.0 && magnitude<9.0)
            ret=R.color.magnitude8;
        else if(magnitude>7.0 && magnitude<8.0)
            ret=R.color.magnitude7;
        else if(magnitude>6.0 && magnitude<7.0)
            ret=R.color.magnitude6;
        else if(magnitude>5.0 && magnitude<6.0)
            ret=R.color.magnitude5;
        else if(magnitude>4.0 && magnitude<5.0)
            ret=R.color.magnitude4;
        else if(magnitude>3.0 && magnitude<4.0)
            ret=R.color.magnitude3;
        else if(magnitude>2.0 && magnitude<3.0)
            ret=R.color.magnitude2;
        else
            ret=R.color.magnitude1;
//        Log.v("myApp","colorCode="+ret);
        return ContextCompat.getColor(context, ret);
    }



    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
