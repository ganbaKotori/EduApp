package com.shape.eduapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    Toolbar toolbarRegistration;
    ProgressBar progressBarRegistration;
    EditText etEmailRegistration, etPwRegistration;
    Button btnRegistration;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbarRegistration = findViewById(R.id.toolbarRegistration);
        toolbarRegistration.setTitle("Register for EduApp"); //Register toolbar title
        progressBarRegistration = findViewById(R.id.progressBarRegistration);
        etEmailRegistration = findViewById(R.id.etEmailRegistration);
        etPwRegistration = findViewById(R.id.etPwRegistration);
        btnRegistration = findViewById(R.id.btnRegistration);

        firebaseAuth = FirebaseAuth.getInstance();

        //User registration button action
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarRegistration.setVisibility(View.VISIBLE); //Progressbar UI
                firebaseAuth.createUserWithEmailAndPassword(etEmailRegistration.getText().toString(),
                        etPwRegistration.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBarRegistration.setVisibility(View.GONE); //Progressbar UI
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Registered successfully", //Successful registration
                                                        Toast.LENGTH_LONG).show();
                                                etEmailRegistration.setText(""); //Clear fields upon successful registration
                                                etPwRegistration.setText("");
                                            }
                                            else {
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), //Error registering
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), //Error registering
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
