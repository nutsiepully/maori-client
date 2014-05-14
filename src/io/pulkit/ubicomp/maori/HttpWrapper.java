package io.pulkit.ubicomp.maori;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Fetch HTTP raw results given a URL.
 */
public class HttpWrapper {

    byte[] fetchResultBytes(String url) {
        byte[] byteArray;
        HttpResponse httpResponse;
        HttpClient httpClient = new DefaultHttpClient();

        try {
            httpResponse = httpClient.execute(new HttpGet(url));
            byteArray = EntityUtils.toByteArray(httpResponse.getEntity());
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
