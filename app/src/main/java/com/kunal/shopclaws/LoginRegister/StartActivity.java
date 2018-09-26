package com.kunal.shopclaws.LoginRegister;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kunal.shopclaws.Inventories.AdminInventory;
import com.kunal.shopclaws.Inventories.SellerInventory;
import com.kunal.shopclaws.R;

public class StartActivity extends AppCompatActivity {
    private Button Admin,Seller;
    private String UserName;
    private String Password;
    private Boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        call_at_runtime2();
        Admin=findViewById(R.id.button2);
        Seller=findViewById(R.id.button3);
        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=true;
                CheckUserCredentials(flag);

            }
        });
        Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag=false;
                CheckUserCredentials(flag);
            }
        });
    }
    //Shared Preferences comes into play!!
    public void CheckUserCredentials(boolean flag1)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("logDetails",
                Context.MODE_PRIVATE);
        UserName = sharedPreferences.getString("Phone",null);
        Password = sharedPreferences.getString("Password",null);
        flag=sharedPreferences.getBoolean("flag",false);
        Log.d("HAR","Username:"+UserName+" Password:"+Password);
        if(UserName != null && Password != null) {
            //Checking login as seller
            if(!flag) {
                //If he clicked on login as admin
                if(flag1)
                    Toast.makeText(this, "Already Signed in as Seller!", Toast.LENGTH_SHORT).show();
                else {
                    Intent i = new Intent(new Intent(StartActivity.this, SellerInventory.class));
                    i.putExtra("Name",UserName);
                    startActivity(i);
                    finish();
                }
            }
            else {
                //If he clicked on login as seller
                if(!flag1)
                    Toast.makeText(this, "Already Signed in as Admin!", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(StartActivity.this, AdminInventory.class);
                    startActivity(intent);
                    finish();
                }
            }
            }
        else
        {
            Log.d("HAR","Shared preference not found");
            Intent intent=new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("Flag",flag1);
            startActivity(intent);
            finish();
        }
    }


    private void call_at_runtime2() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        else
        {
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                call_at_runtime2();
            }
            else
            {
                //Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
