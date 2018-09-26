package com.kunal.shopclaws.Utility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;

public class NotificationActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);
        listView=(ListView)findViewById(R.id.listview);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("admin").child("notifications");
        FirebaseListAdapter<String> firebaseListAdapter=new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                databaseReference
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView=v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };
        listView.setAdapter(firebaseListAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
