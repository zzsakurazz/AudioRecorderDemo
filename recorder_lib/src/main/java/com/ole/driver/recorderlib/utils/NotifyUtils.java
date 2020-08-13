package com.ole.driver.recorderlib.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * @author Zong
 * Created on 2020/4/23
 * Describe:
 */
public class NotifyUtils {


    private NotifyUtils() {
    }

    public static Notification getNotification(Context context, int icId, String title, String des) {
        String channelId = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = "default_channl_id";
            NotificationChannel chan = new NotificationChannel(channelId, "default_channl_name", NotificationManager.IMPORTANCE_DEFAULT);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManagerCompat.from(context).createNotificationChannel(chan);
        }

        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icId)
                .setContentTitle(title)
                .setContentText(des)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
    }



}
