package com.sharemywod.micky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sharemywod.micky.R;

/**
 * Renders the main screen in which the user can
 * pick to post workouts or view them.
 *
 * @author      Micky Kyei
 * @version     4.0
 * @since       1.0
 */
public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    /**
     * Starts PostWorkout activity.
     * @param v the current view
     */
    public void postWorkout(View v) {
        Intent i = new Intent(this, PostWorkout.class);
        startActivity(i);
    }

    /**
     * Starts ViewWorkouts activity.
     * @param v the current view
     */
    public void viewWorkouts(View v) {
        Intent i = new Intent(this, ViewWorkouts.class);
        startActivity(i);
    }
}
