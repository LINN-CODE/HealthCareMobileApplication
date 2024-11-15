package com.sp.mini_project_012;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class staff_log_in_page extends AppCompatActivity {
    EditText staff_email, staff_password;
    TextView staff_log_in_register;
    Button LogInButton;
    FirebaseAuth mAuth;
    ImageButton back;
    private DatabaseReference mDatabase;
    Intent intent;
    String doctorId; // Added this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_log_in_page);
        staff_email = findViewById(R.id.staff_log_in_page_email);
        staff_password = findViewById(R.id.staff_log_in_page_password);
        LogInButton = findViewById(R.id.staff_log_in_page_Login_button);
        staff_log_in_register = findViewById(R.id.staff_log_in_page_register);
        back = findViewById(R.id.staff_log_in_page_back);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Staffs");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staff_log_in_page.this, SplashScreen2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        staff_log_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staff_log_in_page.this, staff_register_activity_zero.class);
                startActivity(intent);
            }
        });

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = staff_email.getText().toString();
                String password = staff_password.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    doctorId = firebaseUser.getUid(); // Set doctorId here
                                    checkUserInDatabase(doctorId);
                                } else {
                                    Toast.makeText(staff_log_in_page.this, "Authentication failed._1", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(staff_log_in_page.this, "Authentication failed._2", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void checkUserInDatabase(String uid) {
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    navigateToMainPage(uid); // Pass the doctorId here
                } else {
                    Toast.makeText(staff_log_in_page.this, "User not found in database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(staff_log_in_page.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainPage(String doctorId) {
        Intent intent = new Intent(staff_log_in_page.this, staffMainPage.class);
        intent.putExtra("doctorId", doctorId); // Pass the doctorId to the intent
        startActivity(intent);
        finish();
    }
}
