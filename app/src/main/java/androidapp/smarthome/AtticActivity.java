package androidapp.smarthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.TextView;

public class AtticActivity extends AppCompatActivity {

    // room and device ID
    private String room_id, temp_id;

    // UI
    private TextView tempValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attic);

        // init ui
        tempValue = findViewById(R.id.tempValue);
        tempValue.setText("133");
    }
}
