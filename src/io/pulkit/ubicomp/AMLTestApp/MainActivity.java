package io.pulkit.ubicomp.AMLTestApp;

import android.app.Activity;
import android.os.Bundle;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringBufferInputStream;

import smile.Network;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        HttpClient httpClient = new DefaultHttpClient();
        String result;
        try {
            result = httpClient.execute(
                    new HttpGet("http://128.237.221.65:9979/ml_server/model/get"), new BasicResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new StringBufferInputStream(result));
            NaiveBayesUpdateable naiveBayesUpdateable = (NaiveBayesUpdateable)ois.readObject();
            System.out.println(naiveBayesUpdateable.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
