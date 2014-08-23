package org.canthack.tris.android.hgdroid;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by tristan on 12/08/2014.
 */
public class PlaylistAdapter extends BaseAdapter {
    private final int NOW_PLAYING_COLOR;
    private final Context context;
    private final int imageWidth, imageHeight; //pixels
    private final float imageDimensionWidth, imageDimensionHeight; //DP
    private List<HgdSong> songs = Collections.emptyList();

    public PlaylistAdapter(Context ctx) {
        super();
        this.context = ctx;

        if (BuildConfig.DEBUG) {
            Picasso.with(ctx.getApplicationContext()).setIndicatorsEnabled(true);
            Picasso.with(ctx.getApplicationContext()).setLoggingEnabled(true);
        }

        this.NOW_PLAYING_COLOR = ctx.getResources().getColor(R.color.now_playing_highlight);
        this.imageHeight = this.imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ctx.getResources().getDimension(R.dimen.playlist_image_size), ctx.getResources().getDisplayMetrics());
        this.imageDimensionHeight = this.imageDimensionWidth = context.getResources().getDimension(R.dimen.playlist_image_size);
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
        return songs.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.playlist_list_item, parent, false);
        }

        convertView.setBackgroundColor(i == 0 ? NOW_PLAYING_COLOR : Color.TRANSPARENT);

        ImageView albumArt = ViewHolder.get(convertView, R.id.song_item_albumart);
        ViewGroup.LayoutParams imageParams = albumArt.getLayoutParams();
        imageParams.height = i == 0 ? (int) imageDimensionHeight * 2 : (int) imageDimensionHeight;
        imageParams.width = i == 0 ? (int) imageDimensionWidth * 2 : (int) imageDimensionWidth;

        TextView songText = ViewHolder.get(convertView, R.id.song_item_song);
        TextView albumText = ViewHolder.get(convertView, R.id.song_item_album);
        TextView artistText = ViewHolder.get(convertView, R.id.song_item_artist);
        TextView userText = ViewHolder.get(convertView, R.id.song_item_username);

        HgdSong theSong = getItem(i);

        songText.setText(theSong.getTrackName());
        albumText.setText(theSong.getAlbumName());
        artistText.setText(theSong.getArtistName());
        userText.setText(theSong.getUserName());

        String artworkUrl = theSong.getAlbumArtUrl();

        Picasso.with(context.getApplicationContext()).load(artworkUrl)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .resize(i == 0 ? imageWidth * 2 : imageWidth, i == 0 ? imageHeight * 2 : imageHeight)
                .centerCrop()
                .into(albumArt);

        return convertView;
    }
}
