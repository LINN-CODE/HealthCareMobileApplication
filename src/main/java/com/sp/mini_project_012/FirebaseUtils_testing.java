package com.sp.mini_project_012;
// FirebaseUtils.java

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtils_testing {

    private static final String TAG = "FirebaseUtils_testing";

    public static void checkUserType(String userID, final FirebaseCallbac_testing callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors").child(userID);
        DatabaseReference clinicsRef = FirebaseDatabase.getInstance().getReference("Clinics").child(userID);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User found in users table");
                    callback.onCallback("Users");
                } else {
                    Log.d(TAG, "User not found in users table, checking doctors table");
                    doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Log.d(TAG, "User found in doctors table");
                                callback.onCallback("Doctors");
                            } else {
                                Log.d(TAG, "User not found in doctors table, checking clinics table");
                                clinicsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Log.d(TAG, "User found in clinics table");
                                            callback.onCallback("Clinics");
                                        } else {
                                            Log.d(TAG, "User not found in any table");
                                            callback.onCallback(null);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Log.e(TAG, "Error checking clinics table", error.toException());
                                        callback.onCallback(null);
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.e(TAG, "Error checking doctors table", error.toException());
                            callback.onCallback(null);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error checking users table", error.toException());
                callback.onCallback(null);
            }
        });
    }
}
