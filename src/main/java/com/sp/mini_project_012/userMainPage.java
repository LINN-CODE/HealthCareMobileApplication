package com.sp.mini_project_012;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class userMainPage extends AppCompatActivity {
    String userId = "";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView greetingUserNameTextView;

    // For clinics list view
    private DatabaseReference clinicDatabase;
    private ArrayList<clinics> clinicList;
    private user_custom_adapter clinicAdapter;
    private ListView clinicListView;

    private Button notificationButton;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_user_mainpage);

        mAuth = FirebaseAuth.getInstance();

        // Retrieve userId from the intent
        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        } else {
            userId = mAuth.getCurrentUser().getUid(); // Fallback to the currently logged in user's UID
        }

        // For username
        mDatabase = FirebaseDatabase.getInstance().getReference();
        greetingUserNameTextView = findViewById(R.id.user_main_page_userName);

        BottomNavigationView bottomNavigationView = findViewById(R.id.userPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home_user);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_message_user) {
                intent = new Intent(getApplicationContext(), user_messaeges_page.class);
            } else if (itemId == R.id.bottom_profile_user) {
                intent = new Intent(getApplicationContext(), userMainPage_Profile.class);
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

        // For clinics list view
        clinicListView = findViewById(R.id.user_main_page_Clinic_listview);
        clinicList = new ArrayList<>();
        clinicAdapter = new user_custom_adapter(this, clinicList);
        clinicListView.setAdapter(clinicAdapter);
        clinicDatabase = FirebaseDatabase.getInstance().getReference("Clinics");

        retrieveUserInfo(userId);
        retrieveClinicsData();



        clinicListView.setOnItemClickListener((parent, view, position, id) -> {
            clinics selectedClinic = clinicList.get(position);
            Intent intent = new Intent(userMainPage.this, userMainPage_booking.class);
            intent.putExtra("clinicID", selectedClinic.getClinicID());
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        notificationButton = findViewById(R.id.user_mainPage_noti_imageButton);
        notificationButton.setOnClickListener(v -> fetchAndShowNotifications());
    }

    private void retrieveClinicsData() {
        DatabaseReference for_clinic_list_view = clinicDatabase;

        for_clinic_list_view.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clinicList.clear();

                for (DataSnapshot snapshot_2 : dataSnapshot.getChildren()) {
                    clinics clinic = snapshot_2.getValue(clinics.class);
                    clinicList.add(clinic);
                }
                clinicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveUserInfo(String userID) {
        if (userID != null) {
            mDatabase.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        users User = snapshot.getValue(users.class);
                        if (User != null) {
                            String greeting = "Hello, " + User.getFirstName() + "!";
                            greetingUserNameTextView.setText(greeting);
                        }
                    } else {
                        Log.d(TAG, "No user data found for user this userID");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Log.e(TAG, "User ID is null");
        }
    }

    private void fetchAndShowNotifications() {
        DatabaseReference notificationsRef = mDatabase.child("Notifications").child(userId);

        notificationsRef.orderByKey().limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<AppNotification> notificationsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AppNotification notification = snapshot.getValue(AppNotification.class);
                    if (notification != null) {
                        notificationsList.add(notification);
                    }
                }
                showNotificationsDialog(notificationsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to fetch notifications", databaseError.toException());
            }
        });
    }

    private void showNotificationsDialog(ArrayList<AppNotification> notificationsList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recent Notifications");

        StringBuilder message = new StringBuilder();
        for (AppNotification notification : notificationsList) {
            message.append(notification.getDateTime()).append(": ").append(notification.getMessage()).append("\n\n");
        }

        builder.setMessage(message.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
