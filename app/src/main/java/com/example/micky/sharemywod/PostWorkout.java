package com.example.micky.sharemywod;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostWorkout extends AppCompatActivity {

    ArrayList<JSONObject> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_workout);
        exercises = new ArrayList<>();


    }

    public void addExercise(View v) {



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.add_exercise, null))
                // Add action buttons
                .setPositiveButton("Add exercise", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            EditText name =
                                    (EditText) ((AlertDialog) dialog).findViewById(R.id.exerciseName);
                            EditText amount =
                                    (EditText) ((AlertDialog) dialog).findViewById(R.id.exerciseAmount);
                            EditText sets =
                                    (EditText) ((AlertDialog) dialog).findViewById(R.id.exerciseSets);
                            EditText additionalInfo =
                                    (EditText) ((AlertDialog) dialog).findViewById(R.id.additionalInfo);
                            RadioGroup radioGroup =
                                    (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.radioButtons);
                            int selected =
                                    radioGroup.getCheckedRadioButtonId();
                            RadioButton rButton =
                                    (RadioButton) ((AlertDialog) dialog).findViewById(selected);


                            JSONObject newExercise = new JSONObject();
                            newExercise.put("name", name.getText());
                            newExercise.put("amount", Integer.parseInt(amount.getText().toString()));
                            newExercise.put("sets", Integer.parseInt(sets.getText().toString()));
                            newExercise.put("additionalInfo", additionalInfo.getText());
                            newExercise.put("value", rButton.getText());

                            System.out.println(newExercise);
                            exercises.add(newExercise);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create().show();



    }


}
