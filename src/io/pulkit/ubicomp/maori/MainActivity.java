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

        AbstractClassifier atmosphereClassifier = new Maori().getClassifier("barometer-model.model");
        Log.i("maori-sample-app", "Fetched classifier " + atmosphereClassifier.getClass().toString());
    }

    public void evaluateLocalModel() {
        HttpClient httpClient = new DefaultHttpClient();
        String result;
        System.out.println("LOG_CHK: Fetching model from server.");
        try {
            result = httpClient.execute(
                    new HttpGet("http://128.237.200.224:9979/maori-server/model/get?modelId=naive-bayes-model.model"), new BasicResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectInputStream ois = null;
        try {
            Log.e("Weka", "Deserializing model");
            long t1 = new Date().getTime();
            ois = new ObjectInputStream(new StringBufferInputStream(result));
            NaiveBayes model = (NaiveBayes)ois.readObject();
            Log.e("Weka", "Time taken to read model : " + (new Date().getTime() - t1));

            Log.e("Weka", "Reading testing data.");
            ConverterUtils.DataSource dataSource =
                    new ConverterUtils.DataSource(getResources().openRawResource(R.raw.accelerometer_data_top100));
            t1 = new Date().getTime();
            Instances accelerometerInstances = dataSource.getDataSet();
            Log.e("Weka", "Time taken to load dataset : " + (new Date().getTime() - t1));
            if (accelerometerInstances.classIndex() == -1)
                accelerometerInstances.setClassIndex(accelerometerInstances.numAttributes() - 1);

            Log.e("Weka", "Predicting using downloaded model.");
            int i = 0;
            t1 = new Date().getTime();
            for (Instance instance : accelerometerInstances) {
                model.classifyInstance(instance);

                i++;
                System.out.println("Classified " + i + " instances.");
            }
            Log.e("Weka", "Time taken to classify data : " + (new Date().getTime() - t1));

//            Evaluation evaluation = new Evaluation(accelerometerInstances);
//            evaluation.evaluateModel(model, accelerometerInstances);
//            String classDetailsString = evaluation.toSummaryString();
//            System.out.println(classDetailsString);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
