package com.sp.mini_project_012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class doctor_custom_adapter extends ArrayAdapter<staff_zero> {
    private Context context;
    private ArrayList<staff_zero> Doctor;

    public doctor_custom_adapter(Context context, ArrayList<staff_zero> Doctor){
        super(context, 0,Doctor);
        this.context = context;
        this.Doctor = Doctor;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.clinic_register_doctor_row, parent, false);
        }

        staff_zero Doctor = getItem(position);

        TextView doctorFirstNameTextView = convertView.findViewById(R.id.clinic_register_doctorFirstName);
        TextView doctorLastNameTextView = convertView.findViewById(R.id.clinic_register_doctorLastName);
        TextView doctorPhoneNumberTextView = convertView.findViewById(R.id.clinic_register_doctorPhoneNumber);
        TextView doctorEmailTextView = convertView.findViewById(R.id.clinic_register_doctorEmail);

        doctorFirstNameTextView.setText(Doctor.getFirstName());
        doctorLastNameTextView.setText(Doctor.getLastName());
        doctorPhoneNumberTextView.setText(Doctor.getPhoneNumber());
        doctorEmailTextView.setText(Doctor.getEmail());

        return convertView;
    }
}
