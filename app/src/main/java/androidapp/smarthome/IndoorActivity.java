package androidapp.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class IndoorActivity extends AppCompatActivity {

    private static final String TAG = IndoorActivity.class.getSimpleName();

    private String userAuthToken;

    //UI
    private Switch indoorLightSwitch, outdoorLightSwitch, fireAlarmSwitch, burglarAlarmSwitch;
    private TextView tempValueText;
    private ProgressBar progressBar;

    //firebase database
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    //stores current JSONobject
    JSONObject jsonObject;

    //broadcast receiver
    BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);


        //init ui
        indoorLightSwitch = findViewById(R.id.switchIndoorLights);
        //outdoorLightSwitch = findViewById(R.id.switchOutdoorLights);
        fireAlarmSwitch = findViewById(R.id.switchFireAlarm);
        burglarAlarmSwitch = findViewById(R.id.switchBurglarAlarm);
        //tempValueText = findViewById(R.id.tempValueText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //init broadcast receiver
        mBroadcastReceiver = new mBroadcastReceiver();

        //init firebase database
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Devices/my9iXu6WvEgx5oNLLegs/enabled");

        //init firebase auth for user auth token
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            userAuthToken = task.getResult().getToken();
                            Log.i(TAG, "user token: " + userAuthToken);
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });

        //temporary solution used instead of cloud messaging
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("true")){
                    indoorLightSwitch.setChecked(true);
                } else if(dataSnapshot.getValue().toString().equals("false")){
                    indoorLightSwitch.setChecked(false);
                }
                Log.i(TAG, "onDataChange: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //updateUi();

        indoorLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    //@TODO get user id from firebase authentication when logged in.
                    jsonObject = new JSONObject();
                    jsonObject.put("id", "my9iXu6WvEgx5oNLLegs");
                    jsonObject.put("enabled", isChecked);

                    new HttpHandler().requestUpdateDeviceStatus(jsonObject, userAuthToken);

                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        /*
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
                } catch (JSONException e) {
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

                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONObject: + " + e.getMessage());
                }
            }
        });
        */

        // remove later
        //burglarAlarmSwitch.setVisibility(View.INVISIBLE);
        //fireAlarmSwitch.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //@TODO updateUi();

        //register broadcast receiver for firebase cloud notifications
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("androidapp.smarthome.FcmService.onMessageReceived");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister broadcast when activity is paused
        unregisterReceiver(mBroadcastReceiver);
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

//not required for first presentation
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
