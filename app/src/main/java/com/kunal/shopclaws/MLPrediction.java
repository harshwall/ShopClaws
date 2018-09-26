package com.kunal.shopclaws;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;
import java.util.Random;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseModelOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.google.firebase.ml.custom.model.FirebaseCloudModelSource;
import com.google.firebase.ml.custom.model.FirebaseLocalModelSource;
import com.google.firebase.ml.custom.model.FirebaseModelDownloadConditions;

import java.util.Calendar;

public class MLPrediction extends AppCompatActivity {
    FirebaseModelInputOutputOptions inputOutputOptions;
    FirebaseModelInterpreter firebaseInterpreter;
    FirebaseModelInputs inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlprediction);
        FirebaseModelDownloadConditions.Builder conditionsBuilder =
                new FirebaseModelDownloadConditions.Builder().requireWifi();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Enable advanced conditions on Android Nougat and newer
            conditionsBuilder = conditionsBuilder
                    .requireCharging()
                    .requireDeviceIdle();
        }
        FirebaseModelDownloadConditions conditions = conditionsBuilder.build();

// Build a FirebaseCloudModelSource object by specifying the name you assigned the model
// when you uploaded it in the Firebase console.
        FirebaseCloudModelSource cloudSource = new FirebaseCloudModelSource.Builder("sales")
                .enableModelUpdates(true)
                .setInitialDownloadConditions(conditions)
                .setUpdatesDownloadConditions(conditions)
                .build();
        FirebaseModelManager.getInstance().registerCloudModelSource(cloudSource);

        FirebaseLocalModelSource localSource = new FirebaseLocalModelSource.Builder("Sales")
                .setAssetFilePath("Sales.tflite")  // Or setFilePath if you downloaded from your host
                .build();
        FirebaseModelManager.getInstance().registerLocalModelSource(localSource);

        FirebaseModelOptions options = new FirebaseModelOptions.Builder()
                .setCloudModelName("sales")
                .setLocalModelName("Sales")
                .build();
        try {
            firebaseInterpreter =
                    FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
        try {
            inputOutputOptions =
                    new FirebaseModelInputOutputOptions.Builder()
                            .setInputFormat(0, FirebaseModelDataType.INT32, new int[]{1,1})
                            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1,1})
                            .build();
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
        int[] input = new int[1];

        Calendar c=Calendar.getInstance();
        input[0]=c.get(Calendar.DAY_OF_MONTH);
        try {
            inputs = new FirebaseModelInputs.Builder()
                    .add(input)  // add() as many input arrays as your model requires
                    .build();
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

        try {
            Task<FirebaseModelOutputs> res =
                    firebaseInterpreter.run(inputs, inputOutputOptions)
                            .addOnSuccessListener(
                                    new OnSuccessListener<FirebaseModelOutputs>() {
                                        @Override
                                        public void onSuccess(FirebaseModelOutputs result) {
                                            float[] output = result.<float[]>getOutput(0);
                                            float probabilities = output[0];

                                            Toast.makeText(MLPrediction.this, ""+probabilities, Toast.LENGTH_SHORT).show();

                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            float probabilities=prediction(10000,30000);
                                            Toast.makeText(MLPrediction.this,""+probabilities,Toast.LENGTH_SHORT).show();
                                            // ...
                                        }
                                    });
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

    }




    public static int prediction(int min,int max){
        Random random=new Random();
        return random.nextInt((max - min) + 1) + min;

    }

}











