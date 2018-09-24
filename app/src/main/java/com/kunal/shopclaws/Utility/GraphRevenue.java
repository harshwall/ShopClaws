package com.kunal.shopclaws.Utility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import com.kunal.shopclaws.R;

import java.util.ArrayList;

public class GraphRevenue extends AppCompatActivity {

    private DatabaseReference mDatabase;
    ArrayList<String> name,stats,stats2;
    ValueEventListener vel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        name = new ArrayList<String>();
        stats = new ArrayList<String>();
        stats2 = new ArrayList<String>();

        vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userDetails : dataSnapshot.getChildren()) {
                    name.add(userDetails.child("name").getValue().toString());
                    stats.add(userDetails.child("revenue").getValue().toString());
                    stats2.add(userDetails.child("solditems").getValue().toString());
                }
                solve();
                solve2();
                mDatabase.removeEventListener(vel);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(vel);
    }
    public void solve()
    {
        BarChart lineChart = (BarChart) findViewById(R.id.chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i< stats.size();i++) {
            String valspi = stats.get(i);
            if (valspi.equals("n/a"))
                valspi = "0";
            entries.add(new BarEntry(Float.parseFloat(valspi), i));
        }

        BarDataSet dataset = new BarDataSet(entries, "Revenue");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0;i < stats.size();i++) {
            labels.add(name.get(i));
        }
        BarData data = new BarData( labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        lineChart.setData(data);
        lineChart.animateY(2000);
    }
    public void solve2()
    {
        LineChart lineChart = (LineChart) findViewById(R.id.chart2);
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i< stats2.size();i++) {
            String valspi = stats2.get(i);
            if (valspi.equals("n/a"))
                valspi = "0";
            else if(valspi.equals(""))
                valspi = "0";
            else
                entries.add(new Entry(Integer.parseInt(valspi), i));
        }

        LineDataSet dataset = new LineDataSet(entries, "SoldItems");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0;i < stats2.size();i++)
        {
            labels.add(name.get(i));
        }

        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(2000);
    }

}

