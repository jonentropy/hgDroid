package org.canthack.tris.android.mockdata;

import org.canthack.tris.android.hgdroid.HgdSong;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock playlist for testing.
 * Created by tristan on 12/08/2014.
 */
public class MockPlaylist {
    public static List<HgdSong> getPlaylist() {
        ArrayList<HgdSong> list = new ArrayList<HgdSong>(5);

        list.add(new HgdSong().setArtistName("Metallica").setAlbumName("Ride the Lightning").setTrackName("Creeping Death").setUserName("tris"));
        list.add(new HgdSong().setArtistName("Yes").setAlbumName("Shit Album").setTrackName("10 Minute Prog").setUserName("sav"));
        list.add(new HgdSong().setArtistName("Billy Talent").setAlbumName("Dead Silence").setTrackName("Viking Death Match").setUserName("claire"));
        list.add(new HgdSong().setArtistName("Aqua").setAlbumName("Some Album").setTrackName("Barbie Girl").setUserName("hannah"));
        list.add(new HgdSong().setArtistName("Slipknot").setAlbumName("Vol. 3").setTrackName("Duality").setUserName("tris"));

        return list;
    }
}
