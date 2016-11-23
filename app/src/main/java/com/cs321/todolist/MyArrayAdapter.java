package com.cs321.todolist;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;

/**
 * Created by Administrator on 11/22/2016.
 */

public class MyArrayAdapter extends ArrayAdapter<String> {
    public MyArrayAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects){
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout v = (RelativeLayout)super.getView(position, convertView, parent);
        Log.v("Content", ((TextView)v.findViewById(R.id.task_title)).getText().toString());
        Log.v("priority is", MainActivity.priority.get(position));

        if (MainActivity.priority.get(position).length() == 5){
            Log.v("What you are? ", "Hahhaaaaaaaaaha");
            v.setBackgroundColor(Color.GREEN);}
        if (MainActivity.priority.get(position).length() == 6){
            Log.v("What you are? ", "Ha");
            v.setBackgroundColor(Color.RED);}
        if (MainActivity.priority.get(position).length() == 8){
            Log.v("What you are? ", "Haha");
            v.setBackgroundColor(Color.BLUE);}
        return v;
    }
}
