package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfile extends AppCompatActivity {


    FirebaseDatabase database ;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage ;

    ImageView imageView ;
    EditText name ;
    Button save ;

    Uri selectedImage ;

    ProgressDialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating Profile ....");
        dialog.setCancelable(false);

        getSupportActionBar().hide();

       imageView = findViewById(R.id.imageView);
       save = findViewById(R.id.button) ;

       firebaseAuth = FirebaseAuth.getInstance() ;

       firebaseStorage = FirebaseStorage.getInstance() ;
       database = FirebaseDatabase.getInstance() ;

       imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent() ;
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);
            }
        });

       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               name = findViewById(R.id.editText) ;
               String x = name.getText().toString() ;

               if (x.isEmpty()) {
                   name.setError("Plz...type a name");
                   return;
               }

               dialog.show();
               if (selectedImage != null ){
                   StorageReference storageReference = firebaseStorage.getReference().child("Profiles").child(firebaseAuth.getUid()) ;
                   storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                 storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                     @Override
                                     public void onSuccess(Uri uri) {
                                         String imageUrl = uri.toString();
                                         String uid = firebaseAuth.getUid();
                                         String email = firebaseAuth.getCurrentUser().getEmail();
                                         String name = x ;

                                         User user = new User(uid,name,email,imageUrl) ;

                                         database.getReference().child("users").child(uid).setValue(user)
                                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void aVoid) {

                                                            dialog.dismiss();
                                                            Intent intent = new Intent(SetupProfile.this,MainActivity.class) ;
                                                            startActivity(intent);
                                                            finish();
                                                     }
                                                 });
                                     }
                                 });
                            }

                       }
                   }) ;
               } else  {

                   String uid = firebaseAuth.getUid();
                   String email = firebaseAuth.getCurrentUser().getEmail();
                   String name = x ;

                   User user = new User(uid,name,email,"No image :(") ;

                   database.getReference().child("users").child(uid).setValue(user)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                                   dialog.dismiss();
                                   Intent intent = new Intent(SetupProfile.this,MainActivity.class) ;
                                   startActivity(intent);
                                   finish();
                               }
                           });
               }

           }
       });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
              if (data.getData() != null) {
                    imageView.setImageURI(data.getData());
                    selectedImage = data.getData() ;
              }
        }
    }
}