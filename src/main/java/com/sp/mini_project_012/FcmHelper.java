package com.sp.mini_project_012;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FcmHelper {
    private static final String TAG = "FcmHelper";

    public static void sendNotification(String token, Map<String, String> data) {
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token)
                .setMessageId(Integer.toString(data.hashCode()))
                .addData("title", data.get("title"))
                .addData("message", data.get("message"))
                .build());
        Log.d(TAG, "Notification sent to token: " + token);
    }
}
