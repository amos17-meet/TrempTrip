package com.example.student.trempme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

import java.util.List;

public class TripListAdapter extends ArrayAdapter<Trip> {
    Context context;
    List<Trip> trips;


    public TripListAdapter(Context context, int resource, int textViewResourceId, List<Trip> trips) {
        super(context, resource, textViewResourceId, trips);
        this.context=context;
        this.trips =trips;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.travel_list_object, parent, false);
        TextView tvFrom = (TextView) view.findViewById(R.id.tvFrom);
        TextView tvTo = (TextView) view.findViewById(R.id.tvTo);
        TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        Trip trip = trips.get(position);
        tvFrom.setText(trip.getFromName());
        tvTo.setText(trip.getToName());
        tvTime.setText(trip.getTrempTime());
        tvDate.setText(trip.getTrempDate());



        return view;
    }


}
