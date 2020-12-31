package com.example.hp.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.hp.test.notice_recyclerview.CHANNEL_ID;

public class FirebaseNotification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            if (title != null) {
                if (title.equals("Notice From GNC")) {
                    Intent intent = new Intent(this, notice_recyclerview.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);


                    NotificationManager mNotificationmgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (mNotificationmgr != null) {
                        mNotificationmgr.notify(0, mBuilder.build());
                    }
                } else if (title.equals("Event From GNC")) {
                    Intent intent = new Intent(this, event_recyclerview.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManager mNotificationmgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (mNotificationmgr != null) {
                        mNotificationmgr.notify(1, mBuilder.build());
                    }
                } else {
                    Intent intent = new Intent(this, maitainance.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManager mNotificationmgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (mNotificationmgr != null) {
                        mNotificationmgr.notify(2, mBuilder.build());
                    }
                }
            }

        }
    }
}




