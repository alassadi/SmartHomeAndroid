package androidapp.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    private ImageButton indoorBtn, outdoorBtn, atticBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //init ui
        indoorBtn = findViewById(R.id.indoorButton);
        outdoorBtn = findViewById(R.id.outdoorButton);
        atticBtn = findViewById(R.id.atticButton);

    }

    public void indoorPressed(View view){
        Intent intent = new Intent(getApplicationContext(), IndoorActivity.class);
        startActivity(intent);
    }

    public void outdoorPressed(View view){
        Intent intent = new Intent(getApplicationContext(), OutdoorActivity.class);
        startActivity(intent);
    }

    public void atticPressed(View view){
        Intent intent = new Intent(getApplicationContext(), AtticActivity.class);
        startActivity(intent);
    }


}
