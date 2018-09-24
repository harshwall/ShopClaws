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

public class UserDetailsActivityManagers extends AppCompatActivity {
    private DatabaseReference mref2;
    private ListView ListViewManager;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_managers);
        ListViewManager = findViewById(R.id.listView2);
        user_id=getIntent().getStringExtra("user_id");
        mref2 = FirebaseDatabase.getInstance().getReference().child("admin");
        FirebaseListAdapter<BlogUser> firebaseListAdapter2=new FirebaseListAdapter<BlogUser>(this,BlogUser.class,android.R.layout.simple_list_item_1,mref2) {
            @Override
            protected void populateView(View v, final BlogUser model, int position) {
                TextView textView = v.findViewById(android.R.id.text1);
                if(model.getMobile()!=null)
                    if(!model.getMobile().equals(user_id))
                        textView.setText(model.getName()+"("+model.getMobile()+")"+"(Manager)");
                    else
                        UserDetails.username=model.getName()+"("+model.getMobile()+")"+"(Manager)";
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserDetails.chatWith=model.getName()+"("+model.getMobile()+")"+"(Manager)";
                        startActivity(new Intent(UserDetailsActivityManagers.this,VizAViz.class));
                    }
                });
            }
        };
        ListViewManager.setAdapter(firebaseListAdapter2);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
