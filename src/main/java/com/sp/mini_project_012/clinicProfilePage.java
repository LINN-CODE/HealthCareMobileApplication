package com.sp.mini_project_012;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class clinicProfilePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference clinicDatabase;
    private TextView clinicNameTextView, clinicEmailTextView, clinicPhoneTextView, clinicAddressTextView;
    private ImageView qrCodeImageView;
    private Button logoutButton;
    private String clinicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_profile_page);

        mAuth = FirebaseAuth.getInstance();
        clinicDatabase = FirebaseDatabase.getInstance().getReference("Clinics");
        qrCodeImageView = findViewById(R.id.clinic_profile_qr_code);
        clinicNameTextView = findViewById(R.id.clinic_profile_name);
        clinicEmailTextView = findViewById(R.id.clinic_profile_email);
        clinicPhoneTextView = findViewById(R.id.clinic_profile_phone);
        clinicAddressTextView = findViewById(R.id.clinic_profile_address);

        logoutButton = findViewById(R.id.clinic_profile_logout_button);

        clinicId = getIntent().getStringExtra("clinicId");
        generateQrCode(clinicId);
        retrieveClinicInfo();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(clinicProfilePage.this, clinic_log_in_page.class);
                startActivity(intent);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.clinicPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile_clinic);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_doctors_clinic) {
                intent = new Intent(getApplicationContext(), clinicMainPage.class);
            } else if (itemId == R.id.bottom_appointments_clinic) {
                intent = new Intent(getApplicationContext(), clinicAppointmentsPage.class);
            } else if (itemId == R.id.bottom_profile_clinic) {
                return true; // Already on this page
            }
            if (intent != null) {
                intent.putExtra("clinicId", clinicId);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        clinicNameTextView.setOnClickListener(v -> startUpdateActivity("name"));
        clinicEmailTextView.setOnClickListener(v -> startUpdateActivity("email"));
        clinicPhoneTextView.setOnClickListener(v -> startUpdateActivity("phone"));
        clinicAddressTextView.setOnClickListener(v -> startUpdateActivity("address"));
    }

    private void startUpdateActivity(String updateField) {
        Intent intent = new Intent(clinicProfilePage.this, update_clinic_info.class);
        intent.putExtra("clinicId", clinicId);
        intent.putExtra("updateField", updateField);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Do nothing to disable back button
    }

    private void retrieveClinicInfo() {
        clinicDatabase.child(clinicId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    clinics clinic = snapshot.getValue(clinics.class);
                    if (clinic != null) {
                        clinicNameTextView.setText("Name: " + clinic.getName());
                        clinicEmailTextView.setText("Email: " + clinic.getEmail());
                        clinicPhoneTextView.setText("Phone: " + clinic.getPhoneNo());
                        clinicAddressTextView.setText("Address: " + clinic.getAddress());
                    } else {
                        Toast.makeText(clinicProfilePage.this, "Failed to load clinic data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(clinicProfilePage.this, "No clinic data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(clinicProfilePage.this, "Failed to load clinic data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateQrCode(String clinicId) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(clinicId, BarcodeFormat.QR_CODE, 400, 400);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
