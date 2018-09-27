package com.kunal.shopclaws.Chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kunal.shopclaws.R;

public class ChooseUser extends AppCompatActivity {
    private Button salespersons,managers;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        salespersons=findViewById(R.id.salespersons);
        managers=findViewById(R.id.managers);
        user_id = getIntent().getStringExtra("user_id");

        salespersons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseUser.this,UserDetailsActivitySalespersons.class);
                i.putExtra("user_id",user_id);
                startActivity(i);
            }
        });

        managers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseUser.this,UserDetailsActivityManagers.class);
                i.putExtra("user_id",user_id);
                startActivity(i);
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
}
