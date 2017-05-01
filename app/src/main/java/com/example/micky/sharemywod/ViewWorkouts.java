package com.example.micky.sharemywod;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewWorkouts extends ListActivity {

    ArrayList<JSONObject> workouts;
    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);

        workouts = new ArrayList<>();

        adapter = new ArrayAdapter<JSONObject>(this,R.layout.adaptertext, workouts);
        listView = getListView();
        listView.setAdapter(adapter);
        new GetWorkoutsTask().execute();

    }

    public class GetWorkoutsTask extends AsyncTask<Void, Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {

                URL url1 = new URL("http://10.0.2.2:8080/workouts");

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
