// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
//
// Settings.java - Settings Activity for the application.

package org.canthack.tris.android.hgdroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Settings extends PreferenceActivity {
    private static final int SCAN_REQUEST_CODE = 0;

    //Settings for the hgDroid client...
    //Please ensure these names match the key IDs in settings.xml
    //To ensure settings are correctly saved and restored in future
    //versions of hgDroid.
    private static final String HGD_SETTING_SERVER = "server";
    private static final String HGD_SETTING_PORT = "port";
    private static final String HGD_SETTING_USERNAME = "uname";
    private static final String HGD_SETTING_PASSWORD = "pwd";
    private static final String HGD_SETTING_SSL = "ssl";
    private static final String HGD_SETTING_FIRST_RUN = "first_run";

    //Settings
    public static String getServerAddress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(HGD_SETTING_SERVER, context.getResources().getString(R.string.default_hostname));
    }

    public static int getServerPort(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(HGD_SETTING_PORT, context.getResources().getInteger(R.integer.default_port));
    }

    public static void setServerAddress(Context context, String address) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(HGD_SETTING_SERVER, address).apply();
    }

    public static void setServerPort(Context context, int port) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(HGD_SETTING_PORT, Integer.toString(port)).apply();
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


    public static boolean getFirstRun(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(HGD_SETTING_FIRST_RUN, context.getResources().getBoolean(R.bool.default_first_run));
    }

    public static void setFirstRun(Context context, boolean firstRun) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(HGD_SETTING_FIRST_RUN, firstRun).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIntent(getIntent());
        Settings.setFirstRun(this, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;
        Uri openUri = intent.getData();
        if (openUri != null && getString(R.string.hgd_uri_scheme).equalsIgnoreCase(openUri.getScheme())) {
            promptToAndSaveSettings(openUri, true);
        }
    }

    private void promptToAndSaveSettings(final Uri uri, final boolean finish) {
        String message = Settings.getFirstRun(this) ? getString(R.string.save_settings_message_first_run) : getString(R.string.save_settings_message);

        new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(message).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Settings.setFirstRun(Settings.this, false);
                Settings.setServerAddress(Settings.this, uri.getHost());
                Settings.setServerPort(Settings.this, uri.getPort());

                if (finish) {
                    Settings.this.finish();
                    startActivity(new Intent(Settings.this, Status.class));
                } else {
                    //reload settings
                    setPreferenceScreen(null);
                    addPreferencesFromResource(R.xml.settings);
                }
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (finish) Settings.this.finish();
            }
        }).show();
    }

    //Creates menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    //Handles menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mitmScanBarcode:
                scanBarcode();
                return true;
        }
        return false;
    }

    private void scanBarcode() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, SCAN_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCAN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                promptToAndSaveSettings(Uri.parse(intent.getStringExtra("SCAN_RESULT")), false);
            }
        }
    }
}
