package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.databinding.RowConverstaionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewholder> {

    Context context ;
    ArrayList<User> users;

    public UserAdapter(Context context , ArrayList<User> users) {
         this.context = context;
         this.users = users ;

    }

    @NonNull
    @Override
    public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

          View view = LayoutInflater.from(context).inflate(R.layout.row_converstaion,parent,false);
          return new UserViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder, int position) {

           User user = users.get(position);
           holder.binding.username.setText(user.getName());
        Glide.with(context).load(user.getProfileImg())
                .placeholder(R.drawable.profileimg)
                .into(holder.binding.userDP);

        String senderId , senderRoom ;
        senderId = FirebaseAuth.getInstance().getUid() ;
        senderRoom = senderId + user.getUid() ;

        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                           if (snapshot.exists()) {
                               String lastMssg = snapshot.child("lastMssg").getValue(String.class);
                               String mssgTime = snapshot.child("lastTime").getValue(String.class);

                               holder.binding.lastText.setText(lastMssg);
                               holder.binding.time.setText(mssgTime); ;
                           }
                           else {
                               holder.binding.lastText.setText("Tap to chat...");
                               holder.binding.time.setText("");
                           }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }) ;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("name" , user.getName());
                intent.putExtra("uid",user.getUid()) ;
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public  class UserViewholder extends RecyclerView.ViewHolder {

          RowConverstaionBinding binding ;
          public UserViewholder(@NonNull View itemView) {
              super(itemView);
              binding = RowConverstaionBinding.bind(itemView) ;
          }
      }
}
