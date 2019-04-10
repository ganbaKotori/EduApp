package com.shape.eduapp;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
    Checks current session of Firebase user
 */
public class HomeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //Send user back to StudentMainActivity if user still logged in & verify user
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            startActivity(new Intent(HomeApplication.this, StudentMainActivity.class));
        }
    }
}
