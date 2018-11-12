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

        progressBar.setVisibility(View.GONE);

        //updateUi();


        indoorLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    //@TODO get user id from firebase authentication when logged in.
                    jsonObject = new JSONObject();
                    jsonObject.put("id", "my9iXu6WvEgx5oNLLegs");
                    jsonObject.put("enabled", isChecked);

                    new HttpHandler().requestUpdateDeviceStatus(jsonObject);

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
                    //new request
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
                    //new request
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
                    //new request

                } catch (JSONException e){
                    Log.e(TAG, "error writing to JSONObject: + " + e.getMessage());
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //@TODO updateUi();

        //register broadcast receiver for firebase cloud notifications
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("androidapp.smarthome.FcmService.onMessageReceived");
        BroadcastReceiver receiver = new mBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }


    //Receives firebase cloud notification
    private class mBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //@TODO updateUi();
            String data = intent.getStringExtra("data");
            String notification = intent.getStringExtra("notification");
            Toast.makeText(getApplicationContext(), "notification: " + data + " + " + notification, Toast.LENGTH_LONG).show();
        }
    }



    /*
    //requires getDeviceStatus endpoint before implementation can be done
    private class updateUi extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //getDeviceStatus
            //JSONObject jsonObject = new JSONObject();
            //HttpHandler httpHandler = new HttpHandler();
            //jsonObject = httpHandler.requestGetDeviceStatus();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                //update ui
                    indoorLightSwitch.setChecked(jsonObject.getBoolean("enabled"));

//                if (jsonObject != null) {
//                    outdoorLightSwitch.setChecked(jsonObject.getBoolean("outdoorLights"));
//                    fireAlarmSwitch.setChecked(jsonObject.getBoolean("fireAlarm"));
//                    tempValueText.setText(jsonObject.get("temperature").toString());
//                    burglarAlarmSwitch.setChecked(jsonObject.getBoolean("burglarAlarm"));
//                    progressBar.setVisibility(View.GONE);
//                }
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: + " + e.getMessage());
            }
        }
    }
    */

}
