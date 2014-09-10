package org.canthack.tris.android.hgdroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.HttpResponseCache;
import android.os.IBinder;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.canthack.tris.android.mockdata.MockPlaylist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    public static final String PLAYLIST_EXTRA = "playlistContents";
    public static final String PLAYLIST_INTENT = "playlist";
    private final static String TAG = "HgdNowPlayingService";
    private final static String EXTRA_STOP_SERVICE = "stop";
    private volatile boolean threadRunning;
    private volatile boolean foreground;
    private NotificationManager mNotificationManager;
    private int notificationId = 1;
    private Notification.Builder nowPlayingBuilder;

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) Log.d(TAG, "Create");
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initNotifications();

        enableHttpResponseCache();

        Thread hgdThread = new Thread(null, new HGDClient(), "HGDClientService");
        hgdThread.start();
        threadRunning = true;
    }

    private void initNotifications() {

        Intent toStatusIntent = new Intent(this, Status.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Status.class);
        stackBuilder.addNextIntent(toStatusIntent);

        PendingIntent toStatusPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nowPlayingBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentInfo(getString(R.string.app_name));

        nowPlayingBuilder.setContentIntent(toStatusPendingIntent);

        //TODO INTENT FOR VOTE OFF PendingIntent stopNowPlayingServicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        nowPlayingBuilder.addAction(R.drawable.ic_stat_vote_off, getString(R.string.crap_song), null); //INTENT HERE

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
        startForeground(notificationId, nowPlayingBuilder.build());
        foreground = true;
        return true;
    }

    @Override
    public void onDestroy() {
        if (BuildConfig.DEBUG) Log.d(TAG, "onDestroy");
        threadRunning = false;
        flushHttpResponseCache();
        super.onDestroy();
    }

    private void enableHttpResponseCache() {
        try {
            File httpCacheDir = new File(getCacheDir(), "http");

            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.d(TAG, "HTTP response cache installation failed:", e);
        }
    }

    private void flushHttpResponseCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    class HGDClient implements Runnable {
        public void run() {
            if (BuildConfig.DEBUG) Log.d(TAG, "Thread starting");

            //TODO only for example...
            ArrayList<HgdSong> playlist = MockPlaylist.getPlaylist();
            Intent playlistIntent = new Intent(PLAYLIST_INTENT);
            playlistIntent.putParcelableArrayListExtra(PLAYLIST_EXTRA, playlist);
            sendStickyBroadcast(playlistIntent);

            int i = 1;

            while (threadRunning) {
                //TODO this Service should startForeground when information starts coming in from
                //the server. It should stopSelf when the server has been idle? or errors have
                //occurred.

                if (i < playlist.size()) {
                    HgdSong playing = playlist.get(i);
                    nowPlayingBuilder.setContentTitle(playing.getTrackName());
                    nowPlayingBuilder.setContentText(playing.getArtistName());

                    Bitmap b;
                    try {
                        b = Picasso.with(HgdNowPlayingService.this).load(playing.getAlbumArtUrl()).resizeDimen(android.R.dimen.notification_large_icon_width, android.R.dimen.notification_large_icon_height).get();
                        nowPlayingBuilder.setLargeIcon(b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (foreground) {
                        mNotificationManager.notify(notificationId, nowPlayingBuilder.build());
                    }

                    i++;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}