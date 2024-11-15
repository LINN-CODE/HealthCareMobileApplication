package com.sp.mini_project_012;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SplashScreen2Activity extends AppCompatActivity {
    Button User_Splash,Staff_Splash,Clinic_Splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_2);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        User_Splash = findViewById(R.id.user_splash);
        Staff_Splash = findViewById(R.id.staff_splash);
        Clinic_Splash = findViewById(R.id.clinic_splash);
        User_Splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_0 = new Intent(getApplicationContext(), user_log_in_page.class);
                startActivity(intent_0);
            }
        });

        Staff_Splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_0 = new Intent(getApplicationContext(), staff_log_in_page.class);
                startActivity(intent_0);
            }
        });


        Clinic_Splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_0 = new Intent(getApplicationContext(), clinic_log_in_page.class);
                startActivity(intent_0);
            }
        });

    }


}