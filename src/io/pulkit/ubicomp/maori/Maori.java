package io.pulkit.ubicomp.maori;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.StringBufferInputStream;

import weka.classifiers.AbstractClassifier;

public class Maori {

    private final String TAG = "Maori";

    public Object getModel(String modelName) {
        Log.i(TAG, "Fetching classifier " + modelName);

        HttpClient httpClient = new DefaultHttpClient();
        String result;
        Log.d(TAG, "Downloading model");
        try {
            // TODO: extract IP to configuration.
            result = httpClient.execute(new HttpGet(
                            "http://128.237.200.224:9979/maori-server/model/get?modelId=" + modelName),
                    new BasicResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectInputStream ois = null;
        Object model;
        Log.d(TAG, "Deserializing model");
        try {
            ois = new ObjectInputStream(new StringBufferInputStream(result));
            model = ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (OptionalDataException e) {
            throw new RuntimeException(e);
        } catch (StreamCorruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return model;
    }

    public <T> T getModelT(String modelName) {
        throw new RuntimeException();
    }

    public AbstractClassifier getClassifier(String modelName) {
        return (AbstractClassifier)getModel(modelName);
    }
}
