package com.sp.mini_project_012;
// MainActivity.java
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;

public class morning_testing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        String userID = "02BxAfZD6aCj6jGzWdt"; // Replace with the actual user ID

        FirebaseUtils_testing.checkUserType(userID, new FirebaseCallbac_testing() {
            @Override
            public void onCallback(String userType) {
                if (userType != null) {
                    switch (userType) {
                        case "Users":
                            startActivity(new Intent(morning_testing.this, staff_register_activity_zero.class));
                            break;
                        case "Doctors":
                            startActivity(new Intent(morning_testing.this, staffMainPage.class));
                            break;
                        case "Clinics":
                            startActivity(new Intent(morning_testing.this, clinicMainPage.class));
                            break;
                        default:
                            // Handle unknown user type
                            break;
                    }
                } else {
                    // Handle user not found in any table
                }
            }
        });
    }
}
