package com.sp.mini_project_012;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_messaeges_page extends AppCompatActivity {

    private DatabaseReference bookingDatabase, doctorDatabase;
    private ListView messageListView;
    private DoctorListAdapter doctorListAdapter;
    private List<staff_zero> doctorList;
    private String userId;
    private TextView testing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messaeges_page);

        messageListView = findViewById(R.id.user_messagePage_listView);
        doctorList = new ArrayList<>();
        doctorListAdapter = new DoctorListAdapter(this, doctorList);
        messageListView.setAdapter(doctorListAdapter);
        testing = findViewById(R.id.messaegs_page_testing);
        userId = getIntent().getStringExtra("userId");

        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings");
        doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors");

        loadDoctorNames();

        messageListView.setOnItemClickListener((parent, view, position, id) -> {
            staff_zero doctor = doctorList.get(position);
            Intent intent = new Intent(user_messaeges_page.this, clinic_chat.class);
            intent.putExtra("doctorName", doctor.getFirstName());
            intent.putExtra("doctorId", doctor.getStaffId());
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.userPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_message_user);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_home_user) {
                intent = new Intent(getApplicationContext(), userMainPage.class);
            } else if (itemId == R.id.bottom_profile_user) {
                intent = new Intent(getApplicationContext(), userMainPage_Profile.class);
            } else if (itemId == R.id.bottom_message_user) {
                // Already on this page
                return true;
            } else if (itemId == R.id.bottom_fitness_user) {
                intent = new Intent(getApplicationContext(), userMainPage_Fitness.class);
            }
            if (intent != null) {
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadDoctorNames() {
        bookingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                doctorList.clear();
                for (DataSnapshot clinicSnapshot : bookingSnapshot.getChildren()) {
                    for (DataSnapshot bookingDetailSnapshot : clinicSnapshot.getChildren()) {
                        Booking booking = bookingDetailSnapshot.getValue(Booking.class);

                        if (booking != null && booking.getUserId().equals(userId) && !booking.isCompleted()) {
                            String doctorId = booking.getDoctorId();
                            doctorDatabase.child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot doctorSnapshot) {
                                    if (doctorSnapshot.exists()) {
                                        staff_zero doctor = doctorSnapshot.getValue(staff_zero.class);
                                        if (doctor != null) {
                                            doctorList.add(doctor);
                                            doctorListAdapter.notifyDataSetChanged();
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
