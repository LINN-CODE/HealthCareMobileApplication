package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class clinicMainPage_zero extends AppCompatActivity {
    String doctorId,clinicId;
    DatePicker datePicker;
    TextView testingTextView;
    DatabaseReference doctorDatabase,assignmenDatabase;
    Button doctorAssignButton;
    RadioGroup choosingShiftRadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_main_page_zero);
        testingTextView = findViewById(R.id.clinic_mainpage_zero_testing);
        clinicId = getIntent().getStringExtra("clinicID");
        doctorId = getIntent().getStringExtra("doctorID");
        datePicker = findViewById(R.id.clinic_mainPage_zero_date_picker);

        doctorAssignButton = findViewById(R.id.clinic_mainpage_zero_assignDoctorButton);
        choosingShiftRadioGroup = findViewById(R.id.clinic_mainpage_zero_radio_group);

        assignmenDatabase = FirebaseDatabase.getInstance().getReference("Assignments");
        doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors");

        //Make Current date and set it as the minimum date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        long minDate = calendar.getTimeInMillis();
        datePicker.setMinDate(minDate);
        fetchAndDisplayDoctorName(doctorId);

        doctorAssignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignDoctorToShift();
            }
        });
    }

    private void assignDoctorToShift() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String selectedDate = day + "/" + (month+1) + "/" + year;

        int selectedShiftId = choosingShiftRadioGroup.getCheckedRadioButtonId();
        if(selectedShiftId == -1){
            Toast.makeText(this, "Please select a shift", Toast.LENGTH_LONG).show();
            return;
        }
        RadioButton selectedShiftRadioButton = findViewById(selectedShiftId);
        String selectedShift = selectedShiftRadioButton.getText().toString();

        checkAndAssignShift(selectedDate,selectedShift);

    }

    private void checkAndAssignShift(String date, String shift) {
        String shiftPath = clinicId + "/" + date + "_" + shift;
        //String bookingForUseShift = shift.trim() + "_booking";

        assignmenDatabase.child(shiftPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(clinicMainPage_zero.this, "This shift already assigned",Toast.LENGTH_LONG).show();
                }else{
                    assignShift(date,shift);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(clinicMainPage_zero.this, "Error checking assignment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void assignShift(String date, String shift) {
        // Generate a unique key for the assignment
        String assignmentId = assignmenDatabase.push().getKey();
        String shiftPath = clinicId + "/" + date + "_" + shift;
        Map<String, String> assignment = new HashMap<>();
        assignment.put("assignmentId", assignmentId);
        assignment.put("doctorId", doctorId);
        assignment.put("clinicId", clinicId);
        assignment.put("date", date);
        assignment.put("shift", shift);
        assignment.put("isBooked", "false"); // Store as a string
        assignmenDatabase.child(shiftPath).setValue(assignment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(clinicMainPage_zero.this, "Doctor assigned successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(clinicMainPage_zero.this, "Error assigning doctor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAndDisplayDoctorName(String doctorId) {
        doctorDatabase.child(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    staff_zero doctor = snapshot.getValue(staff_zero.class);
                    if(doctor != null){
                        String doctorName = doctor.getFirstName();
                        testingTextView.setText("Selected Name: " + doctorName);
                    }else{
                        testingTextView.setText("Doctor Not Found");
                    }
                }else{
                    testingTextView.setText("Doctor Not Found 101");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}