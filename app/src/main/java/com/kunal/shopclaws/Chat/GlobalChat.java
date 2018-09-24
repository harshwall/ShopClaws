package com.kunal.shopclaws.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;

public class GlobalChat extends AppCompatActivity {

    private TextView tv;
    private ImageView send;
    private DatabaseReference ref;
    private String username;
    private boolean flag;
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);
        tv=findViewById(R.id.messageArea);
        send=findViewById(R.id.sendButton);
        listview=findViewById(R.id.list);
        flag=getIntent().getBooleanExtra("Flag",false);
        if(flag)
            username=getIntent().getStringExtra("username")+"(Manager)";
        else
            username=getIntent().getStringExtra("username");

            ref= FirebaseDatabase.getInstance().getReference().child("Chat");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv.getText().toString().equals(""))
                {
                    Toast.makeText(GlobalChat.this, "Write some Text", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ref.push().setValue(username+": "+tv.getText().toString());
                    tv.setText("");
                }
            }
        });

        FirebaseListAdapter<String> firebaseListAdapter=new FirebaseListAdapter<String>(this,String.class,android.R.layout.simple_list_item_1,ref) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView=v.findViewById(android.R.id.text1);
                textView.setText(model);
                if(model.startsWith(username+":"))
                {
                    textView.setGravity(Gravity.LEFT);
                }
                else
                    textView.setGravity(Gravity.RIGHT);
            }
        };
        listview.setAdapter(firebaseListAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
