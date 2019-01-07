package androidapp.smarthome;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {

    }


    //getDeviceStatus endpoint not working yet
    public JSONObject requestGetRoomDevices(String roomid, String auth) {
        JSONObject jsonObject = new JSONObject();
        try {
            StringBuffer response = new StringBuffer();
            //send GET request
            System.out.println("roomid: " + roomid);
            System.out.println("auth: "+ auth);
            URL url = new URL("https://europe-west1-smarthome-3c6b9.cloudfunctions.net/rooms/" + roomid + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + auth);

            int status = connection.getResponseCode();
            if (status != 200) {
                //throw new IOException("requestGetRoomDevices failed");
                System.out.println("bad status");
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            Log.i(TAG, "response: " + response);
             jsonObject = new JSONObject(response.toString());

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

    public void requestUpdateDeviceStatus(String auth, String id, String value) throws JSONException {
        new taskUpdateDeviceStatus(auth, id, value).execute();
    }

    public void requestCreateUser(JSONObject jsonObject) {
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

    private static class taskUpdateDeviceStatus extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String auth;
        String id;
        String value;

        public taskUpdateDeviceStatus(String auth, String id, String value) throws JSONException {
            this.auth = auth;
            this.id = id;
            this.value = value;
            json.put("value", value);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                //send POST request
                URL url = new URL("https://europe-west1-smarthome-3c6b9.cloudfunctions.net/devices/" + id + "/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + auth);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(json.toString());
                out.flush();
                out.close();

                Log.i(TAG, "post updateDeviceStatus: " + value);
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
                URL url = new URL("https://europe-west1-smarthome-3c6b9.cloudfunctions.net/users/");
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

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }

            return null;
        }
    }

}
