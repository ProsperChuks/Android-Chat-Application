package com.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText user;
    EditText pass;
    Button login;
    TextView forgotPwd;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListen;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        forgotPwd = findViewById(R.id.forgot);
        login = findViewById(R.id.login);


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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString();
                String password = pass.getText().toString();

                if (!username.equals("") && !password.equals("")){
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Failed to Sign in", Toast.LENGTH_LONG)
                                                .show();
                                    }else {
                                        Toast.makeText(LoginActivity.this, "Sign in Successful", Toast.LENGTH_LONG)
                                                .show();
                                        Intent showHome = new Intent(LoginActivity.this, Home.class);
                                        startActivity(showHome);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill in login details", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Retrieve it then", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Adds Authentication Listener when the app starts
        mAuth.addAuthStateListener(authListen);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Remove Authentication Listener while the app terminates
        if (authListen != null){
            mAuth.removeAuthStateListener(authListen);
        }
    }
}
