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
	//Please ensure these names match the key ids in settings.xml
	//To ensure settings are correctly saved and restored in future
	//versions of hgDroid.
	private static final String HGD_SETTING_SERVER = "server";
	private static final String HGD_SETTING_PORT = "port";
	private static final String HGD_SETTING_USERNAME = "uname";
	private static final String HGD_SETTING_PASSWORD = "pwd";
	private static final String HGD_SETTING_SSL = "ssl";
	
	//Default Values
	private static final String HGD_SETTING_SERVER_DEF = "192.168.0.57";
	private static final int HGD_SETTING_PORT_DEF = 4444; //ToDo: Change to the real default port (also edit settings.xml)
	private static final String HGD_SETTING_USERNAME_DEF = "";
	private static final String HGD_SETTING_PASSWORD_DEF = "";
	private static final boolean HGD_SETTING_SSL_DEF = true;	
	
	//Settings
	public static String getServerAddress(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).
			getString(HGD_SETTING_SERVER, HGD_SETTING_SERVER_DEF);
	}
	
	public static int getServerPort(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getInt(HGD_SETTING_PORT, HGD_SETTING_PORT_DEF);
	}
	
	public static String getUserName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).
			getString(HGD_SETTING_USERNAME, HGD_SETTING_USERNAME_DEF);
	}
	
	public static String getPassword(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).
			getString(HGD_SETTING_PASSWORD, HGD_SETTING_PASSWORD_DEF);
	}
	
	public static boolean getSSL(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean(HGD_SETTING_SSL, HGD_SETTING_SSL_DEF);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
}
