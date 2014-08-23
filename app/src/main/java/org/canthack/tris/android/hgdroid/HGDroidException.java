// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
//
// HGDroidException.java - Exception class for hgd errors

package org.canthack.tris.android.hgdroid;

public class HGDroidException extends Exception {

    private static final long serialVersionUID = -4151506917585115389L;

    public HGDroidException(String string) {
        super(string);
    }
}
