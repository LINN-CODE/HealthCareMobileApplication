package com.sp.mini_project_012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class clinic_chat extends AppCompatActivity {
    private static final String TAG = "clinic_chat";

    ImageButton back;
    String senderUID, senderName, receiverUID;
    TextView Sendername;
    ImageButton sendBtn;
    EditText textInput;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    RecyclerView messageAdapter;
    ArrayList<messageModel> messagesArrayList;

    MessageAdapter mmessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_chat);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        senderName = getIntent().getStringExtra("doctorName");
        senderUID = getIntent().getStringExtra("doctorId");
        receiverUID = firebaseAuth.getCurrentUser().getUid();

        messagesArrayList = new ArrayList<>();

        sendBtn = findViewById(R.id.message_send);
        textInput = findViewById(R.id.message_input);

        messageAdapter = findViewById(R.id.msgadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        mmessageAdapter = new MessageAdapter(clinic_chat.this, messagesArrayList);
        messageAdapter.setAdapter(mmessageAdapter);

        Sendername = findViewById(R.id.chat_sender_name);
        Sendername.setText(senderName);

        DatabaseReference chatReference = database.getReference()
                .child("user_chat")
                .child(receiverUID)
                .child("chats")
                .child(senderUID)
                .child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messageModel messages = dataSnapshot.getValue(messageModel.class);
                    messagesArrayList.add(messages);
                }
                mmessageAdapter.notifyDataSetChanged();
                messageAdapter.scrollToPosition(messagesArrayList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read messages", error.toException());
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textInput.getText().toString().trim();

                if (message.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter The Message", Toast.LENGTH_SHORT).show();
                    return;
                }

                textInput.setText("");
                Date date = new Date();
                messageModel Messages = new messageModel(message, receiverUID, date.getTime());

                database.getReference()
                        .child("user_chat")
                        .child(receiverUID)
                        .child("chats")
                        .child(senderUID)
                        .child("messages")
                        .push()
                        .setValue(Messages)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    database.getReference()
                                            .child("user_chat")
                                            .child(senderUID)
                                            .child("chats")
                                            .child(receiverUID)
                                            .child("messages")
                                            .push()
                                            .setValue(Messages);
                                } else {
                                    Log.e(TAG, "Failed to send message", task.getException());
                                }
                            }
                        });
            }
        });


    }
}
