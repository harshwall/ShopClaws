package com.kunal.shopclaws.Inventories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kunal.shopclaws.R;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> Item_NameList;
    ArrayList<String> Item_PriceList;
    ArrayList<String> Post_ImageList;

    class SearchViewHolder extends RecyclerView.ViewHolder {
        View mView;
        SimpleDraweeView post_image;
        TextView item_name,item_price;

        public SearchViewHolder(View itemView) {
            super(itemView);


            mView=itemView;
            //typecast the items in list.xml
            post_image=mView.findViewById(R.id.image1);
            item_name=mView.findViewById(R.id.item_name);
            item_price=mView.findViewById(R.id.item_price);

        }
    }

    public SearchAdapter(Context context, ArrayList<String> Item_NameList, ArrayList<String> Item_PriceList, ArrayList<String> Post_ImageList) {
        this.context = context;
        this.Item_NameList = Item_NameList;
        this.Item_PriceList = Item_PriceList;
        this.Post_ImageList = Post_ImageList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.item_name.setText(Item_NameList.get(position));
        holder.item_price.setText(Item_PriceList.get(position));
        holder.post_image.setImageURI(Post_ImageList.get(position));
        holder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Full Name Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Item_NameList.size();
    }
}