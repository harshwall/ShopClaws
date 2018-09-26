package com.kunal.shopclaws.LoginRegister;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.Inventories.AdminInventory;
import com.kunal.shopclaws.Inventories.SellerInventory;
import com.kunal.shopclaws.R;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    TextView tv_registerUser;
    EditText et_username, et_password, et_database;
    Button loginButton;
    String user, pass, database,decrypt;
    static FirebaseAuth auth;
    DatabaseReference mref;
    Boolean flag;
    private ProgressDialog pd;
    AlertDialog CustomDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();
        // typecasting of different elements
        tv_registerUser = (TextView) findViewById(R.id.register);
        et_database = (EditText) findViewById(R.id.phone);
        et_password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        pd = new ProgressDialog(MainActivity.this);
        flag=getIntent().getBooleanExtra("Flag",false);
        //Check network connection!
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please check your internet connection!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            isNetworkConnected();
                        }
                    });

                    builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });

                    builder.setNeutralButton("SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
                    CustomDialog = builder.create();
                    CustomDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });




        tv_registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag) {
                    Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i= new Intent(MainActivity.this,RegisterActivityAdmin.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
    }
    public void isNetworkConnected()
    {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please check your internet connection!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            isNetworkConnected();
                        }
                    });

                    builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });

                    builder.setNeutralButton("SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
                    CustomDialog = builder.create();
                    CustomDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        isNetworkConnected();
    }

    private void signIn() {
        pd.setMessage("Signing In...");
        pd.show();
        pass = et_password.getText().toString();
        database=et_database.getText().toString();
        DatabaseReference mref1=FirebaseDatabase.getInstance().getReference().child("admin");
        mref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if(dataSnapshot.getChildren().iterator().equals(database)){

                    if(flag)
                        mref=FirebaseDatabase.getInstance().getReference().child("admin").child(database);
                    else
                        mref= FirebaseDatabase.getInstance().getReference().child("users").child(database);
                    if (database.isEmpty() || pass.isEmpty()) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Field blank", Toast.LENGTH_SHORT).show();

                    } else {
                        mref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Object passkey = dataSnapshot.child("password").getValue();
                                if(passkey==null)
                                {
                                    pd.dismiss();
                                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    return;

                                }
                                try {
                                    decrypt= Encryption.decrypt(passkey.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if(pass.equals(decrypt)){
                                    SharedPreferences sp =
                                            getSharedPreferences("logDetails", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("Phone", database);
                                    editor.putString("Password", pass);
                                    editor.putBoolean("flag",flag);
                                    editor.commit();
                                    if(!flag) {
                                        Intent i1 = new Intent(MainActivity.this, SellerInventory.class);
                                        i1.putExtra("Name", database);
                                        startActivity(i1);
                                        pd.dismiss();
                                        finish();
                                    }
                                    else
                                    {
                                        Intent i1 = new Intent(MainActivity.this, AdminInventory.class);
                                        i1.putExtra("Name", database);
                                        startActivity(i1);
                                        pd.dismiss();
                                        finish();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

            //}
               // else{
                  //  Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                //}



        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}