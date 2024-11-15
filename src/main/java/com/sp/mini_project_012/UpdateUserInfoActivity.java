package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone;
    private Button buttonUpdate;
    private String userId, updateField;

    private DatabaseReference userDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        userId = getIntent().getStringExtra("userId");

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextName.getText().toString().trim();
                String newEmail = editTextEmail.getText().toString().trim();
                String newPhone = editTextPhone.getText().toString().trim();

                if (!newName.isEmpty() || !newEmail.isEmpty() || !newPhone.isEmpty()) {
                    if (!newName.isEmpty()) {
                        updateUserInfo("FirstName", newName);
                    }
                    if (!newEmail.isEmpty()) {
                        updateUserInfo("Email", newEmail);
                    }
                    if (!newPhone.isEmpty()) {
                        updateUserInfo("PhoneNumber", newPhone);
                    }
                } else {
                    Toast.makeText(UpdateUserInfoActivity.this, "Please enter at least one value.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserInfo(String updateField, String newValue) {
        userDatabase.child(userId).child(updateField).setValue(newValue).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UpdateUserInfoActivity.this, "Information updated successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(UpdateUserInfoActivity.this, "Failed to update information.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
