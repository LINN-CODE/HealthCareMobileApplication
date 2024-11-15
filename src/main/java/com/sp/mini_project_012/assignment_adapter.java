package com.sp.mini_project_012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class assignment_adapter extends ArrayAdapter<assignment> {
    public assignment_adapter(Context context, ArrayList<assignment> assignments){
        super(context, 0, assignments);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent){
        assignment Assignment = getItem(position);

        if(converView == null){
            converView = LayoutInflater.from(getContext()).inflate(R.layout.activity_user_mainpage_bookin_row, parent, false);
        }
        TextView assignmentDateTextView = converView.findViewById(R.id.user_mainpage_bookin_assignment_date);
        TextView assignmentShiftTextView = converView.findViewById(R.id.user_mainpage_bookinassignment_shift);
        if (Assignment != null) {
            assignmentDateTextView.setText(Assignment.getDate());
            assignmentShiftTextView.setText(Assignment.getShift());
        }
        return converView;
    }
}
