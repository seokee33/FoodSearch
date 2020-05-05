package com.nadu.foodsearch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.CustomViewHolder> {

    private ArrayList<FoodList> arrayList;
    FoodList shopinfo = new FoodList();
    private Context context;

    public FoodListAdapter(ArrayList<FoodList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodlist,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.iv_Shop_Profile.getContext())
                .load(arrayList.get(position).getIv_Shop_Profile())
                .into(holder.iv_Shop_Profile);
        holder.tv_Shop_Name.setText(arrayList.get(position).getTv_Shop_Name());
        holder.tv_Shop_Food.setText(arrayList.get(position).getTv_Shop_Food());
        holder.tv_Shop_Time.setText(arrayList.get(position).getTv_Shop_Time());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_Shop_Profile;
        TextView tv_Shop_Name;
        TextView tv_Shop_Food;
        TextView tv_Shop_Time;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_Shop_Profile = itemView.findViewById(R.id.iv_Shop_Profile);
            this.tv_Shop_Name = itemView.findViewById(R.id.tv_Shop_Name);
            this.tv_Shop_Food = itemView.findViewById(R.id.tv_Shop_Food);
            this.tv_Shop_Time = itemView.findViewById(R.id.tv_Shop_Time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopinfo = arrayList.get(getAdapterPosition());
                    Intent intent = new Intent(itemView.getContext(),ShopInfo.class);
                    intent.putExtra("ShopInfo",shopinfo);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
