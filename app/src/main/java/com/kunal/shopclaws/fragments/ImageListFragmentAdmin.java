/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunal.shopclaws.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.Inventories.ItemDetailsActivity;
import com.kunal.shopclaws.Inventories.SellerInventory;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;


public class ImageListFragmentAdmin extends Fragment {

    public static final String STRING_IMAGE_URI = "ImageUri";
    public static final String STRING_IMAGE_POSITION = "ImagePosition";
    public static DatabaseReference mDatabase,mref;
    public static int category;
    private  RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(getActivity(), ImagePipelineConfigFactory.getImagePipelineConfig(getActivity()));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       rv = (RecyclerView) inflater.inflate(R.layout.layout_recylerview_list, container, false);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        //View Pager set on which fragment
        category = ImageListFragmentAdmin.this.getArguments().getInt("type");
        if (category == 1){
            mDatabase= FirebaseDatabase.getInstance().getReference().child("admin").child("Fashion");
        }else if (category == 2){
            mDatabase= FirebaseDatabase.getInstance().getReference().child("admin").child("Books");
        }else if (category == 3){
            mDatabase= FirebaseDatabase.getInstance().getReference().child("admin").child("Electronics");
        }else if (category == 4){
            mDatabase= FirebaseDatabase.getInstance().getReference().child("admin").child("Furniture");
        }else if (category == 5){
            mDatabase= FirebaseDatabase.getInstance().getReference().child("admin").child("Others");
        }
        //Fetching items through firebase recycler adapter.
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>
                (Blog.class,R.layout.list_item,BlogViewHolder.class,mDatabase) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void populateViewHolder( BlogViewHolder viewHolder, Blog model,final int position) {
                final String url=model.getUrl();
                final String title=model.getTitle();
                final String price=model.getPrice();
                final String desc=model.getDesc();
                //Setting different details like itemName, itemPrice, StockSize and Image on a view in Recycler View.
                viewHolder.post_image.setImageURI(url);
                viewHolder.item_name.setText("Item Name: "+title);
                viewHolder.item_price.setText("Item Price: \u20B9"+price);
                final int stock = Integer.parseInt(model.getStock_size());
                viewHolder.stock_size.setText("Stock Size: "+stock);
                if(stock<=5)
                    viewHolder.stock_size.setTextColor(R.color.red);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mref=getRef(position);
                        Intent i= new Intent(getActivity(),ItemDetailsActivity.class);
                        //Sending item details in item details activity.
                        i.putExtra("url",url);
                        i.putExtra("title",title);
                        i.putExtra("desc",desc);
                        i.putExtra("price",price);
                        i.putExtra("stock",stock);
                        i.putExtra("key",mref.getKey());
                        i.putExtra("category",ImageListFragmentAdmin.this.getArguments().getInt("type"));
                        getActivity().startActivity(i);
                    }
                });
            }
        };
        rv.setAdapter(firebaseRecyclerAdapter);
        return rv;
    }


    @Override
    public void onStart() {
        super.onStart();

    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        SimpleDraweeView post_image;
        TextView item_name,item_price,stock_size;

        public BlogViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            post_image=mView.findViewById(R.id.image1);
            item_name=mView.findViewById(R.id.item_name);
            item_price=mView.findViewById(R.id.item_price);
            stock_size=mView.findViewById(R.id.sold_status);
        }


    }

}
