package com.example.projapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class update_stock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);
    }

    public void addStock(View view) {
        Intent intent=new Intent(this, check_in.class);
        startActivity(intent);
    }

    public void removeStock(View view) {
        Intent intent=new Intent(this, ViewStockActivity.class);
        startActivity(intent);
    }
}