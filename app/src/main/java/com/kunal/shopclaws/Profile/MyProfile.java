package com.kunal.shopclaws.Profile;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kunal.shopclaws.Inventories.SellerInventory;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MyProfile extends AppCompatActivity {
    protected String userphone;
    protected SimpleDraweeView img;
    private Uri imgUri;
    protected StorageReference mStorageRef;
    protected TextView prof_name,prof_phone,prof_verify,tvsolditems,tvrevenue;
    protected DatabaseReference mDatabase;
    public static final String FB_STORAGE_PATH = "image/";
    public static final int REQUEST_CODE = 1234;
    private ImageView prof_ver_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Fresco.initialize(MyProfile.this, ImagePipelineConfigFactory.getImagePipelineConfig(MyProfile.this));
        //receiving user_id from seller inventory through intent
        userphone=getIntent().getStringExtra("user_id");
        prof_name=findViewById(R.id.profname);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        img=findViewById(R.id.profimg);
        prof_phone=findViewById(R.id.profphone);
        tvrevenue=findViewById(R.id.revenue);
        tvsolditems=findViewById(R.id.sold_item);
        prof_verify=findViewById(R.id.verify);
        prof_phone.append(userphone);
        prof_ver_icon = (ImageView)findViewById(R.id.profver_icon);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(userphone);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //fetching image and username
                String merchant=dataSnapshot.child("name").getValue().toString();
                Object imgfetch=dataSnapshot.child("img").getValue();
                String verify = dataSnapshot.child("verify").getValue().toString();
                String revenue = dataSnapshot.child("revenue").getValue().toString();
                String solditems = dataSnapshot.child("solditems").getValue().toString();
                if(imgfetch!=null)
                {
                    img.setImageURI(Uri.parse(imgfetch.toString()));
                }
                prof_name.setText("Username: "+merchant);
                tvrevenue.setText(revenue);
                tvsolditems.setText(solditems);
                if(verify.equals("1"))
                {
                    prof_verify.setText("Verified");
                    prof_verify.setTextColor(R.color.green_light);
                    prof_ver_icon.setImageResource(R.drawable.ic_verified_user_black_24dp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
            imgUri = data.getData();

        if(imgUri!=null) {
            try {
                //resizing image to fit on that particular imageview
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                img.setImageBitmap(bm);
                final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));
                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getApplicationContext(), "image uploaded", Toast.LENGTH_LONG).show();
                        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String downloadurl = task.getResult().toString();
                                FirebaseDatabase.getInstance().getReference().child("users").child(userphone).child("img").setValue(downloadurl);
                                Toast.makeText(MyProfile.this, "Profile Picture Updated!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    //getting resolved image
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyProfile.this , SellerInventory.class));
        finish();
    }
}
