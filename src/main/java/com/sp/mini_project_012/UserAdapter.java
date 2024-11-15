package com.sp.mini_project_012;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {

    user_messaeges_page clinicMessages;
    ArrayList<users> usersArrayList;

    users ID;



    public UserAdapter(user_messaeges_page clinicMessages, ArrayList<users> usersArrayList) {
        this.clinicMessages=clinicMessages;
        this.usersArrayList=usersArrayList;
    }


    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(clinicMessages).inflate(R.layout.message_row, parent, false);
        return new viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {

        users user =usersArrayList.get(position);

        holder.usersname.setText(user.getFirstName());
        holder.userstatus.setText(user.getLastName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clinicMessages, userMainPage_Message.class);
                intent.putExtra("Namee", user.getFirstName());
                intent.putExtra("uid", user.getUserID());
                clinicMessages.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {


        TextView usersname;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            usersname=itemView.findViewById(R.id.sender_name);
            userstatus=itemView.findViewById(R.id.received_text);

        }
    }

}
