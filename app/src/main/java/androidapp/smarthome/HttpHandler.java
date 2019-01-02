package androidapp.smarthome;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {

    }


    //getDeviceStatus endpoint not working yet
    public JSONObject requestGetDeviceStatus(JSONObject jsonObject) {
        try {
            //send GET request
            URL url = new URL("https://us-central1-smarthome-3c6b9.cloudfunctions.net/getDeviceStatus");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(jsonObject.toString());
            out.flush();
            out.close();

            jsonObject = new JSONObject(connection.getResponseMessage());

            Log.i(TAG, "GET device status " + jsonObject.toString());
            Log.i(TAG, "server status: " + connection.getResponseCode());
            Log.i(TAG, "server msg: " + connection.getResponseMessage());

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return jsonObject;

    }


    public void requestUpdateToken(JSONObject jsonToken) {
        new taskUpdateToken().execute(jsonToken);
    }

    public void requestUpdateDeviceStatus(JSONObject jsonObject, String auth) {
        new taskUpdateDeviceStatus(auth).execute(jsonObject);
    }

    public void requestCreateUser(JSONObject jsonObject){
        new taskCreateUser().execute(jsonObject);
    }


    private static class taskUpdateToken extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {

            try {
                //send POST request
                URL url = new URL("https://us-central1-smarthome-3c6b9.cloudfunctions.net/updateFcmToken");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(jsonObjects[0].toString());
                out.flush();
                out.close();

                Log.i(TAG, "post updateToken " + jsonObjects[0]);
                Log.i(TAG, "server status: " + connection.getResponseCode());
                Log.i(TAG, "server msg: " + connection.getResponseMessage());

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }


            return null;
        }
    }

    private static class taskUpdateDeviceStatus extends AsyncTask<JSONObject, String, Void> {
        String auth;
        public taskUpdateDeviceStatus(String auth){
            this.auth = auth;
            System.out.println("task auth: "+ auth);
        }

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {

            try {
                //send POST request
                URL url = new URL("https://europe-west1-smarthome-3c6b9.cloudfunctions.net/device");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");

                connection.setRequestProperty("Content-type", "application/json");
                //connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + auth);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(jsonObjects[0].toString());
                out.flush();
                out.close();

                Log.i(TAG, "post updateDeviceStatus: " + jsonObjects[0]);
                Log.i(TAG, "server status: " + connection.getResponseCode());
                Log.i(TAG, "server msg: " + connection.getResponseMessage());

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return null;
        }
    }

    public static class taskCreateUser extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {

            try {
                // send post request
                URL url = new URL("https://europe-west1-smarthome-3c6b9.cloudfunctions.net/createUser");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(jsonObjects[0].toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                Log.i(TAG, "post createUser: " + jsonObjects[0]);
                Log.i(TAG, "server status: " + connection.getResponseCode());
                Log.i(TAG, "server msg: " + connection.getResponseMessage());

            } catch (MalformedURLException e){
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e){
                Log.e(TAG, "IOException: " + e.getMessage());
            }

            return null;
        }
    }

}
