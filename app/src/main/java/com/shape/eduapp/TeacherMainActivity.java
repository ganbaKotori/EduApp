package com.shape.eduapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import  android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherMainActivity extends AppCompatActivity {
    TextView tvUserEmail;
    Button btnLogout;

    private ListView mCourseListView;
    ArrayList<String> courses = new ArrayList<String>();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCourseDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnLogout = findViewById(R.id.btnLogout);

        firebaseAuth = firebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mCourseDatabaseReference = mFirebaseDatabase.getReference().child("courses");

        // Initialize references to views
        mCourseListView = (ListView) findViewById(R.id.courseListView);
        final ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,courses);
        mCourseListView.setAdapter(courseArrayAdapter);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course myChildValues = dataSnapshot.getValue(Course.class);
                courses.add(myChildValues.getTitle());
                courseArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                courseArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mCourseDatabaseReference.addChildEventListener(mChildEventListener);


        tvUserEmail.setText(firebaseUser.getEmail());

        //Logout button action
        btnLogout.setOnClickListener(new View.OnClickListener() { //User signout
            @Override
            public void onClick(View v) { //Makes sure the user is logged out
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TeacherMainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
