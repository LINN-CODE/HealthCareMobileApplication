package com.sp.mini_project_012;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<messageModel> MessageAdapterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVED=2;


    public MessageAdapter(Context context, ArrayList<messageModel> messageAdapterArrayList) {
        this.context = context;
        this.MessageAdapterArrayList = messageAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType ==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new receiverViewHolder(view);
        }
        else {
            View view=LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        messageModel messages=MessageAdapterArrayList.get(position);

        if(holder.getClass()==receiverViewHolder.class){
            receiverViewHolder viewHolder=(receiverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());

        }else{
            senderViewHolder viewHolder=(senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return MessageAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        messageModel messages= MessageAdapterArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getReceiverId())){
            return ITEM_SEND;
        }else{
            return ITEM_RECEIVED;
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder {

        TextView msgtxt;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt=itemView.findViewById(R.id.receiver_chat_bubble);
        }
    }
    class senderViewHolder extends RecyclerView.ViewHolder {

        TextView msgtxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt=itemView.findViewById(R.id.sender_text_bubble);
        }
    }


}
