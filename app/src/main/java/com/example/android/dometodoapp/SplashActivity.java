package com.example.android.dometodoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
FirebaseAuth mAuth;
Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//      handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
//            }
//        },5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
 FirebaseUser user = mAuth.getCurrentUser();
 if(user==null){
     handler = new Handler();
     handler.postDelayed(new Runnable() {
         @Override
         public void run() {
             startActivity(new Intent(SplashActivity.this,LoginActivity.class));
             finish();
         }
     },5000);
 }
 else{
     handler = new Handler();
     handler.postDelayed(new Runnable() {
         @Override
         public void run() {
             startActivity(new Intent(SplashActivity.this,MainActivity.class));
             finish();
         }
     },5000);
 }

    }
}