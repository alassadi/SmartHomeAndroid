package marbac.smarthome;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String API_URL = "https://api.myjson.com/bins/175ch0";

    //UI
    private Switch indoorLightSwitch, outdoorLightSwitch;
    private TextView tempValueText;
    private ProgressBar progressBar;

    //stores current JSONobject
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        indoorLightSwitch = findViewById(R.id.switchIndoorLights);
        outdoorLightSwitch = findViewById(R.id.switchOutdoorLights);
        tempValueText = findViewById(R.id.tempValueText);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        new requestGetJSON().execute();

        indoorLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    jsonObject.put("indoorLights", isChecked);

                    new requestPostJSON().execute();
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        outdoorLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    jsonObject.put("outdoorLights", isChecked);

                    new requestPostJSON().execute();
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

    }

    //Background thread to handle http request to server
    private class requestGetJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //send request to api url and read response
            HttpHandler httpHandler = new HttpHandler();
            String jsonString = httpHandler.reqGetJsonString(API_URL);

            if (jsonString != null) {
                try {
                    //store json
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Failed to read JSON from server");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                //update gui
                outdoorLightSwitch.setChecked(jsonObject.getBoolean("outdoorLights"));
                indoorLightSwitch.setChecked(jsonObject.getBoolean("indoorLights"));
                tempValueText.setText(jsonObject.get("temperature").toString());
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: + " + e.getMessage());
            }

        }

        @Override
        protected void onCancelled() {
            Log.e(TAG, "get request cancelled");
        }
    }

    private class requestPostJSON extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            handler.reqPutState(API_URL, jsonObject);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            Log.e(TAG, "post request cancelled");
        }
    }
}
