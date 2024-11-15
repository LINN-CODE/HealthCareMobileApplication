package com.sp.mini_project_012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DoctorAssignmentAdapter extends BaseAdapter {

    private Context context;
    private List<DoctorAssignment> assignmentList;

    public DoctorAssignmentAdapter(Context context, List<DoctorAssignment> assignmentList) {
        this.context = context;
        this.assignmentList = assignmentList;
    }

    @Override
    public int getCount() {
        return assignmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return assignmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.doctor_assignment_item, parent, false);
        }

        DoctorAssignment assignment = (DoctorAssignment) getItem(position);


        TextView dateTextView = convertView.findViewById(R.id.date_text_view);
        TextView shiftTextView = convertView.findViewById(R.id.shift_text_view);


        dateTextView.setText(assignment.getDate());
        shiftTextView.setText(assignment.getShift());

        return convertView;
    }
}
