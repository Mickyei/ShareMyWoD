package com.sharemywod.micky;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sharemywod.micky.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Renders a view that lets the user view a single workout.
 *
 * @author      Micky Kyei
 * @version     4.0
 * @since       3.0

 */
public class SingleWorkout extends ListActivity {

    /**
     * The workout that is received as an Intent.
     */
    JSONObject workout;

    /**
     * JSONArray extracted from workout.
     */
    JSONArray exercises;

    /**
     * JSONArray exercises parsed to String.
     */
    ArrayList<String> parsedE;

    /**
     * Listview for all the exercises.
     */
    ListView listView;

    /**
     * ArrayAdapter for the ListView.
     */
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_workout);


        try {
            parsedE = new ArrayList<>();
            workout = new JSONObject(getIntent().getStringExtra("workout"));

            String tmp = workout.get("exercises").toString();
            System.out.println(tmp);
            exercises = new JSONArray(tmp);

            for (int i = 0; i < exercises.length(); i++) {
                JSONObject tmpJson = new JSONObject(exercises.get(i).toString());
                //parsedExercises.add(tmpJson);
                String exerciseName = tmpJson.getString("name");
                String exerciseSets = Integer.toString(tmpJson.getInt("sets"));
                String exerciseAmount = Integer.toString(tmpJson.getInt("amount"));
                String exerciseValue = tmpJson.getString("value");
                String exerciseInfo = tmpJson.getString("additionalInfo");
                String wholeExercise = "Exercise: " + exerciseName + "\n" +
                        "Sets: " + exerciseSets + " of " + exerciseAmount + " " + exerciseValue + "\n" +
                        exerciseInfo;
                parsedE.add(wholeExercise);
            }
            adapter = new ArrayAdapter<String>(this,R.layout.adaptertext, parsedE);
            listView = getListView();
            listView.setAdapter(adapter);

            TextView name = (TextView) findViewById(R.id.singleWorkoutName);
            name.setText(workout.getString("name"));

            TextView desc = (TextView) findViewById(R.id.singleWorkoutDesc);
            desc.setText(workout.getString("description"));
        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
