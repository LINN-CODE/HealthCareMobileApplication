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

public class AppointmentAdapter extends ArrayAdapter<Booking> {

    private Context context;
    private ArrayList<Booking> appointments;

    public AppointmentAdapter(Context context, ArrayList<Booking> appointments) {
        super(context, 0, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.appointment_list_item, parent, false);
        }

        Booking appointment = getItem(position);

        TextView appointmentDateTextView = convertView.findViewById(R.id.appointment_date_text_view);
        TextView appointmentTimeTextView = convertView.findViewById(R.id.appointment_time_text_view);

        appointmentDateTextView.setText(appointment.getDate());
        appointmentTimeTextView.setText(appointment.getShift().equals("Morning Shift") ? "9 AM" : "3 PM");

        return convertView;
    }
}
