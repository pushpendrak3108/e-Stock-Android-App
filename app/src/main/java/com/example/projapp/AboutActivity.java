package com.example.projapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        about=findViewById(R.id.editTextTextMultiLine);
        about.setText("e-Stock is an Inventory Management app." +
                "\nIt is developed as Honours project by :" +
                "\nArpit Jain (BSc Hons CS)" +
                "\nHarsh Upadhyay (BSc Hons CS)" +
                "\nNitin Yadav (BSc Hons CS + DVIT)" +
                "\nPushpendra Kumar (BSc Hons CS)");
    }
}