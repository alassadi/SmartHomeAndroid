package androidapp.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegActivity extends AppCompatActivity {

    private Button confirmBtn;
    private TextView firstName, lastName, address, city, dateOfBirth, email, gender, phone, postCode;
    private EditText firstNameET, lastNameET, addressET, cityET, dateOfBirthET, emailET, genderET, phoneET, postCodeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // text views
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        dateOfBirth = findViewById(R.id.bday);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        phone = findViewById(R.id.phone);
        postCode = findViewById(R.id.postalCode);

        // text editors
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        addressET = findViewById(R.id.addressEditText);
        cityET = findViewById(R.id.cityEditText);
        dateOfBirthET = findViewById(R.id.bdayEditText);
        emailET = findViewById(R.id.emailEditText);
        genderET = findViewById(R.id.genderEditText);
        phoneET = findViewById(R.id.phoneEditText);
        postCodeET = findViewById(R.id.postalCodeEditText);

        // button
        confirmBtn = findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save values into database
                // then go to login page or home page
                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                startActivity(intent);
            }
        });

    }
}
