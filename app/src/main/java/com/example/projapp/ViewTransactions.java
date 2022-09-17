package com.example.projapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewTransactions extends AppCompatActivity {
    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    private tr_adapter adapter;
    private List<tr_model> list;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock);
        recyclerView =findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        list=new ArrayList<>();
        adapter=new tr_adapter(this,list);
        recyclerView.setAdapter(adapter);

    showData();
    }
    public void showData(){
        db.collection("users").document(fAuth.getCurrentUser().getUid()).collection("transactions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for(DocumentSnapshot snapshot: task.getResult()){
                            tr_model trmodel = new tr_model(snapshot.getString("stockid"),snapshot.getString("stock"),snapshot.getString("price"),snapshot.getString("quantity"),snapshot.getString("type"),snapshot.getString("Date and Time"));
                            list.add(trmodel);
                        }
                         adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewTransactions.this, "Oops something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}