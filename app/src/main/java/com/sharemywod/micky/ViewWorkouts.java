package com.sharemywod.micky;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sharemywod.micky.R;
import com.sharemywod.micky.SingleWorkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Renders a view that lets user view all workouts posted in backend.
 *
 * @author      Micky Kyei
 * @version     4.0
 * @since       2.0
 */
public class ViewWorkouts extends ListActivity {

    /**
     * The ArrayList in which all workouts will be saved.
     */
    ArrayList<JSONObject> workouts;

    /**
     * The ArrayList which will be shown in a ListView
     */
    ArrayList<String>parsedWorkouts;

    /**
     * ListView that contains workouts.
     */
    ListView listView;

    /**
     * Adapter for the ListView
     */
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);

        workouts = new ArrayList<>();
        parsedWorkouts = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.adaptertext, parsedWorkouts);
        listView = getListView();
        listView.setAdapter(adapter);
        new GetWorkoutsTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), SingleWorkout.class);
                i.putExtra("workout", workouts.get(position).toString());
                startActivity(i);
            }
        });

    }

    /**
     * ASyncTask that connects to backend and GETs all workouts
     */
    public class GetWorkoutsTask extends AsyncTask<Void, Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {

                URL url1 = new URL("http://178.62.102.13:8080/workouts/");

                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                JSONArray result = new JSONArray(sb.toString());
                System.out.println(result);
                for (int i = 0; i < result.length() ; i++) {
                    workouts.add(new JSONObject(result.get(i).toString()));
                }

                for (int i = 0; i < workouts.size() ; i++) {

                    String name = workouts.get(i).getString("name");
                    String desc =  workouts.get(i).getString("description");
                    SpannableString ss1 = new SpannableString(name);
                    ss1.setSpan(new RelativeSizeSpan(2f), 0,ss1.length()-1 , 0);
                    parsedWorkouts.add(ss1 + "\n" + desc);

                    System.out.println(parsedWorkouts.get(i));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.notifyDataSetChanged();
        }
    }
}
