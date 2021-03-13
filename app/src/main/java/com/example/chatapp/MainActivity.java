package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;
    FirebaseDatabase database ;
    ArrayList<User> users;
    UserAdapter userAdapter ;
   // TopStatusAdapter statusAdapter ;
    ArrayList<UserStatus> userStatuses;
    ProgressDialog dialog ;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dialog = new ProgressDialog(this) ;
        dialog.setMessage("Uploading Status....");
        dialog.setCancelable(false);


        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();
        userStatuses = new ArrayList<>() ;

        userAdapter = new UserAdapter(this,users);
       // statusAdapter = new TopStatusAdapter(this,userStatuses) ;

        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                          user = snapshot.getValue(User.class) ;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }) ;

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        binding.statusList.setLayoutManager(layoutManager);
//        binding.statusList.setAdapter(statusAdapter);
          binding.rcv.setAdapter(userAdapter);

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  users.clear();
                  for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                       User user = snapshot1.getValue(User.class);

                       if (!(FirebaseAuth.getInstance().getUid().equals(user.getUid())))
                       users.add(user);
                  }

                  userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.status :
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,75);
                }
                return false;
            }
        });


        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userStatuses.clear();
                    for(DataSnapshot storySnapshot : snapshot.getChildren()) {
                        UserStatus status = new UserStatus();
                        status.setName(storySnapshot.child("name").getValue(String.class));
                        status.setProfileImg(storySnapshot.child("profileImage").getValue(String.class));
                        status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(String.class));

                        ArrayList<Status> statuses = new ArrayList<>();

                        for(DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren()) {
                            Status sampleStatus = statusSnapshot.getValue(Status.class);
                            statuses.add(sampleStatus);
                        }

                        status.setStatuses(statuses);
                        userStatuses.add(status);
                    }
                    //binding.statusList.hideShimmerAdapter();
                   // statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
              if (data.getData() != null) {

                  dialog.show();
                  DateFormat dfTime = new SimpleDateFormat("HH:mm");
                  String time = dfTime.format(Calendar.getInstance().getTime());
                  FirebaseStorage storage = FirebaseStorage.getInstance();
                  StorageReference reference = storage.getReference().child("status").child(time);
                  reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                             if (task.isSuccessful()) {
                                 reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                     @Override
                                     public void onSuccess(Uri uri) {

                                           UserStatus userStatus = new UserStatus() ;
                                           userStatus.setName(user.getName());
                                           userStatus.setProfileImg(user.getProfileImg());
                                           userStatus.setLastUpdated(time);


                                         HashMap<String, Object> obj = new HashMap<>();
                                         obj.put("name", userStatus.getName());
                                         obj.put("profileImage", userStatus.getProfileImg());
                                         obj.put("lastUpdated", userStatus.getLastUpdated());

                                         String imageUrl = uri.toString();
                                         Status status = new Status(imageUrl, userStatus.getLastUpdated());

                                         database.getReference()
                                                 .child("stories")
                                                 .child(FirebaseAuth.getInstance().getUid())
                                                 .updateChildren(obj);

                                         database.getReference().child("stories")
                                                 .child(FirebaseAuth.getInstance().getUid())
                                                 .child("statuses")
                                                 .push()
                                                 .setValue(status);

                                         dialog.dismiss();
                                     }
                                 }) ;
                             }
                      }
                  })  ;
              }
        }
    }

    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.search :
//                break;
//            case R.id.groups :
//                break;
//            case
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu , menu);
        return super.onCreateOptionsMenu(menu);
    }
}