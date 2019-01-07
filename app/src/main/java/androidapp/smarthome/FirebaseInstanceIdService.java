package androidapp.smarthome;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    private static final String TAG = FirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        //Token used for receiving cloud messages from server
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        try{
            JSONObject jsonToken = new JSONObject();

            //@TODO get uid, unit_uid from database
            jsonToken.put("uid", "uqXioj02nfXVve2zmmOlOwuHrkZ2");
            jsonToken.put("unit_uid", "-LR28TKC-j9s1GRI-mqy");
            jsonToken.put("fcm_token", refreshedToken);
            new HttpHandler().requestUpdateToken(jsonToken);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
