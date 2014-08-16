package org.canthack.tris.android.hgdroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * hgDroid - An Android client for the Hackathon Gunther Daemon
 * <p/>
 * Copyright 2014 Tristan Linnell
 * <p/>
 * HgdNowPlayingService.java - Service to maintain connection to server
 * and pull now playing over the connection, and publish notifications.
 *
 * @author tristan
 */
public class HgdNowPlayingService extends Service {
    private final static String TAG = "HgdNowPlayingService";
    private final static String EXTRA_STOP_SERVICE = "stop";

    private volatile boolean threadRunning;
    private volatile boolean foreground;
    private NotificationManager mNotificationManager;
    private int notificationId = 1;
    private Notification.Builder nowPlayingBuilder, notConnectedBuilder, currentBuilder;

    @Override
    public void onCreate() {
        Log.d(TAG, "Create");
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initNotifications();

        Thread hgdThread = new Thread(null, new HGDClient(), "HGDClientService");
        hgdThread.start();
        threadRunning = true;
    }

    private void initNotifications() {
        notConnectedBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Not connected!"); //temp. will not display notification until connected

        Intent toStatusIntent = new Intent(this, Status.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Status.class);
        stackBuilder.addNextIntent(toStatusIntent);

        PendingIntent toStatusPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notConnectedBuilder.setContentIntent(toStatusPendingIntent);

        Intent intent = new Intent(HgdNowPlayingService.this, HgdNowPlayingService.class);
        intent.putExtra(HgdNowPlayingService.EXTRA_STOP_SERVICE, true);

        PendingIntent stopNowPlayingServicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notConnectedBuilder.addAction(R.drawable.ic_stat_disconnect, getString(R.string.close), stopNowPlayingServicePendingIntent);

        nowPlayingBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Track Name")
                        .setContentText("Artist\nAlbum");

        nowPlayingBuilder.setContentIntent(toStatusPendingIntent);

        //TODO INTENT FOR VOTE OFF PendingIntent stopNowPlayingServicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        nowPlayingBuilder.addAction(R.drawable.ic_stat_vote_off, getString(R.string.crap_song), null); //INTENT HERE

        currentBuilder = nowPlayingBuilder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            if (intent.getExtras().getBoolean(HgdNowPlayingService.EXTRA_STOP_SERVICE)) stopSelf();
        }

        return Service.START_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        stopForeground(true);
        foreground = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        foreground = false;
        return null; //we have no interface, just knowing if bound or not
    }

    @Override
    public boolean onUnbind(Intent intent) {
        startForeground(notificationId, currentBuilder.build());
        foreground = true;
        return true;
    }

    @Override
    public void onDestroy() {
        if (BuildConfig.DEBUG) Log.d(TAG, "onDestroy");
        threadRunning = false;
        super.onDestroy();
    }

    class HGDClient implements Runnable {
        public void run() {
            Log.d(TAG, "Thread starting");

            int i = 1;

            while (threadRunning) {
                //TODO this Service should startForeground when information starts coming in from
                //the server. It should stopSelf when the server has been idle? or errors have
                //occurred.

                try {
                    nowPlayingBuilder.setContentText("Track  " + i);

                    if (foreground)
                        mNotificationManager.notify(notificationId, nowPlayingBuilder.build());

                    Thread.sleep(5000);

                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
