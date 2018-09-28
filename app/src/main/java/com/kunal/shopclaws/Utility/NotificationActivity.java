package com.kunal.shopclaws.Utility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.Chat.UserDetailsActivityManagers;
import com.kunal.shopclaws.R;

public class NotificationActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private RecyclerView recnotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("admin").child("notifications");
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recnotification=findViewById(R.id.recnotif);
        recnotification.setHasFixedSize(true);
        recnotification.setItemAnimator(new DefaultItemAnimator());
        recnotification.setLayoutManager(llm);
        reference = FirebaseDatabase.getInstance().getReference().child("admin").child("notifications");
        FirebaseRecyclerAdapter<NotificationData,BlogViewHolder> firebaseRecyclerAdapter =new
                FirebaseRecyclerAdapter<NotificationData, BlogViewHolder>
                        (NotificationData.class,R.layout.notifview,BlogViewHolder.class,reference) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, NotificationData model, int position) {
                        viewHolder.img_sender.setImageResource(R.drawable.manager);
                        viewHolder.sender.setText(model.getSender());
                        viewHolder.notification.setText(model.getMessage());
                        viewHolder.time.setText(model.getTimestamp());
                    }
                };
        recnotification.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        SimpleDraweeView img_sender;
        TextView sender,notification,time;


        public BlogViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            img_sender=mView.findViewById(R.id.notif_mgr);
            sender=mView.findViewById(R.id.notif_sender);
            notification=mView.findViewById(R.id.notif_detail);
            time=mView.findViewById(R.id.timestamp);
        }


    }
}
