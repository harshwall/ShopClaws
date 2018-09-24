package com.kunal.shopclaws.UploadImage;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kunal.shopclaws.Inventories.SellerInventory;
import com.kunal.shopclaws.R;

import java.io.FileNotFoundException;
import java.io.IOException;


public class UploadImage extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private Button buttonUpload;
    private EditText et_title;
    private EditText et_price;
    private EditText et_desc ;
    private EditText et_stcksize;
    public static final int REQUEST_CODE = 1234;
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRefadmin;
    private Uri imgUri;
    public static final String FB_STORAGE_PATH = "image/";
    private ImageView imageView;
    SharedPreferences sharedPreferences;
    Spinner spin ;
    String s1,username ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        sharedPreferences= getSharedPreferences("logDetails",
                Context.MODE_PRIVATE);
        username=sharedPreferences.getString("Phone",null);
        mDataBaseRefadmin = FirebaseDatabase.getInstance().getReference().child("admin");
        et_title=findViewById(R.id.title12);
        et_price=findViewById(R.id.Price12);
        et_desc=findViewById(R.id.desc);
        et_stcksize=findViewById(R.id.stock);
        buttonUpload = findViewById(R.id.buttonUpload);
        spin=findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UploadImage.this,R.array.status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);


        imageView = findViewById(R.id.imageView1);
        imageView.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if (v == imageView) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_CODE);

        }


        if(v == buttonUpload){
            if(s1.equals("Select Item"))
            {
                Toast.makeText(this, "Select Item First", Toast.LENGTH_SHORT).show();

            }
            else
            if(et_title.getText().toString().equals(""))
                Toast.makeText(this, "Write the title of the item", Toast.LENGTH_SHORT).show();
            else
                if(et_price.getText().toString().equals(""))
                    Toast.makeText(this, "Write the price of the item", Toast.LENGTH_SHORT).show();
            else
                if(et_stcksize.getText().toString().equals(""))
                    Toast.makeText(this, "Mention size of stock", Toast.LENGTH_SHORT).show();
            else
            {
                final String desc=et_desc.getText().toString().trim();
                final String title=et_title.getText().toString().trim();
                final String price=et_price.getText().toString().trim();
                final String stocksize=et_stcksize.getText().toString();
                final ProgressDialog dialog=new ProgressDialog(this);
                dialog.setTitle("uploading image");;
                dialog.show();
                final StorageReference ref=mStorageRef.child(FB_STORAGE_PATH+System.currentTimeMillis()+"."+ getImageExt(imgUri));

                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(),"image uploaded",Toast.LENGTH_LONG).show();
                        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String downloadurl=task.getResult().toString();
                                String uploadId=mDataBaseRefadmin.child(s1).push().getKey();
                                ImageUpload imageUpload =new ImageUpload(title,downloadurl,desc,price,stocksize);
                                mDataBaseRefadmin.child(s1).child(uploadId).setValue(imageUpload);
                                Intent intent=new Intent(UploadImage.this, SellerInventory.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress=(100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                dialog.setMessage("uploaded" + (int)progress+"");
                            }
                        });
            }
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
            imgUri = data.getData();

        try {
            if(imgUri!=null) {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageView.setImageBitmap(bm);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        s1 = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}