package org.canthack.tris.android.hgdroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by tristan on 12/08/2014.
 */
public class PlaylistAdapter extends BaseAdapter {
    private final Context context;
    private List<HgdSong> songs = Collections.emptyList();

    public PlaylistAdapter(Context ctx) {
        super();
        this.context = ctx;
    }

    public void updatePlaylist(List<HgdSong> newPlaylist) {
        this.songs = newPlaylist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public HgdSong getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.playlist_list_item, parent, false);
        }

        ImageView albumArt = ViewHolder.get(convertView, R.id.song_item_albumart);
        TextView songText = ViewHolder.get(convertView, R.id.song_item_song);
        TextView albumText = ViewHolder.get(convertView, R.id.song_item_album);
        TextView artistText = ViewHolder.get(convertView, R.id.song_item_artist);
        TextView userText = ViewHolder.get(convertView, R.id.song_item_username);

        HgdSong theSong = getItem(i);

        songText.setText(theSong.getTrackName());
        albumText.setText(theSong.getAlbumName());
        artistText.setText(theSong.getArtistName());
        userText.setText(theSong.getUserName());

        //TODO album art
        albumArt.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));

        return convertView;
    }
}
