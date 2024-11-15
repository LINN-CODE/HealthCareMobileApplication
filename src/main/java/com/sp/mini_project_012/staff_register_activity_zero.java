package com.sp.mini_project_012;

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

public class staff_register_activity_zero extends AppCompatActivity {

    EditText staffFirstName,staffLastName,staffPhoneNumber,staffEmail,staffFinNumber,staffPassword,staffConfirmPassword;
    Button next;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_register_zero);
        staffFirstName = findViewById(R.id.staff_register_firstName);
        staffLastName = findViewById(R.id.staff_register_lastName);
        staffPhoneNumber = findViewById(R.id.staff_register_phoneNumber);
        staffEmail = findViewById(R.id.staff_register_email);
        staffFinNumber = findViewById(R.id.staff_register_finNumber);
        staffPassword = findViewById(R.id.staff_register_password);
        staffConfirmPassword = findViewById(R.id.staff_register_confirmPassword);
        back = findViewById(R.id.staff_register_back_main);
        next = findViewById(R.id.staff_register_nextButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_0 = new Intent(getApplicationContext(), SplashScreen2Activity.class);
                startActivity(intent_0);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name,last_name,phone_number,email,fin_number,password,confirm_password;
                first_name = String.valueOf(staffFirstName.getText());
                last_name = String.valueOf(staffLastName.getText());
                phone_number = String.valueOf(staffPhoneNumber.getText());
                email = String.valueOf(staffEmail.getText());
                fin_number = String.valueOf(staffFinNumber.getText());
                password = String.valueOf(staffPassword.getText());
                confirm_password = String.valueOf(staffConfirmPassword.getText());

                if(TextUtils.isEmpty(first_name)){
                    Toast.makeText(staff_register_activity_zero.this, "Please enter the first name.", Toast.LENGTH_SHORT).show();
                    staffFirstName.requestFocus();
                }else if(TextUtils.isEmpty(last_name)){
                    Toast.makeText(staff_register_activity_zero.this, "Please enter the last name.", Toast.LENGTH_SHORT).show();
                    staffLastName.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(staff_register_activity_zero.this, "Please enter the valid email.", Toast.LENGTH_SHORT).show();
                    staffEmail.requestFocus();
                }else if(!Patterns.PHONE.matcher(phone_number).matches()){
                    Toast.makeText(staff_register_activity_zero.this, "Please enter the valid phone number.", Toast.LENGTH_SHORT).show();
                    staffPhoneNumber.requestFocus();
                }else if(TextUtils.isEmpty(fin_number)){
                    Toast.makeText(staff_register_activity_zero.this, "Please enter the FIN number.", Toast.LENGTH_SHORT).show();
                    staffFinNumber.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(staff_register_activity_zero.this, "Please enter the  Password", Toast.LENGTH_SHORT).show();
                    staffPassword.requestFocus();
                } else if(TextUtils.isEmpty(confirm_password)){
                    Toast.makeText(staff_register_activity_zero.this, "Please enter the confirm password.", Toast.LENGTH_SHORT).show();
                    staffConfirmPassword.requestFocus();
                } else if(password.length() < 5) {
                    Toast.makeText(staff_register_activity_zero.this, "Your Password should be at least 6 digits.", Toast.LENGTH_SHORT).show();
                    staffPassword.requestFocus();
                }else{
                    Intent intent = new Intent(staff_register_activity_zero.this,staff_register_activity_one.class);
                    intent.putExtra("firstName",first_name);
                    intent.putExtra("lastName",last_name);
                    intent.putExtra("phoneNumber",phone_number);
                    intent.putExtra("email",email);
                    intent.putExtra("finNumber",fin_number);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }

            }
        });
    }
}