package com.example.micky.sharemywod;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PostWorkout extends ListActivity {

    ArrayList<JSONObject> exercises;
    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_workout);
        exercises = new ArrayList<>();

        adapter = new ArrayAdapter<JSONObject>(this,R.layout.adaptertext, exercises);
        listView = getListView();
        listView.setAdapter(adapter);

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
                            adapter.notifyDataSetChanged();
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

    public void shareWorkout(View v) {

        JSONObject workout = new JSONObject();
        try {
            EditText name = (EditText) findViewById(R.id.nameField);
            EditText description = (EditText) findViewById(R.id.info);

            workout.put("name", name.getText());
            workout.put("description", description.getText());
            workout.put("points", 0);
            System.out.println("1");
            workout.put("exercises", new JSONArray(exercises));
            System.out.println("2");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new ShareTask().execute(workout);
    }


    public class ShareTask extends AsyncTask<JSONObject, Void,Integer> {

        @Override
        protected Integer doInBackground(JSONObject... params) {

            int result = 400;
            System.out.println("doInBackGround(): " + params[0].toString());
            try {
                URL url1 = new URL("http://10.0.2.2:8080/workouts");

                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(params[0].toString());
                writer.flush();
                writer.close();
                outputStream.close();

                System.out.println(urlConnection.getResponseCode());
                result = urlConnection.getResponseCode();
                urlConnection.disconnect();

            }catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);if(result == 200) {
                Toast.makeText(PostWorkout.this, "Workout shared",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(PostWorkout.this, "Sharing failed",
                        Toast.LENGTH_LONG).show();
            }

            exercises.clear();
            adapter.notifyDataSetChanged();
            EditText name = (EditText) findViewById(R.id.nameField);
            EditText description = (EditText) findViewById(R.id.info);
            name.setText("");
            description.setText("");

        }

    }
}
