package androidapp.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    ImageButton indoorBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        indoorBtn = findViewById(R.id.indoorButton);

    }

    public void indoorPressed(View view){
        Intent intent = new Intent(getApplicationContext(), IndoorActivity.class);
        startActivity(intent);
    }


}
