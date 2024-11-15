package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class staff_register_activity_one extends AppCompatActivity {
    TextView selectedClinic;
    ListView listView;
    Button staffRegister;
    RadioGroup staff_choices;
    private DatabaseReference mDatabase;
    private ArrayList<String> nameList;
    private CustomAdapter adapter;
    FirebaseAuth mAuth;
    public String selectedname;
    private DatabaseReference dDataBase,staffDatabase;
    String clinicID = "";
    private String selectedClinicId;
    private Map<String,String> clinicMap;
    private DatabaseReference pDataBase,AllDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_register_one);
        mDatabase = FirebaseDatabase.getInstance().getReference("Clinics");
        nameList = new ArrayList<>();
        listView = findViewById(R.id.staff_register_clinic_listView);
        adapter = new CustomAdapter(this, nameList);
        listView.setAdapter(adapter);
        selectedClinic = findViewById(R.id.staff_register_selectedClinics_textView);
        staffRegister = findViewById(R.id.register_button);
        staff_choices = findViewById(R.id.staff_choices_radioGroup);
        mAuth = FirebaseAuth.getInstance();
        dDataBase = FirebaseDatabase.getInstance().getReference().child("Doctors");
        pDataBase = FirebaseDatabase.getInstance().getReference().child("Therapist");
        staffDatabase = FirebaseDatabase.getInstance().getReference().child("Staffs");
        AllDataBase = FirebaseDatabase.getInstance().getReference().child("ALL");
        clinicMap = new HashMap<>();  // Initialize the map here
        Log.d("staff_register_zero", "Activity created, calling retrieveData()");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedname = nameList.get(position);
                selectedClinicId = clinicMap.get(selectedname);
                selectedClinic.setText("Your Selected Clinic is " + selectedname);
            }
        });
        retrieveData();

        staffRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName,lastName,email,mobileNo,finNo,password,staffType=null;
                int radioID = staff_choices.getCheckedRadioButtonId();

                if(radioID == R.id.staff_selected_doctor){
                    staffType = "Doctor";
                }else if(radioID == R.id.staff_selected_therapist){
                    staffType = "Theapist";
                } else if (radioID == 0) {
                    staffType = "";
                    Toast.makeText(getApplicationContext(), "Pleas choose the staff type.", Toast.LENGTH_LONG).show();
                }
                firstName = getIntent().getStringExtra("firstName");
                lastName = getIntent().getStringExtra("lastName");
                email = getIntent().getStringExtra("email");
                mobileNo = getIntent().getStringExtra("phoneNumber");
                finNo = getIntent().getStringExtra("finNumber");
                password = getIntent().getStringExtra("password");
                String userType = "";
                String staffId = "";
                registerStaff(firstName,lastName,email,mobileNo,finNo,password,staffType,userType,staffId);
            }


        });

    }

    private void registerStaff(String first_name,String last_name,String email,String mobile_no,String fin_no,String password,String staff_type,String userType,String staffID) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            String staffID = uid;
                            staff_zero staffZero = new staff_zero(first_name,last_name,email,mobile_no,fin_no,selectedname,"null",selectedClinicId,staffID);

                            String E_mail = firebaseUser.getEmail();
                            if(staff_type == "Doctor"){
                                staff_zero staffOne = new staff_zero(first_name,last_name,email,mobile_no,fin_no,selectedname,"Doctors",selectedClinicId,staffID);
                                dDataBase.child(uid).setValue(staffOne)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                firebaseUser.sendEmailVerification();
                                                Toast.makeText(staff_register_activity_one.this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(staff_register_activity_one.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                AllDataBase.child(uid).setValue(staffOne);
                                staffDatabase.child(uid).setValue(staffOne);
                                /*staff_zero staffOne = new staff_zero(first_name,last_name,email,mobile_no,fin_no,selectedname,"Doctors");
                                Toast.makeText(staff_register_activity_one.this,"Registration is successfully done",Toast.LENGTH_LONG).show();
                                dDataBase.push().setValue(staffOne);
                                AllDataBase.push().setValue(staffOne);
                                firebaseUser.sendEmailVerification();*/
                            }else if(staff_type == "Theapist"){
                                staff_zero staffOne = new staff_zero(first_name,last_name,email,mobile_no,fin_no,selectedname,"Doctors",selectedClinicId,uid);
                                pDataBase.child(uid).setValue(staffOne)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                firebaseUser.sendEmailVerification();
                                                Toast.makeText(staff_register_activity_one.this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(staff_register_activity_one.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                AllDataBase.child(uid).setValue(staffOne);
                                staffDatabase.child(uid).setValue(staffOne);
                                /*staff_zero staffOne = new staff_zero(first_name,last_name,email,mobile_no,fin_no,selectedname,"Therapists");
                                Toast.makeText(staff_register_activity_one.this,"Registration is successfully done",Toast.LENGTH_LONG).show();
                                pDataBase.push().setValue(staffOne);
                                AllDataBase.push().setValue(staffOne);
                                firebaseUser.sendEmailVerification();*/
                            }


                        }else{
                            Toast.makeText(staff_register_activity_one.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void retrieveData() {
        // Reference to the specific node where your data is stored
        DatabaseReference userRef = mDatabase;

        // Attach a listener to read the data
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing list
                nameList.clear();

                // Iterate through the children of the node and add them to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    staff Staff = snapshot.getValue(staff.class);
                    if (Staff != null) {
                        String clinicId = snapshot.getKey();  // Get the clinic ID (key)
                        String clinicName = Staff.getName();
                        nameList.add(Staff.getName());
                        clinicMap.put(clinicName, clinicId);
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
                //return null;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors

            }
        });

    }

}