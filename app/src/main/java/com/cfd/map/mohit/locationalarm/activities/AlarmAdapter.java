package com.cfd.map.mohit.locationalarm.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cfd.map.mohit.locationalarm.R;

import java.util.ArrayList;

/**
 * Created by Mohit on 2/2/2017.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {

    private ArrayList<Alarm> mAlarms;
    private static MyClickListener myClickListener;

    public AlarmAdapter(ArrayList<Alarm> myAlarms) {
        myAlarms = myAlarms;
    }

    public static class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //declare view items for each view in alarm_item.xml


        public AlarmHolder(View v) {
            super(v);

            //find each view in the alarm_item custom row

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getLayoutPosition(), view);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    @Override
    public AlarmAdapter.AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_item, parent, false);

        AlarmHolder movieHolder = new AlarmHolder(view);
        return movieHolder;
    }

    @Override
    public void onBindViewHolder(AlarmAdapter.AlarmHolder holder, int position) {

        //Set values to the alarm_item layout (custom row)

    }

    @Override
    public int getItemCount() {
        try {
            return mAlarms.size();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return 0;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

    }
}
