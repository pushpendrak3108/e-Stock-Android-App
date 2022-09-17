package com.example.projapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.os.Bundle;

public class check_in extends AppCompatActivity {
    private EditText mStock, mPrice, mStockId, mQuantity, mSupplier;
    private Button mInsert;
    private String UStock,UPrice,UStockId,UQuantity,USupplier;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        mStockId = findViewById(R.id.stockid);
        mStock = findViewById(R.id.stockname);
        mQuantity = findViewById(R.id.quantity);
        mPrice = findViewById(R.id.price);
        mSupplier = findViewById(R.id.supplier);
        mInsert = findViewById(R.id.insert);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle bundle= getIntent().getExtras();
        if(bundle != null){
            mInsert.setText("OUT");
            UStock=bundle.getString("uStock");
            UStockId=bundle.getString("uStockid");
            UQuantity=bundle.getString("uQuantity");
            UPrice=bundle.getString("uPrice");
            USupplier=bundle.getString("uSupplier");
            mStockId.setText(UStockId);
            mStock.setText(UStock);
            mQuantity.setHint("Enter Quantity");
            mPrice.setHint("Enter price");
            mSupplier.setText(USupplier);
        }

        else{
            mInsert.setText("IN");
        }


        mInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stock = mStock.getText().toString();
                String stockid = mStockId.getText().toString();
                String quantity = mQuantity.getText().toString();
                String price = mPrice.getText().toString();
                String supplier = mSupplier.getText().toString();

                Bundle bundle1 =getIntent().getExtras();
                if(bundle1!=null){
                    // for update
                    String id = fAuth.getCurrentUser().getUid();
                    String cprice=String.valueOf(Integer.parseInt(UPrice)-Integer.parseInt(price));
                    String cquantity=String.valueOf(Integer.parseInt(UQuantity)-Integer.parseInt(quantity));
                    updateToFirebase(id,stockid,stock,cprice,cquantity,supplier);
                    String type= "OUT";
                    updateTransactions(id,stockid,stock,price,quantity,supplier,type);
                }
                else{
                    // for insert
                    String id = fAuth.getCurrentUser().getUid();
                    saveToFirebase(id, stockid, stock, price,quantity,supplier);
                    String type= "IN";
                    updateTransactions(id,stockid,stock,price,quantity,supplier,type);
                }
            }
        });
    }
    private void updateToFirebase(String id, String stockid, String stock, String price, String quantity, String supplier){
        db.collection("users").document(id).collection("stocks").document(stockid).update("stockid", stockid,"stock",stock,"price",price,"quantity",quantity,"supplier",supplier)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(check_in.this, "Data Updated", Toast.LENGTH_SHORT).show();
                            mStockId.setText("");
                            mStock.setText("");
                            mQuantity.setText("");
                            mPrice.setText("");
                            mSupplier.setText("");
                        }else{
                            Toast.makeText(check_in.this, "Error Occured:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(check_in.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveToFirebase(String id, String stockid, String stock, String price, String quantity, String supplier) {
        if (!stock.isEmpty() && !stockid.isEmpty()) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("stock",stock);
            map.put("stockid",stockid);
            map.put("quantity",quantity);
            map.put("price",price);
            map.put("supplier",supplier);
            db.collection("users").document(id).collection("stocks").document(stockid).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(check_in.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                mStockId.setText("");
                                mStock.setText("");
                                mQuantity.setText("");
                                mPrice.setText("");
                                mSupplier.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(check_in.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                }
            });

        } else
            Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();

    }
    private void updateTransactions(String id, String stockid, String stock, String price, String quantity, String supplier, String type){
        if (!stock.isEmpty() && !stockid.isEmpty()) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            HashMap<String,Object> map2 = new HashMap<>();
            map2.put("stock",stock);
            map2.put("stockid",stockid);
            map2.put("quantity",quantity);
            map2.put("price",price);
            map2.put("type", type);
            map2.put("Date and Time", formatter.format(date));
            db.collection("users").document(id).collection("transactions").document(formatter.format(date)).set(map2)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(check_in.this, "Transactions updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(check_in.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                }
            });

        } else
            Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();
    }
    }


