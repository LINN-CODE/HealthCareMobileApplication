package com.sp.mini_project_012;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class user_register_activity extends AppCompatActivity {
    EditText userFirstName;
    EditText userLastName;
    EditText userEmail;
    EditText userPhoneNo;
    EditText userFin;
    EditText userPassword;
    EditText userConfirmPassword;
    Button userRegiser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton back;
    FirebaseAuth mAuth;
    private DatabaseReference mDataBase,AllDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);


        userFirstName = findViewById(R.id.user_register_firstName);
        userLastName = findViewById(R.id.user_register_lastName);
        userEmail = findViewById(R.id.user_register_email);
        userPhoneNo = findViewById(R.id.user_register_phoneNumber);
        userFin = findViewById(R.id.user_fin);
        userPassword = findViewById(R.id.user_register_password);
        userConfirmPassword = findViewById(R.id.user_register_confirmPassword);
        userRegiser = findViewById(R.id.user_registerButton);
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
        AllDataBase = FirebaseDatabase.getInstance().getReference().child("ALL");
        userRegiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName,lastName,email,phoneNo,finNo,password,confirmPassword;
                firstName = String.valueOf(userFirstName.getText());
                lastName = String.valueOf(userLastName.getText());
                email = String.valueOf(userEmail.getText());
                phoneNo = String.valueOf(userPhoneNo.getText());
                finNo = String.valueOf(userFin.getText());
                password = String.valueOf(userPassword.getText());
                confirmPassword = String.valueOf(userConfirmPassword.getText());
                if(TextUtils.isEmpty(firstName)){
                    Toast.makeText(user_register_activity.this, "Please enter the first name.", Toast.LENGTH_SHORT).show();
                    userFirstName.requestFocus();
                }else if(TextUtils.isEmpty(lastName)){
                    Toast.makeText(user_register_activity.this, "Please enter the last name.", Toast.LENGTH_SHORT).show();
                    userLastName.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(user_register_activity.this, "Please enter the valid email.", Toast.LENGTH_SHORT).show();
                    userEmail.requestFocus();
                }else if(!Patterns.PHONE.matcher(phoneNo).matches()){
                    Toast.makeText(user_register_activity.this, "Please enter the valid phone number.", Toast.LENGTH_SHORT).show();
                    userPhoneNo.requestFocus();
                }else if(TextUtils.isEmpty(finNo)){
                    Toast.makeText(user_register_activity.this, "Please enter the FIN number.", Toast.LENGTH_SHORT).show();
                    userFin.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(user_register_activity.this, "Plase enter the  Password", Toast.LENGTH_SHORT).show();
                    userPassword.requestFocus();
                } else if(TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(user_register_activity.this, "Please enter the cofirm password.", Toast.LENGTH_SHORT).show();
                    userConfirmPassword.requestFocus();
                } else if(password.length() < 5) {
                    Toast.makeText(user_register_activity.this, "Your Password should be at least 6 digits.", Toast.LENGTH_SHORT).show();
                    userPassword.requestFocus();
                }

                else{
                    registerUser(firstName,lastName,email,phoneNo,finNo,password,"Users");
                }
            }
        });
    }


    private void registerUser(String first_name, String last_name, String email, String phone_no,String fin,String password,String userType){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String FirstName = first_name;
                        String LastName = last_name;
                        String Email = email;
                        String PhoneNumber = phone_no;
                        String FinNumber = fin;
                        String userType = "Users";
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(user_register_activity.this, "Login Successful",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();
                                users user = new users(FirstName,LastName,Email,PhoneNumber,FinNumber,userType,uid);
                                mDataBase.child(uid).setValue(user)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                firebaseUser.sendEmailVerification();
                                                Toast.makeText(user_register_activity.this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(user_register_activity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                AllDataBase.child(uid).setValue(user);
                            }
                           /* users user = new users(FirstName,LastName,Email,PhoneNumber,FinNumber,userType);
                            String uid = firebaseUser.getUid();
                            String E_mail = firebaseUser.getEmail();
                            mDataBase.push().setValue(user);
                            AllDataBase.push().setValue(user);
                            firebaseUser.sendEmailVerification();*/
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(user_register_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}