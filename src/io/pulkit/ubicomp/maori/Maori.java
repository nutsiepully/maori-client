package io.pulkit.ubicomp.maori;

import android.content.Context;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.StringBufferInputStream;

import weka.classifiers.AbstractClassifier;

public class Maori {

    private final String TAG = "maori";

    private ModelSynchronizer modelSynchronizer;
    private AllModels allModels;

    public Maori(Context context) {
        this.modelSynchronizer = new ModelSynchronizer(context);
        this.allModels = new AllModels(context);
    }

    private Object getModel(String modelName) {
        Log.i(TAG, "Fetching classifier " + modelName);

        Model modelDb = this.allModels.getActive(modelName);

        Object model;
        Log.d(TAG, "Deserializing model");
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(modelDb.getPayload()));
            model = objectInputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return model;
    }

    public AbstractClassifier getClassifier(String modelName) {
        return (AbstractClassifier)getModel(modelName);
    }

    public void refresh() {
        Log.i(TAG, "Refreshing model cache");

        this.modelSynchronizer.synchronize();
    }

}