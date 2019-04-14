package com.shape.eduapp;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    EditText etEmailLogin, etPwLogin;

    Button btnLogin;
    TextView tvRegLink, tvResetPwLink;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String userTypeDBString; //To contain userType

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // toolbarLogin = findViewById(R.id.toolbarLogin);
        //toolbarLogin.setTitle("Login to EduApp"); //Login toolbar title
       // progBarLogin = findViewById(R.id.progBarLogin);
        etEmailLogin= findViewById(R.id.etEmailLogin);
        etPwLogin = findViewById(R.id.etPwLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegLink = findViewById(R.id.tvRegLink);
        tvResetPwLink = findViewById(R.id.tvResetPwLink);
        //Spinner
       // spinnerLogin = findViewById(R.id.spinnerLogin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.userTypeSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //User login button action
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmailLogin.getText().toString().isEmpty() && etPwLogin.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill out the fields with valid input.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //Progressbar UI
                    firebaseAuth.signInWithEmailAndPassword(etEmailLogin.getText().toString(),
                            etPwLogin.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) { //VERIFY USER EMAIL BEFORE THEY CAN LOGIN

                                            /*
                                                HAVE TO FIX THIS CODE TO COMPARE STRINGS
                                             */

                                            databaseReference = firebaseDatabase.getReference().child("Users").
                                                    child(firebaseAuth.getInstance().getCurrentUser().getUid()).
                                                    child("userType");

                                            databaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    userTypeDBString = dataSnapshot.getValue(String.class);

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    System.out.println("The read failed: " + databaseError.getCode());
                                                }
                                            });

                                            if (("Student").equals(userTypeDBString)) { //Send to student activity
                                                startActivity(new Intent(MainActivity.this, StudentMainActivity.class));
                                            }
                                            if (("Teacher").equals(userTypeDBString)) { //Send to teacher activity
                                                startActivity(new Intent(MainActivity.this, TeacherMainActivity.class));
                                            }
                                            else {
                                                Toast.makeText(MainActivity.this, "Please check to see if the correct user type is selected",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "Please verify your email address",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        //Link to register page
        tvRegLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Redirect to RegisterActivity page
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

        //Link to password reset page
        tvResetPwLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Redirect to PasswordResetActivity page
                Intent passwordResetIntent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                MainActivity.this.startActivity(passwordResetIntent);
            }
        });
    }
}
