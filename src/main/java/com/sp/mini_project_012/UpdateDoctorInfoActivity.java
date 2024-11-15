package com.sp.mini_project_012;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDoctorInfoActivity extends AppCompatActivity {

    private EditText editTextDoctorName, editTextDoctorEmail, editTextDoctorPhone;
    private Button buttonUpdateDoctorInfo;
    private String doctorId;

    private DatabaseReference doctorDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor_info);

        editTextDoctorName = findViewById(R.id.editTextDoctorName);
        editTextDoctorEmail = findViewById(R.id.editTextDoctorEmail);
        editTextDoctorPhone = findViewById(R.id.editTextDoctorPhone);
        buttonUpdateDoctorInfo = findViewById(R.id.buttonUpdateDoctorInfo);

        mAuth = FirebaseAuth.getInstance();
        doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorId = getIntent().getStringExtra("doctorId");

        buttonUpdateDoctorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextDoctorName.getText().toString().trim();
                String newEmail = editTextDoctorEmail.getText().toString().trim();
                String newPhone = editTextDoctorPhone.getText().toString().trim();

                if (!newName.isEmpty() || !newEmail.isEmpty() || !newPhone.isEmpty()) {
                    if (!newName.isEmpty()) {
                        updateDoctorInfo("firstName", newName);
                    }
                    if (!newEmail.isEmpty()) {
                        updateDoctorInfo("email", newEmail);
                    }
                    if (!newPhone.isEmpty()) {
                        updateDoctorInfo("phoneNumber", newPhone);
                    }
                } else {
                    Toast.makeText(UpdateDoctorInfoActivity.this, "Please enter at least one value.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDoctorInfo(String updateField, String newValue) {
        doctorDatabase.child(doctorId).child(updateField).setValue(newValue).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UpdateDoctorInfoActivity.this, "Information updated successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(UpdateDoctorInfoActivity.this, "Failed to update information.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
