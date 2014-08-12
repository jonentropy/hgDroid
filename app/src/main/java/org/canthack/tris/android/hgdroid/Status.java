package org.canthack.tris.android.hgdroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.canthack.tris.android.media.SoundEffects;
import org.canthack.tris.android.org.canthack.tris.android.hgdroid.mockdata.MockPlaylist;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * hgDroid - An Android client for the Hackathon Gunther Daemon
 * <p/>
 * Copyright 2014 Tristan Linnell
 * <p/>
 * Status.java - Main Activity displaying playlist_list_item.
 *
 * @author tristan
 */
public class Status extends ListActivity implements OnClickListener {
    private static final String TAG = "STATUS";
    private static final int HGDROID_GETSONG = 1;

    private ServiceConnection nowPlayingConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
        }

        public void onServiceDisconnected(ComponentName className) {
        }
    };
    private PlaylistAdapter playlistAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Set all button's event handlers to the Activity itself...
        View crapButton = this.findViewById(R.id.btnCrapSong);
        crapButton.setOnClickListener(this);

        //TODO add other buttons and views here...
        playlistAdapter = new PlaylistAdapter(this);
        setListAdapter(playlistAdapter);

        playlistAdapter.updatePlaylist(MockPlaylist.getPlaylist());
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(Status.this, HgdNowPlayingService.class);
        startService(intent);
        bindService(intent, nowPlayingConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindService(nowPlayingConnection);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Settings.getFirstRun(this)) {
            new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(getString(R.string.first_run_message)).setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Settings.setFirstRun(Status.this, false);
                    startActivity(new Intent(Status.this, Settings.class));
                }
            }).show();
        }
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
        switch (item.getItemId()) {
            case R.id.mitmSettings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.mitmDisconnect:
                //stop service first...
                stopService(new Intent(Status.this, HgdNowPlayingService.class));
                finish();
                break;
            case R.id.mitmQueue:
                chooseSong();
                return true;
            //TODO: add more menu items here...

        }
        return false;
    }

    //Handle button clicks etc.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCrapSong:
                //TODO add proper logic here. just a test for now
                Toast t = Toast.makeText(this, R.string.crap_song, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM, 0, 0);
                t.show();

                SoundEffects.playEffect(this, R.raw.crapsong);
                break;
        }
    }

    private void chooseSong() {
        // Browse for and return the filename of a track from the phone memory/SD card
        Log.d(TAG, "Selecting song...");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
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

            try {
                InputStream is = getContentResolver().openInputStream(songURI);
                //TODO Pass this to the HGD client implementation to send it to the server.
                //This is slow when on the UI Thread. Move the opening of the input stream to
                //the worker Thread.

            } catch (FileNotFoundException e) {
                Log.e(TAG, "Error opening chosen song", e);
                Toast.makeText(this, getString(R.string.error_opening_song), Toast.LENGTH_SHORT).show();
            }

        }
        //ToDo: other callbacks go here

    }
}