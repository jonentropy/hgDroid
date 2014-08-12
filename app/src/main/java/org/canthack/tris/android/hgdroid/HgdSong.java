package org.canthack.tris.android.hgdroid;

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
}
