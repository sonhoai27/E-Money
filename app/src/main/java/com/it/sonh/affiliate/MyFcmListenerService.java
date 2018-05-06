package com.it.sonh.affiliate;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by sonho on 3/19/2018.
 */

public class MyFcmListenerService extends FirebaseMessagingService {
    private String TAG = "MYFBM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
