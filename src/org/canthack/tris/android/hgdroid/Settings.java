// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
//
// Settings.java - Settings Activity for the application.

package org.canthack.tris.android.hgdroid;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {
	//Settings for the hgDroid client...
	//Please ensure these names match the key IDs in settings.xml
	//To ensure settings are correctly saved and restored in future
	//versions of hgDroid.
	private static final String HGD_SETTING_SERVER = "server";
	private static final String HGD_SETTING_PORT = "port";
	private static final String HGD_SETTING_USERNAME = "uname";
	private static final String HGD_SETTING_PASSWORD = "pwd";
	private static final String HGD_SETTING_SSL = "ssl";
		
	//Settings
	public static String getServerAddress(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getString(HGD_SETTING_SERVER, context.getResources().getString(R.string.default_hostname));
	}
	
	public static int getServerPort(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getInt(HGD_SETTING_PORT, context.getResources().getInteger(R.integer.default_port));
	}
	
	public static String getUserName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).
			getString(HGD_SETTING_USERNAME, context.getResources().getString(R.string.default_username));
	}
	
	public static String getPassword(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).
			getString(HGD_SETTING_PASSWORD, context.getResources().getString(R.string.default_password));
	}
	
	public static boolean getSSL(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean(HGD_SETTING_SSL, context.getResources().getBoolean(R.bool.default_ssl));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
}
