package com.shape.eduapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class RegisterActivity extends AppCompatActivity {
    Toolbar toolbarRegistration;
    ProgressBar progressBarRegistration;
    EditText etEmailRegistration, etPwRegistration, etFirstNameRegistration, etLastNameRegistration;
    Spinner spinnerRegistration;
    Button btnRegistration;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //toolbarRegistration = findViewById(R.id.toolbarRegistration);
       // toolbarRegistration.setTitle("Register for EduApp"); //Register toolbar title
        progressBarRegistration = findViewById(R.id.progressBarRegistration);
        etEmailRegistration = findViewById(R.id.etEmailRegistration);
        etPwRegistration = findViewById(R.id.etPwRegistration);
        etFirstNameRegistration = findViewById(R.id.etFirstNameRegistration);
        etLastNameRegistration = findViewById(R.id.etLastNameRegistration);
        btnRegistration = findViewById(R.id.btnRegistration);
        //Spinner
        spinnerRegistration = findViewById(R.id.spinnerRegistration);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.userTypeSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegistration.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();

        //User registration button action
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmailRegistration.getText().toString().isEmpty() && etPwRegistration.getText().toString().isEmpty() &&
                        etFirstNameRegistration.getText().toString().isEmpty() && etLastNameRegistration.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill out the fields with valid input.",
                            Toast.LENGTH_LONG).show();
                }
                else {
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

                                                    //FOR WRITING DATA TO DB
                                                    final String firstName = etFirstNameRegistration.getText().toString(); //Registration first name
                                                    final String lastName = etLastNameRegistration.getText().toString(); //Registration last name
                                                    final String email = etEmailRegistration.getText().toString();
                                                    final String userType = spinnerRegistration.getSelectedItem().toString(); //Get the selected user type
                                                    User newUser = new User(firstName, lastName, email, userType);

                                                    FirebaseDatabase.getInstance().getReference("Users")
                                                            .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .setValue(newUser);

                                                    Toast.makeText(RegisterActivity.this, "Registered successfully", //Successful registration
                                                            Toast.LENGTH_LONG).show();
                                                    etEmailRegistration.setText(""); //Clear fields upon successful registration
                                                    etPwRegistration.setText("");
                                                    etFirstNameRegistration.setText("");
                                                    etLastNameRegistration.setText("");
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
            }
        });
    }
}
