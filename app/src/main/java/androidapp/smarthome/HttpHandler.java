package androidapp.smarthome;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){

    }

    public JSONObject reqGetJsonObject(String reqUrl){
        JSONObject jsonObject = null;

        try{
            //HTTP GET request
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //read response
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            jsonObject = new JSONObject(reader.readLine());
            in.close();

            Log.i(TAG, "server get JSON data: " + jsonObject.toString());
            Log.i(TAG, "server status: " + connection.getResponseCode());
            Log.i(TAG, "server msg: " + connection.getResponseMessage());


        }catch (MalformedURLException e) {
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

    public void reqPutJsonObject(String reqUrl, JSONObject jsonObject){
        try {
            //send PUT request
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");

            //set header info to inform server about the type of content
            connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            //write request
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(jsonObject.toString());
            out.flush();
            out.close();

            Log.i(TAG, "server put JSON data: " + jsonObject.toString());
            Log.i(TAG, "server status: " + connection.getResponseCode());
            Log.i(TAG, "server msg: " + connection.getResponseMessage());

        }catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public boolean verifyEmail(String email, String password){
        //@TODO verifyEmailRequest
        return true;
    }

}
