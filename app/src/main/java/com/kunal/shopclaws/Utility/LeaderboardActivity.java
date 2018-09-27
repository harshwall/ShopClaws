package com.kunal.shopclaws.Utility;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kunal.shopclaws.R;

public class LeaderboardActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    TextView tvrank,tv2rank;
    SimpleDraweeView imgrank;
    String maximg;
    int maxrev=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        tvrank=findViewById(R.id.tv_rank);
        tv2rank=findViewById(R.id.tv2_rank);
        imgrank=findViewById(R.id.img_rank);
        recyclerView=findViewById(R.id.recycle);
        //to set the fix size of recycler view
        recyclerView.setHasFixedSize(true);
        //to click on item with item
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //to take the recycler view in linear form
        /*recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        //to take the recycler view in grid form
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        Query qref = databaseReference.orderByChild("revenue").limitToLast(10);
        FirebaseRecyclerAdapter<data,MyHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<data, MyHolder>
                (data.class,R.layout.leaderboard_item,MyHolder.class,qref) {
            @Override
            protected void populateViewHolder(MyHolder viewHolder, data model, final int position) {
                final String name = model.getName();
                //Toast.makeText(LeaderboardActivity.this, name, Toast.LENGTH_SHORT).show();
                final String revenue = model.getRevenue().toString();
                final Object imguri = model.getImg();
                if(Integer.parseInt(revenue)>0) {
                    viewHolder.lbname.append(name);
                    viewHolder.lbrevenue.append(revenue);
                    if (imguri != null)
                        viewHolder.img.setImageURI(Uri.parse(imguri.toString()));
                    if (Integer.parseInt(revenue) > maxrev) {
                        maxrev = Integer.parseInt(revenue);
                        if (imguri != null)
                            maximg = imguri.toString();
                        if (maximg != null)
                            imgrank.setImageURI(Uri.parse(maximg));
                        tv2rank.setText(Integer.toString(maxrev));
                    }
                }
                else
                    viewHolder.mView.setVisibility(View.GONE);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(imguri!=null)
                        imgrank.setImageURI(Uri.parse(imguri.toString()));
                        tv2rank.setText(revenue);
                    }
                });

            }
            @Override
            public data getItem(int position) {
                return super.getItem(position);
            }


        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        TextView lbname;
        TextView lbrevenue;
        SimpleDraweeView img;


        public MyHolder(@NonNull View itemView) {
            //to typecast the widgets
            super(itemView);
            mView=itemView;
            lbname=mView.findViewById(R.id.lb_name);
            img = mView.findViewById(R.id.profimg1);
            lbrevenue=mView.findViewById(R.id.lb_revenue);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
