package com.sp.mini_project_012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class user_custom_adapter extends ArrayAdapter<clinics> implements Filterable {

    private Context context;
    private List<clinics> Clinic;
    private List<clinics> ClinicFull;

    public user_custom_adapter(Context context, ArrayList<clinics> Clinic) {
        super(context, 0, Clinic);
        this.context = context;
        this.Clinic = new ArrayList<>(Clinic);
        this.ClinicFull = new ArrayList<>(Clinic);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_register_clinic_row, parent, false);
        }

        clinics Clinic = getItem(position);

        TextView clinicNameTextView = convertView.findViewById(R.id.user_register_clinic_name);
        TextView clinicAddressTextView = convertView.findViewById(R.id.user_register_clinic_address);
        TextView clinicPhoneNumberTextView = convertView.findViewById(R.id.user_register_clinic_phone_number);
        TextView clinicEmailTextView = convertView.findViewById(R.id.user_register_clinic_email);

        clinicNameTextView.setText(Clinic.getName());
        clinicAddressTextView.setText(Clinic.getAddress());
        clinicPhoneNumberTextView.setText(Clinic.getPhoneNo());
        clinicEmailTextView.setText(Clinic.getEmail());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return clinicFilter;
    }

    private Filter clinicFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<clinics> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ClinicFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (clinics item : ClinicFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Clinic.clear();
            Clinic.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
