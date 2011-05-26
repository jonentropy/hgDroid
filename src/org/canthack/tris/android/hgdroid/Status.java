// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
//
// Status.java - Main status Activity for the application.

package org.canthack.tris.android.hgdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;

public class Status extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
    	switch(item.getItemId()) {
    		case R.id.mitmSettings:
    			startActivity(new Intent(this, Settings.class));
    			return true;
    		case R.id.mitmQuit:
    			finish();
    			break;
    		//ToDo: add more menu items here...	
    			
    	}
    	return false;
    }
    
    //ToDo: add button handlers...
    
    
    
}