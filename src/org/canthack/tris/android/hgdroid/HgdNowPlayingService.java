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
 * 
 *  Copyright 2014 Tristan Linnell
 *  
 *  HgdNowPlayingService.java - Service to maintain connection to server
 *  and pull now playing over the connection, and publish notifications.
 * @author tristan
 *
 */
public class HgdNowPlayingService extends Service {
	private final static String TAG = "HgdNowPlayingService";

	private volatile boolean connected = false;
	private volatile boolean threadRunning = false;
	private Thread hgdThread;
	private NotificationManager mNotificationManager;
	private int notificationId = 1;
	
	@Override
	public void onCreate() {	
		Log.d(TAG, "Create");
		super.onCreate();
		
		hgdThread = new Thread(null, new HGDClient(), "HGDClientService");
		hgdThread.start();
		threadRunning = true;
		
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null && intent.getExtras() != null && intent.getExtras().getBoolean("Shutdown")) stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public IBinder onBind(Intent i) {
		Log.d(TAG, "onBind");
		return null;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		threadRunning = false;
		super.onDestroy();
	}

	class HGDClient implements Runnable {	
		public void run() {
			Log.d(TAG, "Thread starting");
			
			Notification.Builder mBuilder =
			        new Notification.Builder(HgdNowPlayingService.this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle("hgDroid")
			        .setContentText("Not connected!");
			
			// Creates an explicit intent for an Activity in your app
			Intent toStatusIntent = new Intent(HgdNowPlayingService.this, Status.class);

			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(HgdNowPlayingService.this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(Status.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(toStatusIntent);
			
			PendingIntent toStatusPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(toStatusPendingIntent);
			mBuilder.addAction(R.drawable.ic_launcher, "To app", toStatusPendingIntent);
			
			Intent intent = new Intent(HgdNowPlayingService.this, HgdNowPlayingService.class);
			intent.putExtra("Shutdown", true);
			PendingIntent stopNowPlayingServicePendingIntent = PendingIntent.getService(HgdNowPlayingService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.addAction(R.drawable.ic_launcher, "Stop Service", stopNowPlayingServicePendingIntent);
			
			HgdNowPlayingService.this.startForeground(notificationId, mBuilder.build());
			
			while(threadRunning){
				Log.d(TAG, "Work");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
