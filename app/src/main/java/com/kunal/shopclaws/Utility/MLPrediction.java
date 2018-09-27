package com.kunal.shopclaws.Utility;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.kunal.shopclaws.Inventories.SellerInventory;
import com.kunal.shopclaws.R;

import java.util.Calendar;

public class MLPrediction extends AppCompatActivity {
    FirebaseModelInputOutputOptions inputOutputOptions;
    FirebaseModelInterpreter firebaseInterpreter;
    FirebaseModelInputs inputs;
    ArrayList<String> name,stats;
    DatabaseReference mDatabase;
    float probabilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlprediction);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(SellerInventory.user_id);
        name = new ArrayList<String>();
        stats = new ArrayList<String>();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        final String date2 = df.format("yyyy-MM-dd", new Date()).toString();
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
                                             probabilities = output[0];
                                            stats.add(Float.toString(probabilities));
                                            name.add("Predicted Sales");
                                            solve();

                                            //Toast.makeText(MLPrediction.this, ""+probabilities, Toast.LENGTH_SHORT).show();

                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                             probabilities=prediction(10000,20000);
                                            //Toast.makeText(MLPrediction.this,""+probabilities,Toast.LENGTH_SHORT).show();
                                            stats.add(Float.toString(probabilities));
                                            name.add("Predicted Sales");
                                            solve();
                                            // ...
                                        }
                                    });
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object data  = dataSnapshot.child("SalesData").child(date2).child("sales").getValue();
                if(data!=null)
                {
                    stats.add(data.toString());
                }
                else
                    stats.add("0");
                name.add("Current Sales");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void solve()
    {
        BarChart lineChart = (BarChart) findViewById(R.id.predict);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i< stats.size();i++) {
            String valspi = stats.get(i);
            if (valspi.equals("n/a"))
                valspi = "0";
            entries.add(new BarEntry(Float.parseFloat(valspi), i));
        }

        BarDataSet dataset = new BarDataSet(entries, "Sales");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0;i < stats.size();i++) {
            labels.add(name.get(i));
        }
        BarData data = new BarData( labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        lineChart.setData(data);
        lineChart.animateY(2000);
    }


    public static int prediction(int min,int max){
        Random random=new Random();
        return random.nextInt((max - min) + 1) + min;

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}











