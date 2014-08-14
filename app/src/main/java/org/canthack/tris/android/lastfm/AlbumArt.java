package org.canthack.tris.android.lastfm;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.canthack.tris.android.hgdroid.CustomHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Album artwork downloader. Caches previous URL locations in memory, but
 * not the artwork itself.
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

    private static final ConcurrentHashMap<String, String> cachedUrls = new ConcurrentHashMap<String, String>();

    private static final Gson gson = new Gson();

    public static String getArtworkUrl(String artist, String album, AlbumArtSize size) {
        String cachedName = getCacheName(artist, album);

        String cachedUrl = cachedUrls.get(cachedName);
        if (cachedUrl != null) {
            Log.d(TAG, "Url cached for " + cachedName);
            return cachedUrl;
        }

        Log.d(TAG, "Url NOT cached for " + cachedName);

        String url;

        try {
            url = new StringBuilder(256)
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

        InputStream in = CustomHttpClient.retrieveStream(url);

        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, UTF8));

            JsonObject responseObject = gson.fromJson(streamReader, JsonObject.class);
            if (responseObject == null) return null;

            JsonObject albumObject = responseObject.getAsJsonObject(ALBUM);
            if (albumObject == null) return null;

            JsonArray imagesArray = albumObject.getAsJsonArray(IMAGE);
            if (imagesArray == null) return null;

            for (int i = 0; i < imagesArray.size(); i++) {
                if (imagesArray.get(i).getAsJsonObject().get(SIZE).getAsString().equalsIgnoreCase(size.name())) {
                    String foundUrl = imagesArray.get(i).getAsJsonObject().get(TEXT).getAsString();
                    cachedUrls.put(cachedName, foundUrl);
                    return foundUrl;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error getting artwork URL!", e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing stream!", e);
            }
        }

        return null;
    }

    private static String getCacheName(String artist, String album) {
        return artist + "::" + album;
    }
}
