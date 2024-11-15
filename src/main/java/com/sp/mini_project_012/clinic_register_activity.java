package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class clinic_register_activity extends AppCompatActivity {
    private EditText clinicName,clinicMobilePhNo,clinicAddress,clinicEmail,clinicPassword,clinicConfirmPassword;
    private Button clinicRegisterButton;
    private ImageButton backButton;
    private FirebaseAuth mAtuh;
    private DatabaseReference mDatabase;
    private DatabaseReference AllDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_register);

        clinicName = findViewById(R.id.clinic_register_name);
        clinicMobilePhNo = findViewById(R.id.clinic_register_mobilePhoneNo);
        clinicAddress = findViewById(R.id.clinic_register_address);
        clinicEmail = findViewById(R.id.clinic_register_email);
        clinicPassword = findViewById(R.id.clinic_register_password);
        clinicConfirmPassword = findViewById(R.id.clinic_register_confirmPassword);
        clinicRegisterButton = findViewById(R.id.clinic_registerButton);
        backButton = findViewById(R.id.clinic_register_backButton);
        mAtuh = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Clinics");
        AllDataBase = FirebaseDatabase.getInstance().getReference().child("ALL");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clinic_register_activity.this, clinic_log_in_page.class);
                startActivity(intent);
                finish();
            }
        });

        clinicRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,mobilePhoneNumber,address,email,password,confirmPassword;
                name = String.valueOf(clinicName.getText());
                mobilePhoneNumber = String.valueOf(clinicMobilePhNo.getText());
                address = String.valueOf(clinicAddress.getText());
                email = String.valueOf(clinicEmail.getText());
                password = String.valueOf(clinicPassword.getText());
                confirmPassword = String.valueOf(clinicConfirmPassword.getText());
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(clinic_register_activity.this, "Please fill in the clinic name.", Toast.LENGTH_SHORT).show();
                    clinicName.requestFocus();
                }else if(TextUtils.isEmpty(mobilePhoneNumber)){
                    Toast.makeText(clinic_register_activity.this, "Please fill in the clinic phone number.", Toast.LENGTH_SHORT).show();
                    clinicMobilePhNo.requestFocus();
                }else if(TextUtils.isEmpty(address)) {
                    Toast.makeText(clinic_register_activity.this, "Please enter the clinic address", Toast.LENGTH_SHORT).show();
                    clinicAddress.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(clinic_register_activity.this, "Please enter the clinic email address.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(clinic_register_activity.this, "Please enter the clinic password.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(clinic_register_activity.this, "Please enter the confirm password.", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(confirmPassword)){
                    Toast.makeText(clinic_register_activity.this, "Password must be equal to confirm equal.", Toast.LENGTH_SHORT).show();
                }else{
                    String userType = "";
                    String clinicId = "";
                    registerClinic(name,mobilePhoneNumber,address,email,password,confirmPassword,userType,clinicId);
                }
            }
        });
    }

    private void registerClinic(String name, String mobilePhoneNumber, String address, String email, String password, String confirmPassword,String type,String clinicId) {
        mAtuh.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String clinic_name = name;
                String clinic_mobile_phone_np = mobilePhoneNumber;
                String clinic_address = address;
                String clinic_email = email;
                String type_ = type;
                String clinicID = clinicId;
                if(task.isSuccessful()){
                    Toast.makeText(clinic_register_activity.this, "Your clinic has been successfully registered.", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = mAtuh.getCurrentUser();
                    clinicID = firebaseUser.getUid();
                    String uid = firebaseUser.getUid();
                    type_ = "Clinics";
                    clinics clinic = new clinics(clinic_name,clinic_mobile_phone_np,clinic_address,clinic_email,type_,clinicID);
                    mDatabase.child(uid).setValue(clinic)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    firebaseUser.sendEmailVerification();
                                    Toast.makeText(clinic_register_activity.this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(clinic_register_activity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AllDataBase.child(uid).setValue(clinic);

                    /*type_ = "Clinics";
                    clinics clinic = new clinics(clinic_name,clinic_mobile_phone_np,clinic_address,clinic_email,type_);
                    mDatabase.push().setValue(clinic);
                    AllDataBase.push().setValue(clinic);
                    firebaseUser.sendEmailVerification();*/

                }else {
                    Toast.makeText(clinic_register_activity.this, "Your clinic cannot be registered.", Toast.LENGTH_SHORT).show();                }
            }
        });
    }
}