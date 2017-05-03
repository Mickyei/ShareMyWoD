package com.example.micky.sharemywod;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SingleWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_workout);

        TextView tv = (TextView) findViewById(R.id.workout);
        tv.setText(getIntent().getStringExtra("workout"));
    }
}
