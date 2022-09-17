package com.example.projapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class  tr_adapter extends RecyclerView.Adapter<tr_adapter.MyViewHolder> {
    private ViewTransactions activity;
    private List<tr_model> mList;
    public tr_adapter(ViewTransactions activity,List<tr_model> mList){
        this.activity=activity;
        this.mList=mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.tr_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.price.setText(mList.get(position).getPrice());
        holder.quantity.setText(mList.get(position).getQuantity());
        holder.stockname.setText(mList.get(position).getStockname());
        holder.stockid.setText(mList.get(position).getStockid());
        holder.type.setText(mList.get(position).getType());
        holder.dateandtime.setText(mList.get(position).getDateandtime());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView stockid,stockname,price,quantity,type,dateandtime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stockid=itemView.findViewById(R.id.stockid);
            stockname=itemView.findViewById(R.id.stockname);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            type=itemView.findViewById(R.id.type);
            dateandtime=itemView.findViewById(R.id.dateandtime);
        }
    }
}
