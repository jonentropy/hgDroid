package org.canthack.tris.android.lastfm;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Album artwork downloader, using lastfm.
 * Created by tristan on 12/08/2014.
 */
public final class AlbumArt {
    private static final String TAG = "AlbumArt";

    private static final String ALBUM = "album";
    private static final String IMAGE = "image";
    private static final String SIZE = "size";
    private static final String TEXT = "#text";
    private static final String UTF8 = "UTF-8";

    private static final String API_ROOT = "http://ws.audioscrobbler.com/2.0/";
    private static final String LAST_FM_API_KEY = "d73bc1bcce5c08bd73c5f941480845e8";
    private static final String API_METHOD = "?method=album.getinfo&api_key=";
    private static final String ARTIST_PARAM = "&artist=";
    private static final String ALBUM_PARAM = "&album=";
    private static final String AUTO_CORRECT_PARAM = "&autocorrect=1";
    private static final String FORMAT_PARAM = "&format=json";

    private static final Gson gson = new Gson();

    public static String getArtworkUrl(String artist, String album, AlbumArtSize size) {
        String urlString;

        try {
            urlString = new StringBuilder(256)
                    .append(API_ROOT)
                    .append(API_METHOD)
                    .append(LAST_FM_API_KEY)
                    .append(ARTIST_PARAM)
                    .append(URLEncoder.encode(artist, UTF8))
                    .append(ALBUM_PARAM)
                    .append(URLEncoder.encode(album, UTF8))
                    .append(AUTO_CORRECT_PARAM)
                    .append(FORMAT_PARAM)
                    .toString();
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "Could not create URL for Album art!", e);
            return null;
        }

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, UTF8));

            JsonObject responseObject = gson.fromJson(streamReader, JsonObject.class);
            if (responseObject == null) return null;

            JsonObject albumObject = responseObject.getAsJsonObject(ALBUM);
            if (albumObject == null) return null;

            JsonArray imagesArray = albumObject.getAsJsonArray(IMAGE);
            if (imagesArray == null) return null;

            for (JsonElement element : imagesArray) {
                if (element.getAsJsonObject().get(SIZE).getAsString().equalsIgnoreCase(size.name())) {
                    String foundUrl = element.getAsJsonObject().get(TEXT).getAsString();
                    return foundUrl;
                }
            }

        } catch (IOException e) {
            Log.e(TAG, "Error getting artwork URL!", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

}
