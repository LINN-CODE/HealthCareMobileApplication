package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class user_log_in_page extends AppCompatActivity {
    EditText user_email, user_password;
    TextView user_log_in_register;
    Button LogInButton;
    FirebaseAuth mAuth;
    ImageButton back;
    private DatabaseReference mDatabase;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in_page);
        user_email = findViewById(R.id.user_log_in_page_email);
        user_password = findViewById(R.id.user_log_in_page_password);
        LogInButton = findViewById(R.id.user_log_in_page_Login_button);
        user_log_in_register = findViewById(R.id.user_log_in_page_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        back = findViewById(R.id.staff_log_in_page_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(user_log_in_page.this, SplashScreen2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        user_log_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(user_log_in_page.this, user_register_activity.class);
                startActivity(intent);
                finish(); // Close the login page after navigating to register page
            }
        });

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString();
                String password = user_password.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    userId = firebaseUser.getUid();
                                    checkUserInDatabase(firebaseUser.getUid());
                                } else {
                                    Toast.makeText(user_log_in_page.this, "Authentication failed._1", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(user_log_in_page.this, "Authentication failed._2", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(user_log_in_page.this, "User not found in database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(user_log_in_page.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainPage() {
        Intent intent = new Intent(user_log_in_page.this, userMainPage.class);
        intent.putExtra("userId", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true); // Minimize the app when back is pressed
    }
}
