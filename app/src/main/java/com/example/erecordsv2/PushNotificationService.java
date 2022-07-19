package com.example.erecordsv2;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();
        String Channel_ID = "MESSAGE";
        CharSequence name = "Message Notification";

         NotificationChannel channel = new NotificationChannel(
                Channel_ID,
                name,
                NotificationManager.IMPORTANCE_HIGH);

         getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Context context;
        Notification.Builder notification = new Notification.Builder(this,Channel_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1,notification.build());

        super.onMessageReceived(remoteMessage);
    }
}
