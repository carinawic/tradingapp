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

public class MainActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    /* Signup default:
    * The Email must be on the form xxx@xxx.xxx
    * The password must be at least 6 characters
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        //find by id from the xml
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);

        //we add an OnClickListener to the signUp-button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                //both fields are empty - error message
                if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();
                }
                //email field is empty - error message
                else if (email.isEmpty()) {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                //password field is empty - error message
                else if (pwd.isEmpty()) {
                    password.setError("please enter your password");
                    password.requestFocus();
                }
                //there are text in both fields
                else if (!(email.isEmpty() && pwd.isEmpty())) {
                    //we try to create the user with an OnCompleteListener
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //OnComplete, if not successful - error message
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Signup unsuccessful, please try again", Toast.LENGTH_SHORT).show();
                            }
                            //OnComplete, if successful - we go to HomeActivity (logged in)
                            else{
                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            }
                        }
                    });
                }
                //if we for some reason haven't triggered errors and still haven't signed up - error
                else{
                    Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //we add an OnClickListener to the "sign in here" textview
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //we go to the LoginActivity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }
}
