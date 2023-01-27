package com.gap.mobigpk1;

import static com.gap.mobigpk1.nNotification.CHANNEL_DESC;
import static com.gap.mobigpk1.nNotification.CHANNEL_ID;
import static com.gap.mobigpk1.nNotification.CHANNEL_NAME;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotifyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Map<String, String> extraData = remoteMessage.getData();

//            String target = extraData.get("target");
            String category = extraData.get("category");

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(body)
//                        .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent intent=null;
            if(category.equals("Notification")) {
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("targetFragment", "Notification");
            }
            PendingIntent pendingIntent=PendingIntent.getActivity(this,10,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            int id=(int)System.currentTimeMillis();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(CHANNEL_DESC);
               notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(id,mBuilder.build());

        }
    }
}
