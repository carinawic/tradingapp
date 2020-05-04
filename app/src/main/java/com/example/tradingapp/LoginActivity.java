package com.example.tradingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        //find by id from the xml
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);

        // we add an AuthStateListener
        // it keeps track of when we switch states (from not logged in to logged in)
        // people tend to have this code, but I don't really see the point honestly
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            //if there is a state switch
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                //if the user is not null
                if(mFirebaseUser != null){
                    //we are logged in
                    Toast.makeText(LoginActivity.this, "you are logged in", Toast.LENGTH_SHORT).show();
                }else{
                    //we are not logged in
                    Toast.makeText(LoginActivity.this, "please login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //we add an OnClickListener to the signIn-button
        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
           public void onClick(View v){
               String email = emailId.getText().toString();
               String pwd = password.getText().toString();

               //both fields are empty - error message
               if (email.isEmpty() && pwd.isEmpty()) {
                   Toast.makeText(LoginActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();
               }
               //email field is empty - error message
               else if (email.isEmpty()) {
                   emailId.setError("Please enter email id");
                   emailId.requestFocus();
               }
               //password field is empty - error message
               else if (pwd.isEmpty()) {
                   password.setError("please enter your password");
                   password.requestFocus();
               }
               //there are text in both fields
               else if (!(email.isEmpty() && pwd.isEmpty())) {
                   //we try to sign in the user with an OnCompleteListener
                   mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                        //OnComplete, if not successful - error message
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login Error, please Login Again", Toast.LENGTH_SHORT).show();
                        }
                        //OnComplete, if successful - we go to HomeActivity (logged in)
                        else{
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        }
                       }
                   });
               }
               //if we for some reason haven't triggered errors and still haven't signed up - error
               else{
                   Toast.makeText(LoginActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
               }
           }
        });

        //we add an OnClickListener to the "sign up here" textview
        tvSignUp.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               //we go to the LoginActivity
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
           }
        });
    }

    //on start we start the firebase auth thing
    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
