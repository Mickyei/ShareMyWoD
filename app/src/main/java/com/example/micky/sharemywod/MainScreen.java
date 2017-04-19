package com.example.micky.sharemywod;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void postWorkout(View v) {
        Intent i = new Intent(this, PostWorkout.class);
        startActivity(i);
    }
}
