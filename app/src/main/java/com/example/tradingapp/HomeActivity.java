package com.example.tradingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    Button btnLogout;
    Button btnDisplay;
    ImageView image1;
    FirebaseFunctions mFunctions;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://tradingapp-8b41e.appspot.com").child("sofa.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //find by id from the xml
        btnLogout = findViewById(R.id.homeBtnLogout);
        btnDisplay = findViewById(R.id.homeBtnDisplay);
        image1 = findViewById(R.id.homeImage);


        mFunctions = FirebaseFunctions.getInstance();

        //we add an OnClickListener to the log out-button
        btnDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    final File file = File.createTempFile("image", "jpg");
                    storageReference.getFile(file).addOnSuccessListener(HomeActivity.this, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot){
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            image1.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            //Vi hamnar här vid exekvering!!! :( Robin hjälp
                            Toast.makeText(HomeActivity.this, "Image failed to load", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });

        //we add an OnClickListener to the log out-button
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });
    }
}