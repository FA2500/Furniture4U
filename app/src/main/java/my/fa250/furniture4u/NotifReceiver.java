package my.fa250.furniture4u;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

public class NotifReceiver extends BroadcastReceiver {
    NotificationManager notifMan;
    Notification notif;

    public static String NOTIFICATIONID = "1001";
    public static String NOTIFICAION = "pushNotification";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        notifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notif = intent.getParcelableExtra(NOTIFICAION);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notifChan = new NotificationChannel(NOTIFICATIONID,"pushNotification",NotificationManager.IMPORTANCE_HIGH);
            assert notifMan != null;
            notifMan.createNotificationChannel(notifChan);
        }
        int id = intent.getIntExtra("1001",0);
        assert notifMan != null;
        notifMan.notify(id, notif);

    }
}
