package com.kunal.shopclaws.Chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.R;

public class GlobalChat extends AppCompatActivity {

    private TextView tv;
    private ImageView send;
    private DatabaseReference ref;
    private String username,curr_message;
    private boolean flag;
    LinearLayout layout;
    ScrollView scrollView;
    RelativeLayout layout_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);
        tv=findViewById(R.id.messageArea);
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        send = (ImageView)findViewById(R.id.sendButton);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        flag=getIntent().getBooleanExtra("Flag",false);
        if(flag)
            username=getIntent().getStringExtra("username")+"(Manager)";
        else
            username=getIntent().getStringExtra("username");

            ref= FirebaseDatabase.getInstance().getReference().child("Chat");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv.getText().toString().equals(""))
                {
                    Toast.makeText(GlobalChat.this, "Write some Text", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    curr_message = username+": "+tv.getText().toString();
                    ref.push().setValue(curr_message);
                    tv.setText("");
                }
            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot chat : dataSnapshot.getChildren()) {
                        String message = chat.getValue().toString();
                        if (message.startsWith(username + ":"))
                            addMessageBox(message, 1);
                        else
                            addMessageBox(message, 2);
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(GlobalChat.this);
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
