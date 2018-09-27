package com.kunal.shopclaws.Utility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.Inventories.AdminInventory;
import com.kunal.shopclaws.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PushNotification extends AppCompatActivity {
    Button b;
    EditText et;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        b=findViewById(R.id.button4);
        et=findViewById(R.id.editText);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        final String formattedDate = df.format(c.getTime());
        ref= FirebaseDatabase.getInstance().getReference().child("admin").child("notifications");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=et.getText().toString();
                String key=ref.push().getKey();
                ref.child(key).child("message").setValue(s);
                ref.child(key).child("timestamp").setValue(formattedDate);
                ref.child(key).child("sender").setValue(AdminInventory.user_name);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
