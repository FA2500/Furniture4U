package my.fa250.furniture4u;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomFCMService extends FirebaseMessagingService {
    NotificationManager notifMan;
    Notification notif;

    Uri defaultSoundUri;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        notifMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Map<String,String> dataMap = new HashMap<>();
        String noteType="";
        if (remoteMessage.getData().size() > 1) {
            noteType = remoteMessage.getData().get("type");
            dataMap = remoteMessage.getData();

        }
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        switch (noteType)
        {
            case "BIGTEXT":
                bigTextNotification(dataMap);
                break;
            case "BIGPIC":
                bigPicNotification(dataMap);
                break;
            case "ACTIONS":
                notificationActions(dataMap);
                break;
            case "DIRECTREPLY":
                directReply(dataMap);
                break;
            case "INBOX":
                inboxTypeNotification(dataMap);
                break;
            case "MESSAGE":
                messageTypeNotification(dataMap);
                break;

        }
    }

    private void notifyUser(String title, String messageBody,String type,String imagePath) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setColor(Color.BLUE)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notifMan =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // For android Oreo and above  notif channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Fcm notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notifMan.createNotificationChannel(channel);
        }

        notifMan.notify(1 , notificationBuilder.build());


    }


    public void bigTextNotification(Map<String,String> dataMap)
    {
        String title = dataMap.get("title");
        String message = dataMap.get("message");
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = "FCMPush";
        NotificationCompat.Builder builder1= new NotificationCompat.Builder(this, channelId);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notifMan.createNotificationChannel(chan);

        }

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(message);
        style.setSummaryText(title);

        builder1.setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.logo)
                .setColor(Color.BLUE)
                .setStyle(style);
        builder1.build();
        notif = builder1.getNotification();
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(1,notif);
        }else {
            notifMan.notify(1, notif);
        }

    }

    public void bigPicNotification(Map<String,String> dataMap)
    {
        String title = dataMap.get("title");
        String message = dataMap.get("message");
        String imageUrl = dataMap.get("imageUrl");
        try {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = "FCMPush";
            NotificationCompat.Builder builder2= new NotificationCompat.Builder(this, channelId);
            if (Build.VERSION.SDK_INT >= 26) {

                NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notifMan.createNotificationChannel(chan);
            }
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.setBigContentTitle(title);
            style.setSummaryText(message);
            style.bigPicture(Glide.with(CustomFCMService.this).asBitmap().load(imageUrl).submit().get());

            builder2.setContentTitle(title)
                    .setContentText(message)
                    .setSound(defaultSoundUri)
                    .setColor(Color.GREEN)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setStyle(style);
            builder2.build();
            notif = builder2.getNotification();
            if (Build.VERSION.SDK_INT >= 26) {
                startForeground(1,notif);
            }else {
                notifMan.notify(1, notif);
            }

        }catch (Exception e)
        {

        }



    }

    public void notificationActions(Map<String,String> dataMap)
    {
        String title = dataMap.get("title");
        String message = dataMap.get("message");

        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = "FCMPush";
        NotificationCompat.Builder builder3= new NotificationCompat.Builder(this, channelId);

        if (Build.VERSION.SDK_INT >= 26) {

            NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notifMan.createNotificationChannel(chan);

        }

        Intent intent1 = new Intent(CustomFCMService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(CustomFCMService.this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(getBaseContext(), NotifReceiver.class);
        cancelIntent.putExtra("ID",1);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, cancelIntent, Intent.FILL_IN_ACTION | PendingIntent.FLAG_IMMUTABLE);

        builder3.setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(Color.BLUE)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent)
                .addAction(android.R.drawable.ic_delete, "DISMISS", cancelPendingIntent)
                .build();
        notif = builder3.getNotification();
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(1,notif);
        }else {
            notifMan.notify(1, notif);
        }



    }

    public void directReply(Map<String,String> dataMap)
    {
        String title = dataMap.get("title");
        String message = dataMap.get("message");
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = "FCMPush";
        NotificationCompat.Builder builder4= new NotificationCompat.Builder(this, channelId);

        if (Build.VERSION.SDK_INT >= 26) {

            NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notifMan.createNotificationChannel(chan);
        }

        Intent cancelIntent = new Intent(getBaseContext(), NotifReceiver.class);
        cancelIntent.putExtra("ID",1);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, cancelIntent, Intent.FILL_IN_ACTION | PendingIntent.FLAG_IMMUTABLE);


        Intent feedbackIntent = new Intent(CustomFCMService.this, NotifReceiver.class);
        PendingIntent feedbackPendingIntent = PendingIntent.getBroadcast(CustomFCMService.this,
                100,feedbackIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteInput remoteInput = new RemoteInput.Builder("DirectReplyNotification")
                .setLabel(message)
                .build();

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(android.R.drawable.ic_delete,
                "Write here...", feedbackPendingIntent)
                .addRemoteInput(remoteInput)
                .build();


        builder4 .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(feedbackPendingIntent)
                .addAction(action)
                .setColor(Color.RED)
                .addAction(android.R.drawable.ic_menu_compass, "Cancel", cancelPendingIntent);
        builder4.build();
        notif = builder4.getNotification();
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(1,notif);
        }else {
            notifMan.notify(1, notif);
        }


    }

    public void inboxTypeNotification(Map<String,String> dataMap)
    {
        try {
            String title = dataMap.get("title");
            String message = dataMap.get("message");
            JSONArray jsonArray = new JSONArray(dataMap.get("contentList"));

            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = "FCMPush";
            NotificationCompat.Builder builder5= new NotificationCompat.Builder(this, channelId);

            if (Build.VERSION.SDK_INT >= 26) {

                NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notifMan.createNotificationChannel(chan);
            }

            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            style.setSummaryText(message);
            style.setBigContentTitle(title);
            for(int i=1;i<jsonArray.length();i++)
            {
                String emailName = jsonArray.getString(i);
                style.addLine(emailName);
            }

            builder5.setContentTitle(title)
                    .setContentText(message)
                    .setColor(Color.BLUE)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.drawable.logo)
                    .setStyle(style);
            builder5.build();
            notif = builder5.getNotification();
            if (Build.VERSION.SDK_INT >= 26) {
                startForeground(1,notif);
            }else {
                notifMan.notify(1, notif);
            }

        }catch (Exception e)
        {

        }


    }
    public void messageTypeNotification(Map<String,String> dataMap)
    {
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = "FCMPush";
        NotificationCompat.Builder builder6= new NotificationCompat.Builder(this, channelId);

        if (Build.VERSION.SDK_INT >= 26) {

            NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notifMan.createNotificationChannel(chan);
        }

        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle("Janhavi");
        style.addMessage("Is there any online tutorial for FCM?",1,"member1");
        style.addMessage("Yes",1,"");
        style.addMessage("How to use constraint layout?",1,"member2");



        builder6.setSmallIcon(R.drawable.logo)
                .setColor(Color.RED)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setSound(defaultSoundUri)
                .setStyle(style)
                .setAutoCancel(true);
        builder6.build();
        notif = builder6.getNotification();

        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(1,notif);
        }else {
            notifMan.notify(1, notif);
        }



    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
