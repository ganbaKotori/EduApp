package com.shape.eduapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import  android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseAdder extends AppCompatActivity {

    EditText title, description;
    Button btnAddCourse;
    private DatabaseReference mCourseReference;
    private FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        title = findViewById(R.id.courseTitle);
        description = findViewById(R.id.courseDescription);
        btnAddCourse = findViewById(R.id.addCourse);

        mDatabase = FirebaseDatabase.getInstance();
        mCourseReference = mDatabase.getReference().child("courses");

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty() && description.getText().toString().isEmpty()) {
                    Toast.makeText(CourseAdder.this, "Please fill out the fields with valid input.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //progressBarRegistration.setVisibility(View.VISIBLE); //Progressbar UI
                    Course newCourse = new Course(title.getText().toString(),description.getText().toString());
                    mCourseReference.child(newCourse.getTitle()).setValue(newCourse).addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           // progressBarRegistration.setVisibility(View.GONE); //Progressbar UI
                            if (task.isSuccessful()) {
                                Toast.makeText(CourseAdder.this, "Lecture successfully added", //Successful registration
                                                            Toast.LENGTH_LONG).show();
                                                    title.setText(""); //Clear fields upon successful registration
                                                    description.setText("");
                                                }
                                                else {
                                                    Toast.makeText(CourseAdder.this, task.getException().getMessage(), //Error registering
                                                            Toast.LENGTH_LONG).show();
                                                }

                        }
                    });
                }
            }
        });

    }
}
