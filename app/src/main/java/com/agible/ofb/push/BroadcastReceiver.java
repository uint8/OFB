package com.agible.ofb.push;

import android.content.Context;
import android.content.Intent;

import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.agible.ofb.events.Events;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.notifications.NotificationsHandler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by seth on 6/16/15.
 */
public class BroadcastReceiver extends NotificationsHandler {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;
    Values values;
    Context context;

    public BroadcastReceiver(Values values){
        this.values = values;
    }
    @Override
    public void onRegistered(Context context,  final String gcmRegistrationId) {
        super.onRegistered(context, gcmRegistrationId);

        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                try {

                    Channel channel = new Channel();
                    channel.setHandle(gcmRegistrationId);
                    MobileServiceClient mClient = values.mClient;
                    //mClient.getPush().register(gcmRegistrationId, new String[] {"Android"});
                    JSONObject jsonObject = new JSONObject();
                    JSONObject payload = new JSONObject();
                    payload.put("message", "$(message)");
                    jsonObject.put("data", payload);
                    System.out.println(jsonObject.toString());
                    mClient.getPush().registerTemplate(gcmRegistrationId, "toastTemplate", jsonObject.toString(), new String[]{"Android"});
                    mClient.getTable(Channel.class).insert(channel);
                    return null;
                }
                catch(Exception e) {
                    Log.e("BroadcastReciever", e.getMessage());
                    // handle error
                }
                return null;
            }
        }.execute();
    }
    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        System.out.println("Recieved GCM");
        String nhMessage = bundle.getString("message");

        sendNotification(nhMessage);

    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, Events.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_action_settings)
                        .setContentTitle("Notification Hub Demo")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    public class Channel {
        // Push Notifications - creates handle column in db table (dynamic schema)
        @com.google.gson.annotations.SerializedName("handle")
        private String mHandle;

        // Returns the handle
        public String getHandle() { return mHandle; }

        // Sets the handle
        public final void setHandle(String handle) { mHandle = handle; }

        // Item Id
        @com.google.gson.annotations.SerializedName("id")
        private String mId;

        //Returns the item id
        public String getId() { return mId; }

        //Sets the item id - @param id : id to set
        public final void setId(String id) { mId = id; }

    }
}
