package com.sp.mini_project_012;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class userMainPage_Message extends AppCompatActivity {
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page_message);

        // Retrieve userId from the intent
        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        }

        /*BottomNavigationView bottomNavigationView = findViewById(R.id.userPageBottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_message_user);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.bottom_home_user) {
                intent = new Intent(getApplicationContext(), userMainPage.class);
            } else if (itemId == R.id.bottom_profile_user) {
                intent = new Intent(getApplicationContext(), userMainPage_Profile.class);
            } else if (itemId == R.id.bottom_fitness_user) {
                intent = new Intent(getApplicationContext(), userMainPage_Fitness.class);
            }
            if (intent != null) {
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });*/
    }
}
