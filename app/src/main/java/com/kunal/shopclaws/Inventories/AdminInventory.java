package com.kunal.shopclaws.Inventories;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.NotificationActivity;
import com.kunal.shopclaws.Chat.ChooseUser;
import com.kunal.shopclaws.Chat.GlobalChat;
import com.kunal.shopclaws.Utility.GraphRevenue;
import com.kunal.shopclaws.Utility.LeaderboardActivity;
import com.kunal.shopclaws.Utility.MyGraph;
import com.kunal.shopclaws.Utility.PushNotification;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.LoginRegister.StartActivity;
import com.kunal.shopclaws.Profile.Seller_Profiles;
import com.kunal.shopclaws.UploadImage.UploadImage;
import com.kunal.shopclaws.fragments.ImageListFragmentAdmin;

import java.util.ArrayList;
import java.util.List;

public class AdminInventory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static ViewPager viewPager;
    static TabLayout tabLayout;
    public static Context context;
    public static String user_id;
    TextView tv_hd1,tv_hd2;
    String user_name,user_email;
    AlertDialog CustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=AdminInventory.this;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences sharedPreferences = getSharedPreferences("logDetails",
                Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Phone",null);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }

        FirebaseDatabase.getInstance().getReference().child("admin").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.child("name").getValue().toString();
                user_email = dataSnapshot.child("mobile").getValue().toString();
                tv_hd1 = findViewById(R.id.hd_tv1);
                tv_hd2 = findViewById(R.id.hd_tv2);
                //Toast.makeText(SellerInventory.this, user_name, Toast.LENGTH_SHORT).show();
                if(tv_hd1!=null)
                    tv_hd1.setText(user_name);
                if(tv_hd2!=null)
                    tv_hd2.setText(user_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display2, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_filter);

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(AdminInventory.this, SearchBarActivity.class));
            return true;
        }else if (id == R.id.action_filter) {

            startActivity(new Intent(AdminInventory.this, LeaderboardActivity.class));

            return true;
        }
        else
            if(id==R.id.action_notifications)
            startActivity(new Intent(AdminInventory.this, PushNotification.class));


        else {
            startActivity(new Intent(AdminInventory.this, NotificationActivity.class));


        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        AdminInventory.Adapter adapter = new AdminInventory.Adapter(getSupportFragmentManager());
        ImageListFragmentAdmin fragment = new ImageListFragmentAdmin();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_1));
        fragment = new ImageListFragmentAdmin();
        bundle = new Bundle();
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_2));
        fragment = new ImageListFragmentAdmin();
        bundle = new Bundle();
        bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_3));
        fragment = new ImageListFragmentAdmin();
        bundle = new Bundle();
        bundle.putInt("type", 4);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_4));
        fragment = new ImageListFragmentAdmin();
        bundle = new Bundle();
        bundle.putInt("type", 5);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_5));
        fragment = new ImageListFragmentAdmin();

        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item1) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_item2) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_item3) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_item4) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_item5) {
            viewPager.setCurrentItem(4);
        }
        else
            if(id==R.id.graph)
            {
                startActivity(new Intent(AdminInventory.this, GraphRevenue.class));
            }
        else if (id == R.id.Logout_item)
        {

            AlertDialog.Builder builder=new AlertDialog.Builder(AdminInventory.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sharedPreferences = getSharedPreferences("logDetails",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Phone", null);
                    editor.putString("Password", null);
                    editor.putBoolean("flag",true);
                    editor.commit();
                    Intent j = new Intent(AdminInventory.this, StartActivity.class);
                    j.putExtra("Flag",true);
                    startActivity(j);
                    finish();
                    dialogInterface.cancel();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            CustomDialog=builder.create();
            CustomDialog.show();

            return true;

        }
        else
        if(id==R.id.sell_here) {
                    startActivity(new Intent(AdminInventory.this, UploadImage.class));
        }
        else
        if(id==R.id.globalchat)
        {
            Intent i = new Intent(AdminInventory.this,GlobalChat.class);
            i.putExtra("username",user_name);
            i.putExtra("Flag",true);
            startActivity(i);
        }
        else
        if(id==R.id.personalchat)
        {
            Intent i = new Intent(AdminInventory.this,ChooseUser.class);
            i.putExtra("user_id",user_id);
            i.putExtra("Flag",false);
            startActivity(i);
        }

        else
        if(id==R.id.my_account)
        {
            startActivity(new Intent(AdminInventory.this,Seller_Profiles.class));
        }
        else
        {
            startActivity(new Intent(AdminInventory.this , NotificationActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

