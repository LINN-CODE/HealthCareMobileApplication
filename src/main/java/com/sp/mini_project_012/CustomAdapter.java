package com.sp.mini_project_012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {


    public CustomAdapter(Context context, List<String> names){
        super(context, 0,names);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        // Get the data item for this position
        String name = getItem(position);

        // Lookup view for data population
        TextView textView = convertView.findViewById(R.id.clinicName);

        // Populate the data into the template view using the data object
        textView.setText(name);

        // Return the completed view to render on screen
        return convertView;
    }
}
