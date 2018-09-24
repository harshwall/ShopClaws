package com.kunal.shopclaws.Utility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;

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
        ref= FirebaseDatabase.getInstance().getReference().child("admin").child("notifications");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=et.getText().toString();
                ref.push().setValue(s);
            }
        });
    }

}
