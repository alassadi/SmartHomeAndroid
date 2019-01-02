package androidapp.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegActivity extends AppCompatActivity {

    // components
    private Button confirmBtn;
    private EditText firstNameET, lastNameET, addressET, cityET, dateOfBirthET, emailET, genderET, phoneET, postCodeET, passwordET, countryET;

    // variables
    private String firstName, lastName, address, city, dateOfBirth, email, gender, phone, postCode, password, country;

    // jsonObject
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // text editors
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        addressET = findViewById(R.id.addressEditText);
        cityET = findViewById(R.id.cityEditText);
        countryET = findViewById(R.id.countryEditText);
        dateOfBirthET = findViewById(R.id.bdayEditText);
        emailET = findViewById(R.id.emailEditText);
        genderET = findViewById(R.id.genderEditText);
        phoneET = findViewById(R.id.phoneEditText);
        postCodeET = findViewById(R.id.postalCodeEditText);
        passwordET = findViewById(R.id.passwordEditText);

        // button
        confirmBtn = findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // save value from text editors
                firstName = firstNameET.getText().toString();
                lastName = lastNameET.getText().toString();
                address = addressET.getText().toString();
                city = cityET.getText().toString();
                country = countryET.getText().toString();
                dateOfBirth = dateOfBirthET.getText().toString();
                email = emailET.getText().toString();
                gender = genderET.getText().toString();
                phone = phoneET.getText().toString();
                postCode = postCodeET.getText().toString();
                password = passwordET.getText().toString();

                if (checkValues()){
                    createJSONObject();
                }

                // NOW CREATE HTTP HANDLER STUFF

                // save values into database

                // go to login page
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

    }

    // check for valid inputs
    public boolean checkValues(){
        if (firstName.equals("") || lastName.equals("") || address.equals("") || city.equals("") || dateOfBirth.equals("") || country.equals("")
                || email.equals("") || gender.equals("") || phone.equals("") || postCode.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Please type your information", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.contains("@")){
            Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 5){
            Toast.makeText(getApplicationContext(), "Password need to be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void createJSONObject(){
        try {
            // create JSONObject and add values
            jsonObject = new JSONObject();
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("phone", phone);
            jsonObject.put("address", address);
            jsonObject.put("postal_code", postCode);
            jsonObject.put("city", city);
            jsonObject.put("country", country);
            jsonObject.put("date_of_birth", dateOfBirth);
            jsonObject.put("gender", gender);


        } catch (JSONException e){
            System.out.println("error - creating jsonObject");
            e.printStackTrace();
        }
    }
}
