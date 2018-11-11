package androidapp.smarthome;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    private static final String TAG = FirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        //Token used for receiving cloud messages from server
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        System.out.println("REFRESH TOKEN: " + refreshedToken);

        //@TODO update database user device token
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }


}
