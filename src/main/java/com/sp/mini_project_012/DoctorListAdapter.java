package com.sp.mini_project_012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;
import com.sp.mini_project_012.staff_zero;

import java.util.ArrayList;
import java.util.List;

public class DoctorListAdapter extends ArrayAdapter<staff_zero> {
    private Context context;
    private List<staff_zero> doctors;

    public DoctorListAdapter(Context context, List<staff_zero> doctors) {
        super(context, 0, doctors);
        this.context = context;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        staff_zero doctor = getItem(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(doctor.getFirstName());

        return convertView;
    }
}
