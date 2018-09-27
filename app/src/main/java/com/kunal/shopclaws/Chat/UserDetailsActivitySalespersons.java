package com.kunal.shopclaws.Chat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;

public class UserDetailsActivitySalespersons extends AppCompatActivity {
    protected RecyclerView RecViewSalesperson;
    private DatabaseReference mref1;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext(), ImagePipelineConfigFactory.getImagePipelineConfig(getApplicationContext()));
        setContentView(R.layout.activity_user_details_salesperson);
        RecViewSalesperson = findViewById(R.id.listView1);
        RecViewSalesperson.setHasFixedSize(true);
        //to click on item with item
        RecViewSalesperson.setItemAnimator(new DefaultItemAnimator());
        RecViewSalesperson.setLayoutManager(new LinearLayoutManager(this));
        user_id =getIntent().getStringExtra("user_id");
        mref1 = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseRecyclerAdapter<BlogUser,BlogViewHolder> firebaseRecyclerAdapter = null;
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<BlogUser,BlogViewHolder>
                (BlogUser.class, R.layout.chatlist_manager,BlogViewHolder.class, mref1) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, final BlogUser model, final int position) {
                if(model.getMobile()!=null)
                    if(!model.getMobile().equals(user_id)) {
                        viewHolder.textView.setText(model.getName());
                        viewHolder.textView1.setText(model.getMobile());
                        if(model.getImg()!=null)
                        viewHolder.img.setImageURI(Uri.parse(model.getImg()));
                    }
                    else {
                        UserDetails.username = model.getName() + "(" + model.getMobile() + ")";
                        viewHolder.textView.setText(model.getName());
                        viewHolder.textView1.setText(model.getMobile());
                        if(model.getImg()!=null)
                            viewHolder.img.setImageURI(Uri.parse(model.getImg()));
                    }
                else
                    viewHolder.mView.setVisibility(View.GONE);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!model.getMobile().equals(user_id)) {
                            BlogUser user = getItem(position);
                            UserDetails.chatWith = user.getName() + "(" + user.getMobile() + ")";
                            Intent i = new Intent(UserDetailsActivitySalespersons.this, VizAViz.class);
                            i.putExtra("imgchat",model.getImg());
                            startActivity(i);
                        }
                        else
                            Toast.makeText(UserDetailsActivitySalespersons.this, "You cannot chat with yourself!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        RecViewSalesperson.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        SimpleDraweeView img;
        TextView textView,textView1;

        public BlogViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            img = mView.findViewById(R.id.profimg_mgr);
            textView = mView.findViewById(R.id.mgr_name);
            textView1 = mView.findViewById(R.id.mgr_phone);
        }


    }
}
