package com.sp.mini_project_012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {

    private Context context;
    private ArrayList<Booking> bookings;

    public BookingAdapter(Context context, ArrayList<Booking> bookings) {
        super(context, 0, bookings);
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.booking_row, parent, false);
        }

        Booking booking = getItem(position);

        TextView bookingDateTextView = convertView.findViewById(R.id.staff_booking_date);
        TextView bookingTimeTextView = convertView.findViewById(R.id.staff_booking_time);

        bookingDateTextView.setText(booking.getDate());
        bookingTimeTextView.setText(booking.getShift().equals("Morning Shift") ? "9 AM" : "3 PM");

        return convertView;
    }
}
