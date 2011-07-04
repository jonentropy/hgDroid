// hgDroid - An Android client for the Hackathon Günther Daemon
//
// Copyright 2011 Tristan Linnell
//
// ServerConstants.java - HGD constant values from hgd.c in the server project.

package org.canthack.tris.android.hgdroid;

public final class ServerConstants {
	//ToDo: most of these will not be needed. Remove unused constants
	//when the client is complete.

	//Protocol Version
	public static final int HGD_PROTO_VERSION =	3;

	/* misc */
	public static final int HGD_DFL_REQ_VOTES =	3;
	public static final int HGD_PID_STR_SZ = 10;
	public static final int HGD_ID_STR_SZ = 40; /* 8byte int */
	public static final int HGD_SHA_SALT_SZ = 20;
	public static final int HGD_MAX_PASS_SZ = 20;
	public static final int HGD_MAX_USER_QUEUE = 5;

	/* networking */
	public static final int HGD_DFL_PORT = 6633;
	public static final String HGD_DFL_HOST = "127.0.0.1";
	public static final int HGD_DFL_BACKLOG = 10;
	public static final int HGD_MAX_LINE = 256;
	public static final int HGD_MAX_BAD_COMMANDS = 3;
	public static final int HGD_BINARY_CHUNK = 4096;
	public static final int HGD_BINARY_RECV_SZ = 16384;
	public static final int	HGD_MAX_PROTO_TOKS = 3;
	public static final String HGD_GREET = "ok|HGD-"; //Version number follows this
	public static final String HGD_BYE = "ok|Catch you later d00d!";

	/* SSL */
	public static final int	HGD_CRYPTO_PREF_ALWAYS = 0;
	public static final int HGD_CRYPTO_PREF_IF_POSS = 1;
	public static final int HGD_CRYPTO_PREF_NEVER = 2;
	
}
