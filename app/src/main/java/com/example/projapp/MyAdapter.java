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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class  MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ViewStockActivity activity;
    private List<Model> mList;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    public MyAdapter(ViewStockActivity activity,List<Model> mList){
        this.activity=activity;
        this.mList=mList;
    }
    private FirebaseAuth fAuth;

    public void updateData(int position){
        Model item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uStockid",item.getStockid());
        bundle.putString("uStock",item.getStockname());
        bundle.putString("uPrice",item.getPrice());
        bundle.putString("uQuantity",item.getQuantity());
        bundle.putString("uSupplier",item.getSupplier());
        Intent intent = new Intent(activity,check_in.class);

        intent.putExtras(bundle);
        activity.startActivity(intent,bundle);
    }
    public void deleteData(int position){
        fAuth = FirebaseAuth.getInstance();
        Model item = mList.get(position);
        db.collection("users").document(fAuth.getCurrentUser().getUid()).collection("stocks").document(item.getStockid()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notifyRemoved(position);
                            Toast.makeText(activity, "Data deleted", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(activity, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void notifyRemoved(int position){
        mList.remove(position);
        notifyItemRemoved(position);
        activity.showData();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.price.setText(mList.get(position).getPrice());
        holder.quantity.setText(mList.get(position).getQuantity());
        holder.stockname.setText(mList.get(position).getStockname());
        holder.stockid.setText(mList.get(position).getStockid());
        holder.supplier.setText(mList.get(position).getSupplier());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView stockid,stockname,price,quantity,supplier;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stockid=itemView.findViewById(R.id.stockid);
            stockname=itemView.findViewById(R.id.stockname);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            supplier=itemView.findViewById(R.id.supplier);
        }
    }
}
