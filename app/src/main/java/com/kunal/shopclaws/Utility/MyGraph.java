package com.kunal.shopclaws.Utility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.Inventories.SellerInventory;
import com.kunal.shopclaws.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MyGraph extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String userId, registrationnum;
    ArrayList<String> branch,stats;
    ValueEventListener vel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_graph);

        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(SellerInventory.user_id).child("SalesData");
        branch = new ArrayList<String>();
        stats = new ArrayList<String>();

        vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userDetails : dataSnapshot.getChildren()) {
                    Calendar c = Calendar.getInstance();
                    int month = c.get(Calendar.MONTH);
                    //Toast.makeText(MyGraph.this, Integer.toString(month), Toast.LENGTH_SHORT).show();
                    if(userDetails.getKey()!=null) {
                        String key = userDetails.getKey().toString();
                        String[] arr = key.split("-");
                        //Toast.makeText(MyGraph.this, arr[2], Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(arr[1]) == month + 1) {
                            branch.add(userDetails.getKey().toString());
                            stats.add(userDetails.child("sales").getValue().toString());
                        } else
                            userDetails.getRef().removeValue();
                    }
                }
                if(branch!=null)
                solve2();
                mDatabase.removeEventListener(vel);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(vel);
    }
    public void solve2()
    {
        LineChart lineChart = (LineChart) findViewById(R.id.mychart);
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i< stats.size();i++) {
            String valspi = stats.get(i);
            if (valspi.equals("n/a"))
                valspi = "0";
            else if(valspi.equals(""))
                valspi = "0";
            else
                entries.add(new Entry(Float.parseFloat(valspi), i));
        }

        LineDataSet dataset = new LineDataSet(entries, "Date");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0;i < stats.size();i++)
        {
            labels.add(branch.get(i));
        }

        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(2000);
    }

}
