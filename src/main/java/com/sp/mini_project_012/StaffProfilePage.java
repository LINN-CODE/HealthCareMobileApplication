package com.sp.mini_project_012;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class StaffProfilePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference doctorDatabase;
    private TextView doctorNameTextView, doctorEmailTextView, doctorPhoneTextView;
    private Button updateProfileButton, logoutButton; // Add logoutButton
    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile_page);

        mAuth = FirebaseAuth.getInstance();
        doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorNameTextView = findViewById(R.id.doctor_profile_name);
        doctorEmailTextView = findViewById(R.id.doctor_profile_email);
        doctorPhoneTextView = findViewById(R.id.doctor_profile_phone);
        updateProfileButton = findViewById(R.id.buttonUpdateProfile);
        logoutButton = findViewById(R.id.buttonLogout); // Initialize logoutButton

        doctorId = getIntent().getStringExtra("doctorId");

        retrieveDoctorInfo();

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateActivity();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() { // Add logout button click listener
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(StaffProfilePage.this, staff_log_in_page.class);
                startActivity(intent);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.doctorPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile_doctor);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_appointments_doctor) {
                intent = new Intent(getApplicationContext(), staffMainPage.class);
            } else if (itemId == R.id.bottom_message_doctor) {
                intent = new Intent(getApplicationContext(), StaffMessagePage.class);
            } else if (itemId == R.id.bottom_profile_doctor) {
                // Already on this page
                return true;
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

    private void startUpdateActivity() {
        Intent intent = new Intent(StaffProfilePage.this, UpdateDoctorInfoActivity.class);
        intent.putExtra("doctorId", doctorId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Do nothing to disable back button
    }

    private void retrieveDoctorInfo() {
        doctorDatabase.child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    staff_zero doctor = snapshot.getValue(staff_zero.class);
                    if (doctor != null) {
                        doctorNameTextView.setText("Name: " + doctor.getFirstName());
                        doctorEmailTextView.setText("Email: " + doctor.getEmail());
                        doctorPhoneTextView.setText("Phone: " + doctor.getPhoneNumber());
                    } else {
                        Toast.makeText(StaffProfilePage.this, "Failed to load doctor data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StaffProfilePage.this, "No doctor data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StaffProfilePage.this, "Failed to load doctor data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
