package org.canthack.tris.android.hgdroid;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.util.Log;
/**
 * Provides a single, shared, thread-safe HTTPClient.
 */
public final class CustomHttpClient {
    private static final String TAG = "hgDroid HTTP Client";
    private static AbstractHttpClient customHttpClient;
    private static HttpRequestRetryHandler requestRetryHandler;
    private static final int MIN_URL_LENGTH = 7;

    /** A private Constructor prevents any other class from instantiating. */
    private CustomHttpClient() {
    }

    /**
     * Get a singleton, thread-safe HttpClient for making HTTP requests.
     */
    public static synchronized HttpClient getHttpClient() {
        if (customHttpClient == null) {
            final HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, false);

            ConnManagerParams.setTimeout(params, 100);

            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);

            final SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));


            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRoute() {
                @Override
                public int getMaxForRoute(final HttpRoute httproute)
                {
                    return 20;
                }
            });

            final ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params,schReg);

            customHttpClient = new DefaultHttpClient(conMgr, params);
            requestRetryHandler = new DefaultHttpRequestRetryHandler(5, false){

                @Override
                public boolean retryRequest(IOException ex, int count, HttpContext cx) {
                    if(super.retryRequest(ex, count, cx)){
                        Log.d(TAG, "Retrying request " + cx.toString());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            };

            customHttpClient.setHttpRequestRetryHandler(requestRetryHandler);
        }
        return customHttpClient;
    }

    /**
     * Retrieve the input stream from the specified Url. Can return null if
     * the URL cannot be found or times out.
     */
    public static synchronized InputStream retrieveStream(final String url) {
        if(url == null || url.length() <= MIN_URL_LENGTH){
            return null;
        }

        HttpGet getRequest;

        try{
            getRequest = new HttpGet(url);

            try {
                final HttpResponse response = CustomHttpClient.getHttpClient().execute(getRequest);
                if(response == null) return null;

                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                final HttpEntity getResponseEntity = response.getEntity();
                if(getResponseEntity == null) return null;

                final InputStream s = getResponseEntity.getContent();

                return s;
            }
            catch (IOException e) {
                getRequest.abort();
            }
            catch(IllegalStateException e){
                getRequest.abort();
            }

        }
        catch(Exception e){
            return null;
        }

        return null;
    }
}