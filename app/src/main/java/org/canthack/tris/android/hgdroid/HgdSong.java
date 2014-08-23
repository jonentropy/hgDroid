package org.canthack.tris.android.hgdroid;

import android.os.Parcel;
import android.os.Parcelable;

import org.canthack.tris.android.lastfm.AlbumArt;
import org.canthack.tris.android.lastfm.AlbumArtSize;

/**
 * Class to represent a song in the HGD playlist.
 * Created by tristan on 12/08/2014.
 */
public class HgdSong implements Parcelable {
    public static final Parcelable.Creator<HgdSong> CREATOR = new Parcelable.Creator<HgdSong>() {
        public HgdSong createFromParcel(Parcel in) {
            return new HgdSong(in);
        }

        public HgdSong[] newArray(int size) {
            return new HgdSong[size];
        }
    };
    private int id;
    private String trackName;
    private String albumName;
    private String artistName;
    private String userName;
    private boolean voted;
    private String albumArtUrl;

    public HgdSong(Parcel in) {
        this.id = in.readInt();
        this.trackName = in.readString();
        this.albumName = in.readString();
        this.artistName = in.readString();
        this.userName = in.readString();
        this.voted = in.readByte() == 1;
        this.albumArtUrl = in.readString();
    }

    public HgdSong() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(trackName);
        parcel.writeString(albumName);
        parcel.writeString(artistName);
        parcel.writeString(userName);
        parcel.writeByte((byte) (voted ? 1 : 0));
        parcel.writeString(albumArtUrl);
    }

    /**
     * Fetch album art Url from network. Do not call from
     * the UI Thread!
     *
     * @return
     */
    public HgdSong autoSetAlbumArtUrl() {
        this.albumArtUrl = AlbumArt.getArtworkUrl(this.artistName, this.albumName, AlbumArtSize.large);
        return this;
    }
}
