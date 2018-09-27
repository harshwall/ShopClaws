package com.kunal.shopclaws.Chat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.fragments.Blog;
import com.kunal.shopclaws.fragments.ImageListFragmentAdmin;

public class UserDetailsActivityManagers extends AppCompatActivity {
    private DatabaseReference mref2;
    private RecyclerView RecViewManager;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_managers);
        RecViewManager = findViewById(R.id.listView2);
        RecViewManager.setHasFixedSize(true);
        //to click on item with item
       RecViewManager.setItemAnimator(new DefaultItemAnimator());
        RecViewManager.setLayoutManager(new LinearLayoutManager(this));
        user_id=getIntent().getStringExtra("user_id");
        mref2 = FirebaseDatabase.getInstance().getReference().child("admin");
        FirebaseRecyclerAdapter<BlogUser,BlogViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<BlogUser, BlogViewHolder>
                (BlogUser.class, R.layout.chatlist_manager, BlogViewHolder.class, mref2) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, BlogUser model, final int position) {
                if(model.getMobile()!=null)
                    if(!model.getMobile().equals(user_id)) {
                        viewHolder.textView.setText(model.getName());
                        viewHolder.textView1.setText(model.getMobile());
                        viewHolder.img.setImageResource(R.drawable.manager);
                    }
                    else {
                        UserDetails.username = model.getName() + "(" + model.getMobile() + ")";
                        viewHolder.mView.setVisibility(View.GONE);
                    }
                    else
                        viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BlogUser user=getItem(position);
                            UserDetails.chatWith=user.getName()+"("+user.getMobile()+")";
                            startActivity(new Intent(UserDetailsActivityManagers.this,VizAViz.class));
                        }
                    });
            }
        };
        RecViewManager.setAdapter(firebaseRecyclerAdapter);

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
