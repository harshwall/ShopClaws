package com.kunal.shopclaws.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;

public class Seller_Profiles extends AppCompatActivity {
protected RecyclerView recyclerView;
private DatabaseReference mDatabase;
AlertDialog CustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(Seller_Profiles.this, ImagePipelineConfigFactory.getImagePipelineConfig(Seller_Profiles.this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__profiles);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users");
        recyclerView=findViewById(R.id.recyclerviewadmin);
        //to set the fix size of recycler view
        recyclerView.setHasFixedSize(true);
        //to click on item with item
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //to take the recycler view in linear form
        /*recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        //to take the recycler view in grid form
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        //to call the adapter class in recycler view
        FirebaseRecyclerAdapter<BlogProfile,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<BlogProfile, BlogViewHolder>
                (BlogProfile.class,R.layout.profile,BlogViewHolder.class,mDatabase) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, BlogProfile model, int position) {
                final String name=model.getName();
                final String mobile=model.getMobile();
                final String imguri=model.getImg();
                final String verify=model.getVerify();
                //Toast.makeText(Seller_Profiles.this, name, Toast.LENGTH_SHORT).show();
                if(name!=null)
                viewHolder.profname.setText("Username: "+name);
                viewHolder.profphone.setText("Phone Number: "+mobile);
                if(imguri!=null)
                    viewHolder.img.setImageURI(Uri.parse(imguri));
                if(verify!=null && verify.equals("1")) {
                    viewHolder.profverify.setText("Verified");
                    viewHolder.profvericon.setImageResource(R.drawable.ic_verified_user_black_24dp);
                    viewHolder.profverify.setTextColor(R.color.green_light);
                }
                viewHolder.profverify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(verify.equals("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Seller_Profiles.this);
                            builder.setMessage("Are you sure you want to verify this user?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    viewHolder.profverify.setTextColor(R.color.green_light);
                                    viewHolder.profverify.setText("Verified");
                                    viewHolder.profvericon.setImageResource(R.drawable.ic_verified_user_black_24dp);
                                    FirebaseDatabase.getInstance().getReference().child("users").child(mobile).child("verify").setValue("1");
                                    dialogInterface.cancel();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            CustomDialog = builder.create();
                            CustomDialog.show();
                        }
                    }
                });
            }
        };
        //to attach the data into recycler view
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        SimpleDraweeView img;
        ImageView profvericon;
        TextView profname,profphone,profverify;


        public BlogViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            img=mView.findViewById(R.id.profimg);
            profname=mView.findViewById(R.id.profname);
            profphone=mView.findViewById(R.id.profphone);
            profverify=mView.findViewById(R.id.profverify);
            profvericon=mView.findViewById(R.id.profver_icon);

        }


    }

}
