package com.it.sonh.affiliate;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sonho on 3/19/2018.
 */

public class MyInstanceIdListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("FCMAPP", "Token is "+refreshToken);
    }
}
