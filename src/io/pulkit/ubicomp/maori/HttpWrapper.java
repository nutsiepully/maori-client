package io.pulkit.ubicomp.maori;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Fetch HTTP raw results given a URL.
 */
public class HttpWrapper {

    private static final String TAG = "maori:HttpWrapper";

    byte[] fetchResultBytes(String url) {
        Log.d(TAG, "Fetching result for url - " + url);

        byte[] byteArray = null;
        HttpClient httpClient = new DefaultHttpClient();
        String result;

        try {
            result = httpClient.execute(new HttpGet(url), new BasicResponseHandler());
            byteArray = Base64.decode(result.getBytes(), Base64.DEFAULT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArray;
    }

    String fetchResultString(String url) {
        String result;
        HttpResponse httpResponse;
        HttpClient httpClient = new DefaultHttpClient();

        try {
            httpResponse = httpClient.execute(new HttpGet(url));
            InputStream inputStream = httpResponse.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
