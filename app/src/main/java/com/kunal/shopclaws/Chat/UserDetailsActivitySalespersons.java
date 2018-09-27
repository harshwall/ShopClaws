package com.kunal.shopclaws.Chat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;

public class UserDetailsActivitySalespersons extends AppCompatActivity {
    protected ListView ListViewSalesperson;
    private DatabaseReference mref1;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext(), ImagePipelineConfigFactory.getImagePipelineConfig(getApplicationContext()));
        setContentView(R.layout.activity_user_details_salesperson);
        ListViewSalesperson = findViewById(R.id.listView1);
        user_id =getIntent().getStringExtra("user_id");
        mref1 = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseListAdapter<BlogUser> firebaseListAdapter=new FirebaseListAdapter<BlogUser>(this,BlogUser.class,R.layout.chatlist_manager,mref1) {
            @Override
            protected void populateView(View v, final BlogUser model, int position) {
                TextView textView = v.findViewById(R.id.mgr_name);
                TextView textView1 = v.findViewById(R.id.mgr_phone);
                SimpleDraweeView img = v.findViewById(R.id.profimg_mgr);
                if(model.getMobile()!=null)
                if(!model.getMobile().equals(user_id)) {
                    textView.setText(model.getName());
                    textView1.setText(model.getMobile());
                    if(model.getImg()!=null)
                    img.setImageURI(Uri.parse(model.getImg()));
                }
                else {
                    UserDetails.username = model.getName() + "(" + model.getMobile() + ")";
                }
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
