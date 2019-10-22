package com.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    EditText user;
    EditText pass;
    EditText email;
    Button signup;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListen;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        user = (findViewById(R.id.user));
        email = (findViewById(R.id.mail));
        pass = (findViewById(R.id.pass));
        signup = (findViewById(R.id.signup));

        //Listens for when user is signed in or out
        authListen = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Getting Current User
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    Log.d(TAG, "User Is Signed In");
                }else {
                    //user is signed out
                    Log.d(TAG, "User Is Signed Out");
                }
            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String pwd = pass.getText().toString();

                if (!mail.equals("") && !pwd.equals("")){
                    mAuth.createUserWithEmailAndPassword(mail, pwd)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "Failed to Create Account", Toast.LENGTH_LONG)
                                                .show();
                                    }else {
                                        Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_LONG)
                                                .show();
                                        Intent showHome = new Intent(SignupActivity.this, Home.class);
                                        startActivity(showHome);
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListen);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authListen);
    }
}
