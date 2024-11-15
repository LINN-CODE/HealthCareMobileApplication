package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class userMainPage_Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase, bookingDatabase;
    private TextView userNameTextView, userEmailTextView, userPhoneTextView, reservationDetailsTextView, qrcode;
    private Button logoutButton, scanQRButton;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page_profile);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings");

        userNameTextView = findViewById(R.id.profile_user_name);
        userEmailTextView = findViewById(R.id.profile_user_email);
        userPhoneTextView = findViewById(R.id.profile_user_phone);
        qrcode = findViewById(R.id.usermain_profile_text);
        reservationDetailsTextView = findViewById(R.id.profile_reservation_details);
        logoutButton = findViewById(R.id.profile_logout_button);
        scanQRButton = findViewById(R.id.scan_qr_button);

        userId = getIntent().getStringExtra("userId");

        retrieveUserInfo();
        retrieveUserReservation();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(userMainPage_Profile.this, user_log_in_page.class);
                startActivity(intent);
                finish();
            }
        });

        userNameTextView.setOnClickListener(v -> startUpdateActivity("firstName"));
        userEmailTextView.setOnClickListener(v -> startUpdateActivity("email"));
        userPhoneTextView.setOnClickListener(v -> startUpdateActivity("phoneNumber"));

        scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(userMainPage_Profile.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan Clinic QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.userPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile_user);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home_user) {
                Intent intent = new Intent(getApplicationContext(), userMainPage.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.bottom_message_user) {
                Intent intent = new Intent(getApplicationContext(), user_messaeges_page.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.bottom_fitness_user) {
                Intent intent = new Intent(getApplicationContext(), userMainPage_Fitness.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
                return true;
            } else {
                return true;
            }
        });
    }

    private void startUpdateActivity(String updateField) {
        Intent intent = new Intent(userMainPage_Profile.this, UpdateUserInfoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("updateField", updateField);
        startActivity(intent);
    }

    private void retrieveUserInfo() {
        userDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    users user = snapshot.getValue(users.class);
                    if (user != null) {
                        userNameTextView.setText("Name: " + user.getFirstName() + " " + user.getLastName());
                        userEmailTextView.setText("Email: " + user.getEmail());
                        userPhoneTextView.setText("Phone: " + user.getPhoneNumber());
                    }
                } else {
                    Toast.makeText(userMainPage_Profile.this, "User data not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userMainPage_Profile.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserReservation() {
        bookingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot datashot : snapshot.getChildren()) {
                            Booking booking = datashot.getValue(Booking.class);
                            if (booking != null && booking.getUserId().equals(userId) && !booking.isCompleted()) {
                                fetchClinicName(booking);
                                return;
                            }
                        }
                    }
                } else {
                    reservationDetailsTextView.setText("No reservation found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userMainPage_Profile.this, "Failed to load reservation.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchClinicName(Booking booking) {
        DatabaseReference clinicRef = FirebaseDatabase.getInstance().getReference("Clinics").child(booking.getClinicId());
        clinicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String clinicName = dataSnapshot.child("name").getValue(String.class);
                    String reservationDetails = "Clinic: " + clinicName + "\nDate: " + booking.getDate() + "\nTime: " + booking.getShift();
                    reservationDetailsTextView.setText(reservationDetails);
                } else {
                    reservationDetailsTextView.setText("No reservation found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userMainPage_Profile.this, "Failed to fetch clinic name.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                validateQRCode(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void validateQRCode(String qrCode) {
        bookingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot datashot : snapshot.getChildren()) {
                        Booking booking = datashot.getValue(Booking.class);
                        if (booking != null && booking.getUserId().equals(userId) && !booking.isCompleted()) {
                            String tite_sitt = booking.getClinicId().toString().trim();
                            String qqcode = qrCode.toString().trim();
                            if (tite_sitt.equals(qqcode)) {
                                completeReservation(booking);
                                return;
                            } else {
                                Toast.makeText(userMainPage_Profile.this, "Invalid QR Code for this clinic.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userMainPage_Profile.this, "Failed to validate QR code.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completeReservation(Booking booking) {
        // Set the reservation as completed
        bookingDatabase.child(booking.getClinicId()).child(booking.getBookingId()).child("completed").setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(userMainPage_Profile.this, "Reservation completed.", Toast.LENGTH_SHORT).show();
                reservationDetailsTextView.setText("No active reservation.");
            } else {
                Toast.makeText(userMainPage_Profile.this, "Failed to complete reservation.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
