// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
// Copyright 2011 Steve Savia
//
// Settings.java - Settings Activity for the application.

package org.canthack.tris.android.hgdroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
}
