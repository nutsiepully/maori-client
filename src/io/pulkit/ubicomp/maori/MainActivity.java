package io.pulkit.ubicomp.maori;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringBufferInputStream;
import java.util.Date;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class MainActivity extends Activity
{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Maori maori = new Maori(this);
        maori.refresh();

        AbstractClassifier atmosphereClassifier = maori.getClassifier("barometer-model.model");
        Log.i("maori-sample-app", "Fetched classifier " + atmosphereClassifier);
    }
}
