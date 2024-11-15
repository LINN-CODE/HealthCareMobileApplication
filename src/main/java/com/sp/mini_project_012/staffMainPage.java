package com.sp.mini_project_012;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class staffMainPage extends AppCompatActivity {

    private DatabaseReference doctorDatabase, bookingDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<Booking> appointmentList;
    private BookingAdapter bookingAdapter;
    private ListView appointmentListView;
    private TextView patientDetailsTextView;
    private String doctorId,clinicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_mainpage);

        mAuth = FirebaseAuth.getInstance();
        doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors");
        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings");

        doctorId = getIntent().getStringExtra("doctorId");

        appointmentListView = findViewById(R.id.appointments_list_view);
        patientDetailsTextView = findViewById(R.id.patient_details_text_view);

        appointmentList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(this, appointmentList);
        appointmentListView.setAdapter(bookingAdapter);
        getRelatedClinicId(doctorId);
        appointmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking selectedBooking = appointmentList.get(position);
                showPatientDetails(selectedBooking.getUserId());
            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.doctorPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_appointments_doctor);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_profile_doctor) {
                intent = new Intent(getApplicationContext(), StaffProfilePage.class);
            } else if (itemId == R.id.bottom_appointments_doctor) {
                // Already on this page
                return true;
            } else if (itemId == R.id.bottom_message_doctor) {
                intent = new Intent(getApplicationContext(), StaffMessagePage.class);
            }
            if (intent != null) {
                intent.putExtra("doctorId", doctorId);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }


    private void getRelatedClinicId(String doctorId) {
        DatabaseReference doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorDatabase.child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    clinicId = dataSnapshot.child("selectedClinicId").getValue(String.class);
                    if (clinicId != null) {

                        // Use the clinicId as needed
                        bookingDatabase.child(clinicId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                appointmentList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Booking booking = dataSnapshot.getValue(Booking.class);
                                    if (booking != null && !booking.isCompleted() && booking.getDoctorId().equals(doctorId)) {
                                    appointmentList.add(booking);
                                    }
                                }
                                bookingAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(staffMainPage.this, "Failed to load appointments", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e("ClinicID", "No clinic ID found for this doctor.");
                    }
                } else {
                    Log.e("ClinicID", "Doctor data not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ClinicID", "Database error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do nothing, so the back button is disabled
    }

    private void showPatientDetails(String userId) {
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users user = snapshot.getValue(users.class);
                if (user != null) {
                    String details = "Name: " + user.getFirstName() + " " + user.getLastName() + "\n"
                            + "Email: " + user.getEmail() + "\n"
                            + "Phone: " + user.getPhoneNumber();
                    patientDetailsTextView.setText(details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(staffMainPage.this, "Failed to load patient details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
