package com.kunal.shopclaws.LoginRegister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;

public class RequestOtp extends AppCompatActivity {
    private EditText mobile;
    private Button submit;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext(), ImagePipelineConfigFactory.getImagePipelineConfig(getApplicationContext()));
        setContentView(R.layout.activity_request_otp);
        mobile=findViewById(R.id.f_phone);
        submit=findViewById(R.id.loginButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobile.getText().toString().equals(""))
                    Toast.makeText(RequestOtp.this, "Please enter mobile number!", Toast.LENGTH_SHORT).show();
                else
                {
                    Intent i=new Intent(RequestOtp.this,OtpForgotPassword.class);
                    i.putExtra("mobile",mobile.getText().toString().trim());
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
