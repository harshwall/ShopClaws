package com.kunal.shopclaws.LoginRegister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.kunal.shopclaws.R;

import static com.kunal.shopclaws.LoginRegister.MainActivity.auth;

public class RegisterActivityAdmin extends AppCompatActivity {
    EditText et_username, et_password,et_database;
    Button registerButton;
    String user, pass,database;
    TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);
        Firebase.setAndroidContext(this);
        auth=FirebaseAuth.getInstance();
        et_database=findViewById(R.id.phone);
        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(RegisterActivityAdmin.this, MainActivity.class);
                j.putExtra("Flag",true);
                startActivity(j);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=et_username.getText().toString();
                pass=et_password.getText().toString();
                database=et_database.getText().toString();
                if (user.equals("")) {
                    et_username.setError("can't be blank");
                } else if (pass.equals("")) {
                    et_password.setError("can't be blank");
                }
                else if (user.length() < 5) {
                    et_username.setError("at least 5 characters long");
                }
                else if (pass.length() < 5) {
                    et_password.setError("at least 5 characters long");
                }
                else if(database.length()<10||database.isEmpty()){
                    et_database.setError("Enter valid mobile number");
                }
                else {
                    user=user.trim();
                    pass=pass.trim();
                    database=database.trim();
                    Intent i=new Intent(RegisterActivityAdmin.this,PhoneAuth.class);
                    i.putExtra("phone",database);
                    i.putExtra("email",user);
                    i.putExtra("password",pass);
                    i.putExtra("Flag",true);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
