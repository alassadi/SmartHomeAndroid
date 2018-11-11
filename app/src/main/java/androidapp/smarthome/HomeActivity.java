package androidapp.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String API_URL = "https://api.myjson.com/bins/179fxm";



    //UI
    private Switch indoorLightSwitch, outdoorLightSwitch, fireAlarmSwitch, burglarAlarmSwitch;
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
        fireAlarmSwitch = findViewById(R.id.switchFireAlarm);
        burglarAlarmSwitch = findViewById(R.id.switchBurglarAlarm);

        tempValueText = findViewById(R.id.tempValueText);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        new requestGetJSON().execute();


        indoorLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    jsonObject.put("indoorLights", isChecked);

                    new requestPutJSON().execute();
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

                    new requestPutJSON().execute();
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        fireAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    jsonObject.put("fireAlarm", isChecked);
                    new requestPutJSON().execute();
                } catch (JSONException e){
                    Log.e(TAG, "error writing to JSONObject: + " + e.getMessage());
                }
            }
        });

        burglarAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    jsonObject.put("burglarAlarm", isChecked);
                    new requestPutJSON().execute();
                } catch (JSONException e){
                    Log.e(TAG, "error writing to JSONObject: + " + e.getMessage());
                }

            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        //@TODO refreshUi();


        //register broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("androidapp.smarthome.FcmService.onMessageReceived");
        BroadcastReceiver receiver = new mBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }


    //Receives notification FcmService
    private class mBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //update ui
            String data = intent.getStringExtra("data");
            String notification = intent.getStringExtra("notification");

            Toast.makeText(getApplicationContext(), "notification: " + data + " + " + notification, Toast.LENGTH_LONG).show();
        }
    }




    //Background thread to handle http GET request to server
    private class requestGetJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //send request to api url and read response
            HttpHandler httpHandler = new HttpHandler();
            jsonObject = httpHandler.reqGetJsonObject(API_URL);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                //update gui
                if (jsonObject != null){
                    outdoorLightSwitch.setChecked(jsonObject.getBoolean("outdoorLights"));
                    indoorLightSwitch.setChecked(jsonObject.getBoolean("indoorLights"));
                    fireAlarmSwitch.setChecked(jsonObject.getBoolean("fireAlarm"));
                    tempValueText.setText(jsonObject.get("temperature").toString());
                    burglarAlarmSwitch.setChecked(jsonObject.getBoolean("burglarAlarm"));
                    progressBar.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: + " + e.getMessage());
            }

        }

        @Override
        protected void onCancelled() {
            Log.e(TAG, "get request cancelled");
        }
    }

    //Background thread to handle http PUT request to server
    private class requestPutJSON extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            handler.reqPutJsonObject(API_URL, jsonObject);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            Log.e(TAG, "put request cancelled");
        }
    }


}
