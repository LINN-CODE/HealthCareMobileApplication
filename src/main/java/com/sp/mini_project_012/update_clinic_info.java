package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class update_clinic_info extends AppCompatActivity {

    private EditText editTextClinicName, editTextClinicEmail, editTextClinicPhone, editTextClinicAddress;
    private Button buttonUpdateClinicInfo;
    private String clinicId;

    private DatabaseReference clinicDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_clinic_info);

        editTextClinicName = findViewById(R.id.editTextClinicName);
        editTextClinicEmail = findViewById(R.id.editTextClinicEmail);
        editTextClinicPhone = findViewById(R.id.editTextClinicPhone);
        editTextClinicAddress = findViewById(R.id.editTextClinicAddress);
        buttonUpdateClinicInfo = findViewById(R.id.buttonUpdateClinicInfo);

        mAuth = FirebaseAuth.getInstance();
        clinicDatabase = FirebaseDatabase.getInstance().getReference("Clinics");

        clinicId = getIntent().getStringExtra("clinicId");

        buttonUpdateClinicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextClinicName.getText().toString().trim();
                String newEmail = editTextClinicEmail.getText().toString().trim();
                String newPhone = editTextClinicPhone.getText().toString().trim();
                String newAddress = editTextClinicAddress.getText().toString().trim();

                if (!newName.isEmpty() && !newEmail.isEmpty() && !newPhone.isEmpty() && !newAddress.isEmpty()) {
                    updateClinicInfo(newName, newEmail, newPhone, newAddress);
                } else {
                    Toast.makeText(update_clinic_info.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateClinicInfo(String newName, String newEmail, String newPhone, String newAddress) {
        clinicDatabase.child(clinicId).child("name").setValue(newName);
        clinicDatabase.child(clinicId).child("email").setValue(newEmail);
        clinicDatabase.child(clinicId).child("phone").setValue(newPhone);
        clinicDatabase.child(clinicId).child("address").setValue(newAddress).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(update_clinic_info.this, "Information updated successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(update_clinic_info.this, "Failed to update information.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
