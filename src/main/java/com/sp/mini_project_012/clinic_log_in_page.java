package com.sp.mini_project_012;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class clinic_log_in_page extends AppCompatActivity {
    EditText clinic_email, clinic_password;
    TextView clinic_log_in_register;
    ImageButton back;
    Button LogInButton;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Intent intent;
    String userId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_log_in_page);
        clinic_email = findViewById(R.id.clinic_log_in_page_email);
        clinic_password = findViewById(R.id.clinic_log_in_page_password);
        LogInButton = findViewById(R.id.clinic_log_in_page_Login_button);
        clinic_log_in_register = findViewById(R.id.clinic_log_in_page_register);
        back = findViewById(R.id.clinic_log_in_page_back);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Clinics");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clinic_log_in_page.this, SplashScreen2Activity.class);
                startActivity(intent);
                finish();
            }
        });
        clinic_log_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clinic_log_in_page.this, clinic_register_activity.class);
                startActivity(intent);
            }
        });
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = clinic_email.getText().toString();
                String password = clinic_password.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    checkUserInDatabase(firebaseUser.getUid());
                                    userId = firebaseUser.getUid();
                                }
                                else {
                                    Toast.makeText(clinic_log_in_page.this, "Authentication failed._1", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(clinic_log_in_page.this, "Authentication failed._2", Toast.LENGTH_SHORT).show();
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
                    navigateToMainPage();
                } else {
                    Toast.makeText(clinic_log_in_page.this, "User not found in database.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(clinic_log_in_page.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainPage() {
        Intent intent = new Intent(clinic_log_in_page.this, clinicMainPage.class);
        intent.putExtra("clinicId",userId);
        startActivity(intent);
        finish();
    }
}