package marbac.smarthome;

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

    public String reqGetJsonString(String reqUrl){
        String response = null;

        try{
            //send GET request
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //read response
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(in);

            Log.i(TAG, "server response: " + response);

        }catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public void reqPutState(String reqUrl, JSONObject jsonObject){
        try {
            //send PUT request
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            //set header info to inform server about the type of content
            connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setDoOutput(true);
            //connection.setDoInput(true);

            //write request
            Log.i(TAG, "put json data: " + jsonObject.toString());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(jsonObject.toString());
            out.flush();
            out.close();

            Log.i(TAG, "response status: " + connection.getResponseCode());
            Log.i(TAG, "response msg: " + connection.getResponseMessage());

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
        return true;
    }

    private String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try{
            while ((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        }catch (IOException e){
            e.printStackTrace();
            Log.e(TAG, "IOexception: " + e.getMessage());
        }finally {
            try {
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "IOexception: " + e.getMessage());
            }
        }
        return sb.toString();
    }
}
