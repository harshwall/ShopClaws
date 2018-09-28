package com.kunal.shopclaws.Chat;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kunal.shopclaws.R;

import java.util.HashMap;
import java.util.Map;

public class VizAViz extends AppCompatActivity {
    private LinearLayout layout;
    private RelativeLayout layout_2;
    private ImageView sendButton;
    private EditText messageArea;
    private ScrollView scrollView;
    private Firebase reference1, reference2;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton back;
    private TextView tv_uname;
    private String imgchat;
    private SimpleDraweeView chatimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viz_aviz);
        toolbar =  findViewById(R.id.vizchat_tool);
        setSupportActionBar(toolbar);

        back = findViewById(R.id.back_btn);
        tv_uname = findViewById(R.id.tv_uname);
        tv_uname.setText(UserDetails.chatWith);
        chatimg=findViewById(R.id.chat_img);
        imgchat = getIntent().getStringExtra("imgchat");
        if(imgchat!=null)
            chatimg.setImageURI(Uri.parse(imgchat));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        Firebase.setAndroidContext(this);

        reference1 = new Firebase("https://salesmanagement-a0741.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://salesmanagement-a0741.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if(map.get("message")!=null) {
                    String message = map.get("message").toString();
                    String userName = map.get("user").toString();

                    if (userName.equals(UserDetails.username)) {
                        addMessageBox(message, 1);
                    } else {
                        addMessageBox(message, 2);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(VizAViz.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;
        lp2.topMargin=10;


        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;

            textView.setBackgroundResource(R.drawable.msg_bg);
            textView.setTextSize(15);
            textView.setMaxWidth(750);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.etmsg_rec);
            textView.setTextSize(15);
            textView.setMaxWidth(750);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
