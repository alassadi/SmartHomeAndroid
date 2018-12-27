package androidapp.smarthome;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    //UI
    private Button loginButton, regButton;
    private ProgressBar progressBar;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        regButton = findViewById(R.id.regButton);
        progressBar = findViewById(R.id.progressBar);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        progressBar.setVisibility(View.GONE);

        loginButton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to reg page
                Intent intent = new Intent(getApplicationContext(), RegActivity.class);
                startActivity(intent);
            }
        });
    }



    public void attemptLogin(){

        //reset errors
        emailEditText.setError(null);
        passwordEditText.setError(null);

        String email, password;
        boolean cancel = false;

        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        

        /*
        //disabled for faster testing

        if (!(email.contains("@") && email.length() > 5)){
            //email invalid
            emailEditText.setError("email invalid, try again");
            cancel = true;
        }

        if (!(password.length() > 4)){
            passwordEditText.setError("password too short, try again");
            cancel = true;
        }


        if (cancel){

        }else {
            progressBar.setVisibility(View.VISIBLE);
            TaskUserLogin taskUserLogin = new TaskUserLogin(email, password);
            taskUserLogin.execute((Void) null);
        }
        */

        progressBar.setVisibility(View.VISIBLE);
        TaskUserLogin taskUserLogin = new TaskUserLogin(email, password);
        taskUserLogin.execute((Void) null);

    }



    public class TaskUserLogin extends AsyncTask<Void, Void, Boolean>{
        private final String email;
        private final String password;

        public TaskUserLogin(String email, String password){
            this.email = email;
            this.password = password;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.i(TAG, "requestVerifyEmailAndPassword: " + email);
            /*
            send login request with HttpHandler
            return true if login was verified
            if(httpHandler.verifyEmail(email, password)){return true;}

            */

            try{
                //simulate login verification
                Thread.sleep(2000);
            }catch (InterruptedException e){
                Log.e(TAG, "InterruptedException: " + e.getMessage());
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean verified) {

            if (verified){
                Log.i(TAG, "email verified: " + email);
                //start new activity
                progressBar.setVisibility(View.GONE);
                // FOR TRY: CHANGE BACK TO HomeActivity.class
                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                /*
                intent.putExtra("user", email);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                */
                startActivity(intent);
                //finish() removes activity from activityStack
                //finish();
            }else {
                Log.i(TAG, "email unverified: " + email);
                passwordEditText.setError("Password is incorrect, try again");
                passwordEditText.requestFocus();

            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "login cancelled", Toast.LENGTH_SHORT);
        }
    }


}
