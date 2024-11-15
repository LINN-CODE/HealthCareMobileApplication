package com.sp.mini_project_012;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffMessagePage extends AppCompatActivity {

    private DatabaseReference bookingDatabase, userDatabase;
    private ListView messageListView;
    private PatientListAdapter patientListAdapter;
    private List<String> patientNames;
    private Map<String, String> patientIdMap; // Map to store patient names and their IDs
    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_message_page);

        messageListView = findViewById(R.id.staff_messagePage_lsitView);
        patientNames = new ArrayList<>();
        patientIdMap = new HashMap<>();
        patientListAdapter = new PatientListAdapter(this, patientNames);
        messageListView.setAdapter(patientListAdapter);

        doctorId = getIntent().getStringExtra("doctorId");

        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings");
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        loadPatientNames();

        messageListView.setOnItemClickListener((parent, view, position, id) -> {
            String patientName = patientNames.get(position);
            String patientId = patientIdMap.get(patientName); // Retrieve patient ID from the map
            Intent intent = new Intent(StaffMessagePage.this, clinic_chat.class);
            intent.putExtra("doctorName", patientName);
            intent.putExtra("doctorId", patientId); // Pass the patient ID to the intent
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.doctorPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_message_doctor);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_appointments_doctor) {
                intent = new Intent(getApplicationContext(), staffMainPage.class);
            } else if (itemId == R.id.bottom_message_doctor) {
                // Already on this page
                return true;
            } else if (itemId == R.id.bottom_profile_doctor) {
                intent = new Intent(getApplicationContext(), StaffProfilePage.class);
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

    private void loadPatientNames() {
        bookingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                patientNames.clear();
                patientIdMap.clear();
                for (DataSnapshot clinicSnapshot : bookingSnapshot.getChildren()) {
                    for (DataSnapshot bookingDetailSnapshot : clinicSnapshot.getChildren()) {
                        Booking booking = bookingDetailSnapshot.getValue(Booking.class);
                        if (booking != null && booking.getDoctorId().equals(doctorId) && !booking.isCompleted()) {
                            String userId = booking.getUserId();
                            userDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    if (userSnapshot.exists()) {
                                        users user = userSnapshot.getValue(users.class);
                                        if (user != null) {
                                            patientNames.add(user.getFirstName());
                                            patientIdMap.put(user.getFirstName(), userId); // Store patient ID in the map
                                            patientListAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
