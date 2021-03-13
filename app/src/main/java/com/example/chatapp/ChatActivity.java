package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding ;
    RecyclerView recyclerView ;
    MessageAdapter adapter ;
    ArrayList<Message> messages ;

    FirebaseDatabase database ;

    String senderRoom , receiverRoom ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages = new ArrayList<>();

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid() ;

        senderRoom = senderUid + receiverUid ;
        receiverRoom = receiverUid + senderUid ;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(this,messages,senderRoom,receiverRoom);
        binding.recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance() ;

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                              Message message = snapshot1.getValue(Message.class) ;
                              message.setMssgId(snapshot1.getKey());
                              messages.add(message);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   String messageSent = binding.messageBox.getText().toString();
                   Date date = new Date();
                    DateFormat dfTime = new SimpleDateFormat("HH:mm");
                    String time = dfTime.format(Calendar.getInstance().getTime());

                  Log.i("Time", time);
                Message message = new Message(messageSent , senderUid , time);
                binding.messageBox.setText("");

                   String randomKey = database.getReference().push().getKey();

                   HashMap<String,Object> lastObj = new HashMap<>() ;
                   lastObj.put("lastMssg" , message.getMssg()) ;
                   lastObj.put("lastTime" , time) ;

                database.getReference().child("chats").child(senderRoom).updateChildren(lastObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastObj);

                   database.getReference().child("chats")
                           .child(senderRoom)
                           .child("messages")
                           .child(randomKey)
                           .setValue(message)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                                   database.getReference().child("chats")
                                           .child(receiverRoom)
                                           .child("messages")
                                           .child(randomKey)
                                           .setValue(message)
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                               }
                                           }) ;

                                   database.getReference().child("chats").child(senderRoom).updateChildren(lastObj);
                                   database.getReference().child("chats").child(receiverRoom).updateChildren(lastObj);

                               }

                           }) ;
            }
        });

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}