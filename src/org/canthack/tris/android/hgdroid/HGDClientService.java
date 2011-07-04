// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
//
// HGDClientService.java - Service to maintain connection to server
//    and pull now playing over the connection, and submit tracks

package org.canthack.tris.android.hgdroid;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class HGDClientService extends Service {
	private final static String TAG = "HGDClientService";
	
	private boolean connected = false;
	
	@Override
	public void onCreate() {	
		Log.d(TAG, "Create");
		super.onCreate();
		Thread hgdThread = new Thread(null, new HGDClient(), "HGDClientService");
		hgdThread.start();
	}
	
	@Override
	public IBinder onBind(Intent i) {
		Log.d(TAG, "onBind");
		return null;
	}
	
	public void OnDestroy() {
		Log.d(TAG, "Destroy");
		super.onDestroy();
	}
	
	public boolean queueSong(Uri song) throws IOException, HGDroidException { //ToDo: Exception types
		if (!connected) {
			throw new HGDroidException("test");
		}
		return true;
	}
	
	class HGDClient implements Runnable {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(TAG, "Work");
			
		}
		
	}

}
