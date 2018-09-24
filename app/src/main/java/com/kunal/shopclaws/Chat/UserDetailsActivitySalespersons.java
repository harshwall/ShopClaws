package com.kunal.shopclaws.Chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;

public class UserDetailsActivitySalespersons extends AppCompatActivity {
    protected ListView ListViewSalesperson;
    private DatabaseReference mref1;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ListViewSalesperson=findViewById(R.id.listView1);
        user_id =getIntent().getStringExtra("user_id");
        mref1 = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseListAdapter<BlogUser> firebaseListAdapter=new FirebaseListAdapter<BlogUser>(this,BlogUser.class,android.R.layout.simple_list_item_1,mref1) {
            @Override
            protected void populateView(View v, final BlogUser model, int position) {
                TextView textView = v.findViewById(android.R.id.text1);
                if(!model.getMobile().equals(user_id))
                textView.setText(model.getName()+"("+model.getMobile()+")");
                else
                UserDetails.username=model.getName()+"("+model.getMobile()+")";
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserDetails.chatWith=model.getName()+"("+model.getMobile()+")";
                        startActivity(new Intent(UserDetailsActivitySalespersons.this,VizAViz.class));
                    }
                });
            }
        };
        ListViewSalesperson.setAdapter(firebaseListAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
