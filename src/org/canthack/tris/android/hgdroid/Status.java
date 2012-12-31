// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
//
// Status.java - Main status Activity for the application.

package org.canthack.tris.android.hgdroid;

import org.canthack.tris.android.media.SoundEffects;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Status extends ListActivity implements OnClickListener{
	private static final String TAG = "STATUS";
	private static final int HGDROID_GETSONG = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Set all button's event handlers to the Activity itself...
        View crapButton = this.findViewById(R.id.btnCrapSong);
        crapButton.setOnClickListener(this);
                
        //TODO add other buttons and views here...
             
        //create the service...
        startService(new Intent(Status.this, HGDClientService.class));
    }
    
    //Creates menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;   	
    }
    
    //Handles menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.d("MENUS", "MENUS");
    	switch(item.getItemId()) {
    		case R.id.mitmSettings:
    			startActivity(new Intent(this, Settings.class));
    			return true;
    		case R.id.mitmDisconnect:
    			//stop service first...
    			stopService(new Intent(Status.this, HGDClientService.class));
    			break;
    		//TODO: add more menu items here...	
    			
    	}
    	return false;
    }

    //Handle button clicks etc.
    @Override
	public void onClick(View v) {	
		Log.v(TAG, "Clicked!");
		switch(v.getId()) {
		case R.id.btnCrapSong:
			//TODO add proper logic here. just a test for now
			Toast t = Toast.makeText(this , R.string.crap_song, Toast.LENGTH_SHORT);
			t.setGravity(Gravity.BOTTOM, 0, 0);
			t.show();
				
			SoundEffects.playEffect(this, R.raw.crapsong);	
			break;			
		}	
	}

	private void ChooseSong() {
		// Browse for and return the filename of a track from the phone memory/SD card
		Log.d(TAG, "Selecting song...");
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, getResources().getText(R.string.select_song_intent)), HGDROID_GETSONG);
	}     
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode == RESULT_CANCELED)
	    	return;
	    
		if (requestCode == HGDROID_GETSONG) {
		    //Select song callback...
			Uri songURI = data.getData();
			Log.d(TAG, "Song selected: " + songURI.toString());
			
	    }
	    //ToDo: other callbacks go here
		
	}
}