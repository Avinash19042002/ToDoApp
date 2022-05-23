package com.example.android.dometodoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
FirebaseAuth mAuth;

TextView today,important,pending,allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("Home");
        tb.setLogo(R.drawable.ic_baseline_home_24);
        setSupportActionBar(tb);
        mAuth=FirebaseAuth.getInstance();

        today =findViewById(R.id.Today);
        important =findViewById(R.id.Important);
        pending =findViewById(R.id.Pending);
        allTasks = findViewById(R.id.alltasks);

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AllTasksActivity.class));
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TodayActivity.class));
            }
        });
        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ImportantActivity.class));
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PendingActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int item_id = item.getItemId();
     if(item_id==R.id.log_out){
            mAuth.signOut();
            Toast.makeText(MainActivity.this,"You have been Signed Out",Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        return true;}
        return super.onOptionsItemSelected(item);
    }
}