package com.kunal.shopclaws.Inventories;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;

import java.util.Calendar;
import java.util.Date;

public class ItemDetailsActivity extends AppCompatActivity {
private String item_name,item_desc,url,key;
private int stock;
private SimpleDraweeView image;
private int category;
private String verify="0";
private TextView etitem_name,etitem_price,etitem_desc,etsold,etremove;
private DatabaseReference mDatabase;
private AlertDialog CustomDialog;
private int solditems,item_price;
String date2;
long revenue,dailysale;
    private Boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        Fresco.initialize(ItemDetailsActivity.this, ImagePipelineConfigFactory.getImagePipelineConfig(ItemDetailsActivity.this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        item_name=getIntent().getStringExtra("title");
        key=getIntent().getStringExtra("key");
        item_price=Integer.parseInt(getIntent().getStringExtra("price"));
        item_desc=getIntent().getStringExtra("desc");
        category=getIntent().getIntExtra("category",0);
        stock=getIntent().getIntExtra("stock",10);
        url=getIntent().getStringExtra("url");
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        date2 = df.format("yyyy-MM-dd", new Date()).toString();
        image=findViewById(R.id.image_url);
        etitem_desc=findViewById(R.id.descript);
        etitem_name=findViewById(R.id.item_name);
        etitem_price=findViewById(R.id.item_price);
        etsold=findViewById(R.id.text_action_bottom2);
        etremove=findViewById(R.id.text_action_bottom1);
        SharedPreferences sharedPreferences = getSharedPreferences("logDetails",
                Context.MODE_PRIVATE);
        flag = sharedPreferences.getBoolean("flag",false);
        //Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
        image.setImageURI(Uri.parse(url));
        etitem_name.append(": "+item_name);
        etitem_price.append(": \u20B9"+Integer.toString(item_price));
        etitem_desc.setText(item_desc);
        //checking item is sold or not on the first istance
        if(stock!=0)
        etsold.setText("UNSOLD");
        if (category == 1) {
            mDatabase=FirebaseDatabase.getInstance().getReference().child("admin").child("Fashion").child(key);
        } else if (category == 2) {
           mDatabase= FirebaseDatabase.getInstance().getReference().child("admin").child("Books").child(key);
        } else if (category == 3) {
            mDatabase=FirebaseDatabase.getInstance().getReference().child("admin").child("Electronics").child(key);
        } else if (category == 4) {
            mDatabase=FirebaseDatabase.getInstance().getReference().child("admin").child("Furniture").child(key);
        } else if (category == 5) {
            mDatabase=FirebaseDatabase.getInstance().getReference().child("admin").child("Others").child(key);
        }
        if(SellerInventory.user_id!=null)
        FirebaseDatabase.getInstance().getReference().child("users").child(SellerInventory.user_id).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     Object data = dataSnapshot.child("verify").getValue();
                     if(data!=null)
                     verify=data.toString();
                     if(dataSnapshot.child("solditems").getValue()!=null)
                     solditems = Integer.parseInt(dataSnapshot.child("solditems").getValue().toString());
                    if(dataSnapshot.child("revenue").getValue()!=null)
                        revenue = (long) dataSnapshot.child("revenue").getValue();
                    if(dataSnapshot.child("SalesData").child(date2)!=null)
                        dailysale = (long) dataSnapshot.child("SalesData").child(date2).child("sales").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(ItemDetailsActivity.this, key, Toast.LENGTH_SHORT).show();
        //Updating Sold Status!
        etremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);
                    builder.setMessage("Are you sure you want to remove this item?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDatabase.removeValue();
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
                else
                {
                    Toast.makeText(ItemDetailsActivity.this, "Only admin can remove this item!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        etsold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Admin cannot change the sold status
                if(!flag)
                {
                        if (verify.equals("0"))
                            Toast.makeText(ItemDetailsActivity.this, "Sorry! You are not verified!", Toast.LENGTH_SHORT).show();
                            //Seller updating the sold status
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);
                            builder.setMessage("Are you sure this item is sold?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    etsold.setText("SOLD");
                                    stock--;
                                    //Toast.makeText(ItemDetailsActivity.this, Integer.toString(stock), Toast.LENGTH_SHORT).show();
                                    mDatabase.child("stock_size").setValue(Integer.toString(stock));
                                    FirebaseDatabase.getInstance().getReference().child("users").
                                            child(SellerInventory.user_id).child("solditems").setValue(Integer.toString(solditems+1));
                                    FirebaseDatabase.getInstance().getReference().child("users").
                                            child(SellerInventory.user_id).child("revenue").setValue(item_price+revenue);
                                    FirebaseDatabase.getInstance().getReference().child("users").child(SellerInventory.user_id).
                                            child("SalesData").child(date2).child("sales").setValue(item_price+dailysale);
                                    if(stock==0)
                                        mDatabase.removeValue();

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
                else
                {
                    //If Admin by-mistakes clicks on sold option!
                    Toast.makeText(ItemDetailsActivity.this, "Only Salesperson can update this!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
