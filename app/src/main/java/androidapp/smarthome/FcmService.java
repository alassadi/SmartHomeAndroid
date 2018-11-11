package androidapp.smarthome;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmService extends FirebaseMessagingService {

    private static final String TAG = FcmService.class.getSimpleName();

    public FcmService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        String data = null;
        String notification = null;

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            data = remoteMessage.getData().toString();

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notification = remoteMessage.getNotification().getBody();
        }

        //broadcasts notification to HomeActivity
        Intent intent = new Intent();
        intent.putExtra("data", data);
        intent.putExtra("notification", notification);
        intent.setAction("androidapp.smarthome.FcmService.onMessageReceived");
        sendBroadcast(intent);

    }

}
