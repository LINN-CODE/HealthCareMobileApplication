package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class clinicAppointmentsPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference appointmentsDatabase, usersDatabase;
    private ListView appointmentsListView;
    private ArrayList<Booking> appointmentList;
    private AppointmentAdapter appointmentAdapter;
    private String clinicId;
    private TextView patientDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_appointments_page);

        mAuth = FirebaseAuth.getInstance();
        appointmentsDatabase = FirebaseDatabase.getInstance().getReference("Bookings");
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        appointmentsListView = findViewById(R.id.appointments_list_view);
        patientDetailsTextView = findViewById(R.id.patient_details_text_view);
        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(this, appointmentList);
        appointmentsListView.setAdapter(appointmentAdapter);

        clinicId = getIntent().getStringExtra("clinicId");

        retrieveClinicAppointments();

        appointmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking selectedAppointment = appointmentList.get(position);
                retrievePatientDetails(selectedAppointment.getUserId());
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.clinicPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_appointments_clinic);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_doctors_clinic) {
                intent = new Intent(getApplicationContext(), clinicMainPage.class);
            } else if (itemId == R.id.bottom_appointments_clinic) {
                // Already on this page
                return true;
            }else if (itemId == R.id.bottom_profile_clinic) {
                intent = new Intent(getApplicationContext(), clinicProfilePage.class);
            }
            if (intent != null) {
                intent.putExtra("clinicId", clinicId);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }

    private void retrieveClinicAppointments() {
        appointmentsDatabase.child(clinicId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Booking booking = dataSnapshot.getValue(Booking.class);
                            if(!booking.isCompleted()){
                                appointmentList.add(booking);
                            }

                    }
                    appointmentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(clinicAppointmentsPage.this, "No appointments found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(clinicAppointmentsPage.this, "Failed to load appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrievePatientDetails(String userId) {
        usersDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    users patient = snapshot.getValue(users.class);
                    if (patient != null) {
                        String patientDetails = "Name: " + patient.getFirstName() + " " + patient.getLastName() +
                                "\nEmail: " + patient.getEmail() +
                                "\nPhone: " + patient.getPhoneNumber();
                        patientDetailsTextView.setText(patientDetails);
                    } else {
                        Toast.makeText(clinicAppointmentsPage.this, "Failed to load patient details.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(clinicAppointmentsPage.this, "No patient data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(clinicAppointmentsPage.this, "Failed to load patient details.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
