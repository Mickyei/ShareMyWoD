package com.sharemywod.micky;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sharemywod.micky.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Renders a view that lets user post a workout to backend.
 *
 * @author      Micky Kyei
 * @version     4.0
 * @since       2.0
 */
public class PostWorkout extends ListActivity {

    /**
     * Contains the exercises in JSON.
     * These will be sent to the backend.
     */
    ArrayList<JSONObject> exercises;

    /**
     * Contains the exercises in String.
     * These will be shown in the app.
     */
    ArrayList<String> parsedExercises;

    /**
     * The ListView for the exercises.
     */
    ListView listView;

    /**
     * Adapter for the ListView
     */
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_workout);
        exercises = new ArrayList<>();
        parsedExercises = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,R.layout.adaptertext, parsedExercises);
        listView = getListView();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exercises.remove(position);
                parsedExercises.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

    }


    /**
     * Adds an exercise to parsedExercises and exercises.
     * Also notifies ArrayAdapter of the changes.
     * @param v current View
     */
    public void addExercise(View v) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.add_exercise, null))
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
                            parsedExercises.add("Exercise: " + newExercise.get("name") + "\n" +
                                    "Sets: " + newExercise.get("sets") + " of " + newExercise.get("amount")
                                    + " " + newExercise.get("value")  + "\n" +
                                    newExercise.get("additionalInfo") );
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

    /**
     * Creates a JSONObject and shares it to the backend.
     * Uses a ASyncTask.
     *
     * @param v current View
     */
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


    /**
     * ASyncTask that connects to backend and POSTs a JSONObject
     */
    public class ShareTask extends AsyncTask<JSONObject, Void,Integer> {

        @Override
        protected Integer doInBackground(JSONObject... params) {

            int result = 400;
            System.out.println("doInBackGround(): " + params[0].toString());
            try {
                URL url1 = new URL("http://178.62.102.13:8080/workouts/");

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
            super.onPostExecute(result);
            if(result == 200) {
                Toast.makeText(PostWorkout.this, "Workout shared",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(PostWorkout.this, "Sharing failed",
                        Toast.LENGTH_LONG).show();
            }

            exercises.clear();
            parsedExercises.clear();
            adapter.notifyDataSetChanged();
            EditText name = (EditText) findViewById(R.id.nameField);
            EditText description = (EditText) findViewById(R.id.info);
            name.setText("");
            description.setText("");
            Intent back = new Intent(getApplicationContext() ,MainScreen.class);
            startActivity(back);

        }

    }
}
