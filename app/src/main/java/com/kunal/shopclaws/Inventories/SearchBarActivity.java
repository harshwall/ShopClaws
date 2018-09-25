package com.kunal.shopclaws.Inventories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunal.shopclaws.R;
import com.kunal.shopclaws.cache.ImagePipelineConfigFactory;
import com.kunal.shopclaws.fragments.ImageListFragmentAdmin;

import java.util.ArrayList;

public class SearchBarActivity extends AppCompatActivity {
    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> Item_NameList;
    ArrayList<String> Item_PriceList;
    ArrayList<String> Post_ImageList;
    ArrayList<String> Stock_Size;
    SearchAdapter searchAdapter;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearch);
        SellerInventory sell = new SellerInventory();
        //Getting current item from viewpager.
        a = sell.CalcPostion();
        //Toast.makeText(this, Integer.toString(a), Toast.LENGTH_SHORT).show();
        if (a == 0) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("admin").child("Fashion");
        }else
        if(a==1)
            databaseReference = FirebaseDatabase.getInstance().getReference().child("admin").child("Books");
        else
        if(a==2)
            databaseReference = FirebaseDatabase.getInstance().getReference().child("admin").child("Electronics");
        else
        if(a==3)
            databaseReference = FirebaseDatabase.getInstance().getReference().child("admin").child("Furniture");
        else
        if(a==4)
            databaseReference = FirebaseDatabase.getInstance().getReference().child("admin").child("Others");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL));
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /*
         * Create a array list for each node you want to use
         * */
        Item_NameList = new ArrayList<>();
        Item_PriceList = new ArrayList<>();
        Post_ImageList = new ArrayList<>();
        Stock_Size = new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    /*
                     * Clear the list when editText is empty
                     * */
                    Item_NameList.clear();
                    Item_PriceList.clear();
                    Post_ImageList.clear();
                    recyclerView.removeAllViews();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    /*
                     * Clear the list when editText is empty
                     * */
                    Item_NameList.clear();
                    Item_PriceList.clear();
                    Post_ImageList.clear();
                    recyclerView.removeAllViews();
                }
            }
        });
    }

    private void setAdapter(final String searchedString) {




          //  System.out.println(a);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        /*
                         * Clear the list for every new search
                         * */
                        Item_NameList.clear();
                        Item_PriceList.clear();
                        Post_ImageList.clear();
                        recyclerView.removeAllViews();

                        int counter = 0;

                        /*
                         * Search all users for matching searched string
                         * */
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String uid = snapshot.getKey();
                            String item_name = snapshot.child("title").getValue(String.class);
                            String item_price = snapshot.child("price").getValue(String.class);
                            String profile_pic = snapshot.child("url").getValue(String.class);
                            String stock = snapshot.child("stock_size").getValue(String.class);

                            if (item_name.toLowerCase().contains(searchedString.toLowerCase())) {
                                Item_NameList.add(item_name);
                                Item_PriceList.add(item_price);
                                Post_ImageList.add(profile_pic);
                                Stock_Size.add(stock);
                                counter++;
                            } else if (item_name.toLowerCase().contains(searchedString.toLowerCase())) {
                                Item_NameList.add(item_name);
                                Item_PriceList.add(item_price);
                                Post_ImageList.add(profile_pic);
                                Stock_Size.add(stock);
                                counter++;
                            }

                            /*
                             * Get maximum of 15 searched results only
                             * */
                            if (counter == 15)
                                break;
                        }

                        searchAdapter = new SearchAdapter(SearchBarActivity.this, Item_NameList, Item_PriceList, Post_ImageList,Stock_Size);
                        recyclerView.setAdapter(searchAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }



    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(SearchBarActivity.this , SellerInventory.class);
        startActivity(i);*/
        finish();
      //  super.onBackPressed();
    }
}