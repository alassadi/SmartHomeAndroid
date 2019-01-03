package androidapp.smarthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.TextView;

public class OutdoorActivity extends AppCompatActivity {

    private String room_id, lamp_id, tempValue_id, burglarAlarm_id;

    private Switch lampSwitch, burglarAlarmSwitch;
    private TextView tempValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor);

        lampSwitch = findViewById(R.id.switchOutdoorLights);
        burglarAlarmSwitch = findViewById(R.id.switchBurglarAlarm);
        tempValue = findViewById(R.id.tempValue);

    }
}
