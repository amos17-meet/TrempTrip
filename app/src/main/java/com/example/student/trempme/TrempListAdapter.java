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

public class TrempListAdapter extends ArrayAdapter<Tremp> {
    Context context;
    List<Tremp> tremps;



    public TrempListAdapter(Context context, int resource, int textViewResourceId, List<Tremp> tremps) {
        super(context, resource, textViewResourceId, tremps);
        this.context=context;
        this.tremps =tremps;


    }
    //creating the base layout for the users list
    //using the given details from the current user in the users list

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.travel_list_object, parent, false);
        TextView tvFrom = (TextView) view.findViewById(R.id.tvFrom);
        TextView tvTo = (TextView) view.findViewById(R.id.tvTo);
        TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        Tremp tremp = tremps.get(position);
        //Log.w("place position",places.get(position*2)+""+places.get((position*2)+1));
        tvFrom.setText(tremp.getFromName());
        tvTo.setText(tremp.getToName());
        tvTime.setText(tremp.getTrempTime());
        tvDate.setText(tremp.getTrempDate());



        return view;
    }



}



