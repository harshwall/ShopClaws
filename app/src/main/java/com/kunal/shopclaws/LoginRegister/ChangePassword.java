package com.kunal.shopclaws.LoginRegister;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.R;

public class ChangePassword extends AppCompatActivity {
    private DatabaseReference mref,mref1;
    private EditText et_changepass,et_confchangepass;
    private Button btn_changepass;
    private String phoneNumber,changepass,confchangepass,passkey ;
    private int change =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        phoneNumber=getIntent().getStringExtra("phone");
        et_changepass=findViewById(R.id.change_pass);
        et_confchangepass=findViewById(R.id.conf_change_pass);
        btn_changepass=findViewById(R.id.btn_change_pass);
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(phoneNumber);
        mref1=FirebaseDatabase.getInstance().getReference().child("admin").child(phoneNumber);

        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changepass=et_changepass.getText().toString();
                confchangepass=et_confchangepass.getText().toString();
                if(changepass.trim().isEmpty() || confchangepass.trim().isEmpty())
                    Toast.makeText(ChangePassword.this, "Enter field blank", Toast.LENGTH_SHORT).show();
                else
                if(!changepass.trim().equals(confchangepass.trim()))
                    Toast.makeText(ChangePassword.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                else
                {
                    //if user is a salesperson
                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("name"))
                            {
                                try {
                                    passkey= Encryption.encrypt(changepass.trim());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mref.child("password").setValue(passkey);
                                change++;
                                //Toast.makeText(ChangePassword.this, changepass.trim(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ChangePassword.this, MainActivity.class);
                                intent.putExtra("Flag",false);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //if user is an admin
                    mref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("name"))
                            {
                                try {
                                    passkey= Encryption.encrypt(changepass.trim());
                                    //Toast.makeText(ChangePassword.this, changepass.trim(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mref1.child("password").setValue(passkey);
                                change++;
                                Intent intent=new Intent(ChangePassword.this, MainActivity.class);
                                intent.putExtra("Flag",true);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


    }
}
