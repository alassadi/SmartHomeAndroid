package androidapp.smarthome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

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

public class OutdoorActivity extends AppCompatActivity {
    private static final String TAG = OutdoorActivity.class.getSimpleName();

    //the ids are hardcoded here because the function on server side was not implemented/working properly
    private static final String ID_OUTDOOR = "xXKPpHKTbMWCXDluthqz";
    private static final String ID_OUTDOOR_LIGHT = "HrWTumcyQgNbcai78KAv";
    private static final String ID_OUTDOOR_TEMP = "ORr9T5abPtJpeV6XdPw8";
    private static final String ID_OUTDOOR_BURGLAR = "ICYkFxoI0x2ng9NzGark";

    private static Switch lampSwitch, burglarAlarmSwitch;
    private static TextView tempValue;

    //firebase database
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    //firebase auth token
    private String userAuthToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor);

        lampSwitch = findViewById(R.id.switchOutdoorLights);
        burglarAlarmSwitch = findViewById(R.id.switchBurglarAlarm);
        tempValue = findViewById(R.id.tempValue);

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

                            new OutdoorActivity.updateUi(ID_OUTDOOR, userAuthToken).execute();
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

                if (room_id.equals(ID_OUTDOOR)) {
                    switch (name) {
                        case "lamp outside":
                            if (value.equals("true")) {
                                lampSwitch.setChecked(true);
                            } else {
                                lampSwitch.setChecked(false);
                            }
                            break;
                        case "burglar alarm":
                            if (value.equals("true")) {
                                burglarAlarmSwitch.setChecked(true);
                            } else {
                                burglarAlarmSwitch.setChecked(false);
                            }
                            break;

                        case "temperature outside":
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

        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_OUTDOOR_LIGHT, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });

        burglarAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateDeviceState(userAuthToken, ID_OUTDOOR_BURGLAR, String.valueOf(isChecked));
                } catch (JSONException e) {
                    Log.e(TAG, "error writing to JSONobject: + " + e.getMessage());
                }
            }
        });
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
            json = new HttpHandler().requestGetRoomDevices(ID_OUTDOOR, auth);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                //update ui
                tmpJson = (JSONObject) json.get(ID_OUTDOOR_LIGHT);
                if (tmpJson.get("value").toString().equals("true")) {
                    lampSwitch.setChecked(true);
                } else {
                    lampSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_OUTDOOR_BURGLAR);
                if (tmpJson.get("value").toString().equals("true")) {
                    burglarAlarmSwitch.setChecked(true);
                } else {
                    burglarAlarmSwitch.setChecked(false);
                }

                tmpJson = (JSONObject) json.get(ID_OUTDOOR_TEMP);
                tempValue.setText(tmpJson.get("value").toString());

            } catch (JSONException e) {
                Log.e(TAG, "JSONException: + " + e.getMessage());
            }
        }
    }
}
