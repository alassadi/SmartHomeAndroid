package androidapp.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONException;
import org.json.JSONObject;


public class IndoorActivity extends AppCompatActivity {

    private static final String TAG = IndoorActivity.class.getSimpleName();
    //the ids are hardcoded here because the function on server side was not implemented/working properly
    private static final String ID_INDOOR = "K7F2O2YJLAWJxy4t9DI9";
    private static final String ID_INDOOR_LIGHT = "my9iXu6WvEgx5oNLLegs";
    private static final String ID_INDOOR_FIREALARM = "NjF7valDGmHOPZrF0S9O";
    private static final String ID_INDOOR_TEMP = "Dga7pWKFbWR0NquaWc9V";
    private static final String ID_INDOOR_DOOR = "eGtv1yzXNHPiOeCqdY81";
    private static final String ID_INDOOR_RADIATOR = "y3BVqxWaMZOmmcEPunb6";
    private static final String ID_INDOOR_WATERLEAK = "PbjTCF949vOXLB9idof0";
    private static final String ID_INDOOR_STOVE = "wHDkKc81N73z3JxKKiYJ";
    private static final String ID_INDOOR_WINDOW = "jUMNhUrIo4EqYX7RGcp5";


    //UI
    private static Switch indoorLightSwitch, fireAlarmSwitch, waterLeakageSwitch, doorSwitch, windowSwitch, stoveSwitch, radiatorSwitch;
    private static TextView tempValue;

    private ProgressBar progressBar;

    //firebase database
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    //firebase auth token
    private String userAuthToken;

    //broadcast receiver(not used)
    BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);

        //init ui
        indoorLightSwitch = findViewById(R.id.switchIndoorLights);
        fireAlarmSwitch = findViewById(R.id.switchFireAlarm);
        waterLeakageSwitch = findViewById(R.id.switchWaterLeakage);
        doorSwitch = findViewById(R.id.switchDoor);
        windowSwitch = findViewById(R.id.switchWindow);
        stoveSwitch = findViewById(R.id.switchStove);
        radiatorSwitch = findViewById(R.id.switchRadiator);
        tempValue = findViewById(R.id.tempValue);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //init broadcast receiver
        mBroadcastReceiver = new mBroadcastReceiver();

        //init firebase database
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("Devices");

        //init firebase auth for user auth token
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            userAuthToken = task.getResult().getToken();
                            Log.i(TAG, "user token: " + userAuthToken);

                            new IndoorActivity.updateUi(ID_INDOOR, userAuthToken).execute();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });

        //listen for changes in database
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "onDataChange: " + dataSnapshot.getValue());
                String room_id = dataSnapshot.child("room_id").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String value = dataSnapshot.child("value").getValue().toString();

                System.out.println("room_id: " + room_id);
                System.out.println("name: " + name);
                System.out.println("value: " + value);

                if (room_id.equals(ID_INDOOR)) {
                    switch (name) {
                        case "lamp inside":
                            if (value.equals("true")) {
                                indoorLightSwitch.setChecked(true);
                            } else {
                                indoorLightSwitch.setChecked(false);
                            }
                            break;
                        case "door":
                            if (value.equals("true")) {
                                doorSwitch.setChecked(true);
                            } else {
                                doorSwitch.setChecked(false);
                            }
                            break;

                        case "window":
                            if (value.equals("true")) {
                                windowSwitch.setChecked(true);
                            } else {
                                windowSwitch.setChecked(false);
                            }
                            break;

                        case "radiator":
                            if (value.equals("true")) {
                                radiatorSwitch.setChecked(true);
                            } else {
                                radiatorSwitch.setChecked(false);
                            }
                            break;

                        case "fire alarm":
                            if (value.equals("true")) {
                                fireAlarmSwitch.setChecked(true);
                            } else {
                                fireAlarmSwitch.setChecked(false);
                            }
                            break;

                        case "stove":
                            if (value.equals("true")) {
                                stoveSwitch.setChecked(true);
                            } else {
                                stoveSwitch.setChecked(false);
                            }
                            break;

                        case "water leak":
                            if (value.equals("true")) {
                                waterLeakageSwitch.setChecked(true);
                            } else {
                                waterLeakageSwitch.setChecked(false);
                            }
                            break;

                        case "temperature inside":
                            tempValue.setText(value);
                            break;
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        indoorLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_INDOOR_LIGHT, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_INDOOR_DOOR, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        windowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_INDOOR_WINDOW, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        radiatorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_INDOOR_RADIATOR, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });


        fireAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_INDOOR_FIREALARM, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONObject: + " + e.getMessage());
                }
            }
        });

        stoveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_INDOOR_STOVE, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        waterLeakageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_INDOOR_WATERLEAK, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register broadcast receiver for firebase cloud notifications (not used)
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("androidapp.smarthome.FcmService.onMessageReceived");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister broadcast when activity is paused(not used)
        unregisterReceiver(mBroadcastReceiver);
    }

    //Receives firebase cloud notification(not used)
    private class mBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            String notification = intent.getStringExtra("notification");
            Toast.makeText(getApplicationContext(), "notification: " + data + " + " + notification, Toast.LENGTH_LONG).show();
        }
    }

    public void updateDeviceState(String auth, String id, String value) throws JSONException {
        new HttpHandler().requestUpdateDeviceStatus(auth, id, value);
    }

    public static class updateUi extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        JSONObject tmpJson = new JSONObject();
        String roomid;
        String auth;

        public updateUi(String roomid, String auth) {
            this.roomid = roomid;
            this.auth = auth;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            json = new HttpHandler().requestGetRoomDevices(ID_INDOOR, auth);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                //update ui
                tmpJson = (JSONObject) json.get(ID_INDOOR_LIGHT);
                if (tmpJson.get("value").toString().equals("true")) {
                    indoorLightSwitch.setChecked(true);
                } else {
                    indoorLightSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_INDOOR_DOOR);
                if (tmpJson.get("value").toString().equals("true")) {
                    doorSwitch.setChecked(true);
                } else {
                    doorSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_INDOOR_WINDOW);
                if (tmpJson.get("value").toString().equals("true")) {
                    windowSwitch.setChecked(true);
                } else {
                    windowSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_INDOOR_RADIATOR);
                if (tmpJson.get("value").toString().equals("true")) {
                    radiatorSwitch.setChecked(true);
                } else {
                    radiatorSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_INDOOR_FIREALARM);
                if (tmpJson.get("value").toString().equals("true")) {
                    fireAlarmSwitch.setChecked(true);
                } else {
                    fireAlarmSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_INDOOR_STOVE);
                if (tmpJson.get("value").toString().equals("true")) {
                    stoveSwitch.setChecked(true);
                } else {
                    stoveSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_INDOOR_WATERLEAK);
                if (tmpJson.get("value").toString().equals("true")) {
                    waterLeakageSwitch.setChecked(true);
                } else {
                    waterLeakageSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_INDOOR_TEMP);
                tempValue.setText(tmpJson.get("value").toString());

            } catch (JSONException e) {
                Log.e(TAG, "JSONException: + " + e.getMessage());
            }
        }
    }


}
