package com.sp.mini_project_012;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import java.util.ArrayList;

public class userMainPage_booking extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    String clinicId, userId;
    private DatabaseReference assignmentDatabase, bookingDatabase, userDatabase, chatDatabase;
    private ArrayList<assignment> assignmentList;
    private assignment_adapter assignmentAdapter;
    private ListView assignmentListView;
    private TextView selectedTextView, availableTextView;
    private Button bookAssignments;
    private assignment selectedAssignment;
    private String assignmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page_booking);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Get clinic ID and user ID from intent
        clinicId = getIntent().getStringExtra("clinicID");
        userId = getIntent().getStringExtra("userId");
        createNotificationChannel();
        // Find views by their IDs
        selectedTextView = findViewById(R.id.user_mainpage_booking_textView);
        assignmentListView = findViewById(R.id.user_main_page_booking_assignment_listView);
        bookAssignments = findViewById(R.id.user_mainPage_booking_button);
        availableTextView = findViewById(R.id.user_mainPage_booking_slot_textView);


        // Initialize Firebase database reference
        assignmentDatabase = FirebaseDatabase.getInstance().getReference("Assignments");
        bookingDatabase = FirebaseDatabase.getInstance().getReference("Bookings").child(clinicId);
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        chatDatabase = FirebaseDatabase.getInstance().getReference("user_chat");
        assignmentList = new ArrayList<>();
        assignmentAdapter = new assignment_adapter(this, assignmentList);
        assignmentListView.setAdapter(assignmentAdapter);

        assignmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAssignment = assignmentList.get(position);
                assignmentId = selectedAssignment.getAssignmentId();
                if (selectedAssignment.getShift().equals("Morning Shift")) {
                    selectedTextView.setText("Selected: " + selectedAssignment.getDate() + " Morning 9am");
                } else {
                    selectedTextView.setText("Selected: " + selectedAssignment.getDate() + " Evening 3pm");
                }
            }
        });

        retrieveAssignments();

        bookAssignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAssignment != null) {
                    checkAndAddBooking(selectedAssignment);
                } else {
                    Toast.makeText(userMainPage_booking.this, "Please select an assignment first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showNotification(String title, String content) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            builder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }

        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void createBooking(Booking booking) {
        //DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Bookings").child(booking.getClinicId()).push();
        String bookingId = bookingDatabase.getKey();
        booking.setBookingId(bookingId);

        bookingDatabase.setValue(booking).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Send message to user

            } else {
                // Handle booking creation failure
            }
        });
    }

    private void sendMessageToUser(String userId, String doctorId) {
        DatabaseReference userChatRef = FirebaseDatabase.getInstance().getReference("user_chat").child(userId).child("chats").child(doctorId).child("messages");
        Date date = new Date();
        messageModel message = new messageModel("Thank you for making the reservation. Please put a note.", doctorId, date.getTime());

        userChatRef.push().setValue(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Message sent successfully
            } else {
                // Handle message sending failure
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            String channelDescription = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("RestrictedApi")
    private void addBookingToDatabase(assignment selectedAssignment) {
        if (selectedAssignment == null) {
            Toast.makeText(userMainPage_booking.this, "No assignment selected.", Toast.LENGTH_SHORT).show();
            return;
        }

        String bookingId = bookingDatabase.push().getKey();

        if (bookingId == null) {
            Toast.makeText(userMainPage_booking.this, "Booking ID is null.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Booking ID is null.");
            return;
        }

        if (selectedAssignment.getAssignmentId() == null) {
            Toast.makeText(userMainPage_booking.this, "Assignment ID is null.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Assignment ID is null.");
            return;
        }

        if (selectedAssignment.getDate() == null) {
            Toast.makeText(userMainPage_booking.this, "Date is null.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Date is null.");
            return;
        }

        if (selectedAssignment.getShift() == null) {
            Toast.makeText(userMainPage_booking.this, "Shift is null.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Shift is null.");
            return;
        }

        String assignmentPath = clinicId + "/" + selectedAssignment.getDate() + "_" + selectedAssignment.getShift();

        // Check if the selected assignment is already booked
        assignmentDatabase.child(assignmentPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assignment currentAssignment = dataSnapshot.getValue(assignment.class);
                if (currentAssignment != null) {
                    String isBooked = currentAssignment.isBooked();
                    Log.d(TAG, "Current assignment isBooked: " + isBooked);
                    if ("true".equals(isBooked)) {
                        Toast.makeText(userMainPage_booking.this, "This time slot is already booked.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Proceed with booking
                        Booking booking = new Booking(
                                bookingId,
                                clinicId,
                                userId,
                                selectedAssignment.getDate(),
                                selectedAssignment.getShift(),
                                selectedAssignment.getAssignmentId(),
                                currentAssignment.getDoctorId()
                        );
                        String chatPath = userId + "/chats" + currentAssignment.getDoctorId();

                        bookingDatabase.child(bookingId).setValue(booking)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Mark the assignment as booked
                                        if (assignmentDatabase.child(assignmentPath).child("shift").equals("Morning Shift")) {
                                            assignmentDatabase.child(assignmentPath).child("shift").setValue("Morning_taken");
                                        } else {
                                            assignmentDatabase.child(assignmentPath).child("shift").setValue("Evening_taken");
                                        }
                                        sendMessageToUser(booking.getUserId(), booking.getDoctorId());
                                        Log.d(TAG, "Booking added successfully");
                                        Toast.makeText(userMainPage_booking.this, "Booking added successfully", Toast.LENGTH_SHORT).show();

                                        // Add notification to database
                                        addNotificationToDatabase("Your booking has been confirmed.", selectedAssignment.getDate() + " " + selectedAssignment.getShift());

                                        // Show notification
                                        showNotification("Booking Confirmed", "Your booking has been confirmed.");

                                    } else {
                                        Log.e(TAG, "Failed to add booking", task.getException());
                                        Toast.makeText(userMainPage_booking.this, "Failed to add booking", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                } else {
                    Log.e(TAG, "Current assignment is null for path: " + assignmentPath);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to check assignment booking status", databaseError.toException());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void checkAndAddBooking(assignment selectedAssignment) {
        if (selectedAssignment == null) {
            Toast.makeText(userMainPage_booking.this, "No assignment selected.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for existing active bookings for the user
        bookingDatabase.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean hasActiveBooking = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking existingBooking = snapshot.getValue(Booking.class);
                    if (existingBooking != null && !existingBooking.isCompleted()) {
                        hasActiveBooking = true;
                        break;
                    }
                }

                if (hasActiveBooking) {
                    Toast.makeText(userMainPage_booking.this, "You already have an active appointment.", Toast.LENGTH_SHORT).show();

                    // Add notification to database
                    addNotificationToDatabase("Active Appointment", "You already have an active appointment.");

                    // Show notification
                    showNotification("Active Appointment", "You already have an active appointment.");

                } else {
                    // Proceed with booking
                    addBookingToDatabase(selectedAssignment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to check existing bookings", databaseError.toException());
            }
        });
    }

    private void retrieveAssignments() {
        assignmentDatabase.child(clinicId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assignmentList.clear();

                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot shiftSnapshot : dateSnapshot.getChildren()) {
                        Log.d(TAG, "Shift Snapshot Key: " + shiftSnapshot.getKey() + " | Value: " + shiftSnapshot.getValue());
                        for (DataSnapshot fieldSnapshot : shiftSnapshot.getChildren()) {
                            assignment Assignment = fieldSnapshot.getValue(assignment.class);
                            if (Assignment != null) {
                                String isBooked = Assignment.isBooked();
                                Log.d(TAG, "Assignment: " + Assignment.getDate() + " " + Assignment.getShift() + " isBooked: " + isBooked);
                                if (Assignment.getShift().equals("Morning Shift") || Assignment.getShift().equals("Evening Shift")) { // Exclude booked assignments
                                    Log.d(TAG, "Adding Assignment: " + Assignment.getDate() + " " + Assignment.getShift());
                                    assignmentList.add(Assignment);
                                }
                            } else {
                                Log.d(TAG, "Assignment is null for snapshot: " + shiftSnapshot.getKey());
                            }
                        }
                    }
                }
                assignmentAdapter.notifyDataSetChanged();

                if (assignmentList.isEmpty()) {
                    availableTextView.setText("There are no available slots"); // Show if no appointments
                } else {
                    availableTextView.setVisibility(View.GONE); // Hide if there are appointments
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error retrieving assignments: ", error.toException());
            }
        });
    }

    private void addNotificationToDatabase(String message, String dateTime) {
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);
        String notificationId = notificationRef.push().getKey();
        AppNotification notification = new AppNotification(message, dateTime);
        notificationRef.child(notificationId).setValue(notification);
    }
}
