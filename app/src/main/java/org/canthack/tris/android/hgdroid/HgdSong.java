package org.canthack.tris.android.hgdroid;

import org.canthack.tris.android.lastfm.AlbumArt;
import org.canthack.tris.android.lastfm.AlbumArtSize;

/**
 * Class to represent a song in the HGD playlist.
 * Created by tristan on 12/08/2014.
 */
public class HgdSong {
    private int id;

    private String trackName;
    private String albumName;
    private String artistName;

    private String userName;

    private boolean voted;
    private String albumArtUrl;

    public HgdSong(){
        //TODO real URL.
        albumArtUrl = null;
    }

    public String getTrackName() {
        return trackName;
    }

    public HgdSong setTrackName(String trackName) {
        this.trackName = trackName;
        return this;
    }

    public String getAlbumName() {
        return albumName;
    }

    public HgdSong setAlbumName(String albumName) {
        this.albumName = albumName;
        //TEMp
        //TODO
        this.albumArtUrl = AlbumArt.getArtworkUrl(artistName, albumName, AlbumArtSize.large);
        return this;
    }

    public String getArtistName() {
        return artistName;
    }

    public HgdSong setArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public HgdSong setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public boolean isVoted() {
        return voted;
    }

    public HgdSong setVoted(boolean voted) {
        this.voted = voted;
        return this;
    }

    public int getId() {
        return id;
    }

    public HgdSong setId(int id) {
        this.id = id;
        return this;
    }

    public String getAlbumArtUrl() {
        return albumArtUrl;
    }
}
