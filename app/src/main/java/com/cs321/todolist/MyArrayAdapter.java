package com.cs321.todolist;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 11/22/2016.
 */

public class MyArrayAdapter extends ArraySwipeAdapter<String> {
    public MyArrayAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects){
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public int getSwipeLayoutResourceId(int position){
        return R.id.swipe;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the current view
        SwipeLayout v = (SwipeLayout) super.getView(position, convertView, parent);
        //Log.v("Content", ((TextView)v.findViewById(R.id.task_title)).getText().toString());
        //Log.v("priority is", MainActivity.priority.get(position).toString());

        //set background for this view according to priority
        TextView taskTitle = (TextView) v.findViewById(R.id.task_title);
        if (MainActivity.priority.get(position) == 1){
            taskTitle.setTextColor(Color.BLUE);
        }
        if (MainActivity.priority.get(position) == 3){
            taskTitle.setTextColor(Color.RED);
        }
        if (MainActivity.priority.get(position) == 2) {
            taskTitle.setTextColor(Color.BLACK);
        }
        return v;
    }
}
