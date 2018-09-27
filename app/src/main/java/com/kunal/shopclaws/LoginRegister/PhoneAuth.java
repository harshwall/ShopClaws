package com.kunal.shopclaws.LoginRegister;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kunal.shopclaws.R;

import java.util.concurrent.TimeUnit;

import static com.kunal.shopclaws.LoginRegister.MainActivity.auth;


public class PhoneAuth extends AppCompatActivity {
    private boolean flag;
    String phone_number,email,passwd,encrypt;
    com.alimuzaffar.lib.pin.PinEntryEditText Otp;
    private String mVerificationId;
    private Button submit,Resend;
    private int check=0;
    DatabaseReference mref,mref1;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        Firebase.setAndroidContext(this);
        auth=FirebaseAuth.getInstance();
        Otp=findViewById(R.id.otp);
        submit=findViewById(R.id.btn);
        Resend=findViewById(R.id.resend);
        phone_number=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        passwd=getIntent().getStringExtra("password");
        flag=getIntent().getBooleanExtra("Flag",false);
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(phone_number);
        mref1=FirebaseDatabase.getInstance().getReference().child("admin").child(phone_number);
        sendVerificationCode(phone_number);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyVerificationCode(Otp.getText().toString());

                if (check==1) {
                    Toast.makeText(PhoneAuth.this, "Hogya Auth", Toast.LENGTH_SHORT).show();
                    registerUser(passwd);
                }
                else
                    Toast.makeText(PhoneAuth.this, "Validate correct Otp", Toast.LENGTH_SHORT).show();
            }
        });

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mResendToken!=null) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phone_number,        // Phone number to verify
                            60,               // Timeout duration
                            TimeUnit.MINUTES,   // Unit of timeout
                            PhoneAuth.this,               // Activity (for callback binding)
                            mCallbacks,         // OnVerificationStateChangedCallbacks
                            mResendToken);      // Force Resending Token from callbacks
                }

            }
        });


    }
    public void registerUser(String s){
        try {
            encrypt= Encryption.encrypt(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!flag) {
            FirebaseMessaging.getInstance().subscribeToTopic("notify");
            mref.child("password").setValue(encrypt);
            mref.child("name").setValue(email);
            mref.child("mobile").setValue(phone_number);
            mref.child("verify").setValue("0");
            mref.child("solditems").setValue("0");
            mref.child("revenue").setValue(0);
        }
        else{
            mref1.child("password").setValue(encrypt);
            mref1.child("name").setValue(email);
            mref1.child("mobile").setValue(phone_number);
        }
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {

                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            check=1;
            //storing the verification id that is sent to the user
            mVerificationId = s;
            mResendToken=forceResendingToken;

        }
    };
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneAuth.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            check=1;
                            //verification successful we will start the profile activity
                            registerUser(passwd);
                            Intent intent = new Intent(PhoneAuth.this, MainActivity.class);
                            intent.putExtra("Flag",flag);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

}

