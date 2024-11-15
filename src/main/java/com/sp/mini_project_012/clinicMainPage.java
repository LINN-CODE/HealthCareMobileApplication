package com.sp.mini_project_012;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class clinicMainPage extends AppCompatActivity {
    String clinicId = "";
    String doctorId = "";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView greetingClincNameTextView;

    // For Doctor List View
    private DatabaseReference doctorDatabase;
    private ArrayList<staff_zero> doctorList;
    private doctor_custom_adapter doctorAdapter;
    private ListView doctorListView;
    private TextView selectedDoctorTextView;
    Button to_clinicMainPage_zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_mainpage);

        mAuth = FirebaseAuth.getInstance();

        selectedDoctorTextView = findViewById(R.id.clinic_mainpage_selecteddoctor);
        // For Clinic Name
        mDatabase = FirebaseDatabase.getInstance().getReference();
        greetingClincNameTextView = findViewById(R.id.clinic_mainpage_clinicName);
        clinicId = getIntent().getStringExtra("clinicId");
        doctorListView = findViewById(R.id.clinic_mainpage_doctor_listView);
        to_clinicMainPage_zero = findViewById(R.id.clinic_mainpage_assign_doctor_button);
        to_clinicMainPage_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), clinicMainPage_zero.class);
                intent.putExtra("clinicID", clinicId);
                intent.putExtra("doctorID", doctorId);
                startActivity(intent);
            }
        });

        // For doctors list view
        doctorListView = findViewById(R.id.clinic_mainpage_doctor_listView);
        doctorList = new ArrayList<>();
        doctorAdapter = new doctor_custom_adapter(this, doctorList);
        doctorListView.setAdapter(doctorAdapter);
        doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                staff_zero selectedDoctor = doctorList.get(position);
                doctorId = selectedDoctor.getStaffId();
                selectedDoctorTextView.setText("Selected doctor: " + selectedDoctor.getFirstName());
            }
        });
        retrieveDoctorInfo();
        retrieveUserInfo(clinicId);

        // Set up Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.clinicPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_doctors_clinic);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_doctors_clinic) {
                // Already on this page
                return true;
            } else if (itemId == R.id.bottom_appointments_clinic) {
                intent = new Intent(getApplicationContext(), clinicAppointmentsPage.class);
            }  else if (itemId == R.id.bottom_profile_clinic) {
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

    @Override
    public void onBackPressed() {
        // Do nothing to disable back button
    }

    private void retrieveDoctorInfo() {
        DatabaseReference for_doctor_list_view = doctorDatabase;

        for_doctor_list_view.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctorList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    staff_zero Doctor = dataSnapshot.getValue(staff_zero.class);
                    if (Doctor.getSelectedClinicId().equals(clinicId)) {
                        doctorList.add(Doctor);
                    }
                }
                doctorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveUserInfo(String clinicId) {
        mDatabase.child("Clinics").child(clinicId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    clinics clinic = snapshot.getValue(clinics.class);
                    if (clinic != null) {
                        String greeting = "Hello, " + clinic.getName() + "!";
                        greetingClincNameTextView.setText(greeting);
                    } else {
                        Log.d(TAG, "No user data found for user this userID");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
