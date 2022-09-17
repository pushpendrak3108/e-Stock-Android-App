package com.example.projapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class AdminMenuActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        db= FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        db.collection("users").document(fAuth.getCurrentUser().getUid()).collection("stocks").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot snapshot: task.getResult()){
                            if(Integer.parseInt(snapshot.getString("quantity"))<5){
                                notification("Stock id = "+snapshot.getString("stockid")+"  Name = "+snapshot.getString("stock")+"  Quantity = "+snapshot.getString("quantity"),Integer.parseInt(snapshot.getString("quantity")));
                            }
                        }
                    }
                });
    }
    public void notification(String msg,int id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Low Stock reminder")
                .setContentText(msg)
                .setAutoCancel(true);
        NotificationManagerCompat managercompat = NotificationManagerCompat.from(this);
        managercompat.notify(id,builder.build());

    }

    public void viewStock(View view){
       Intent intent=new Intent(this, ViewStockActivity.class);
       startActivity(intent);
    }
    public void updateStock(View view) {
        Intent intent=new Intent(this, update_stock.class);
        startActivity(intent);
    }

    public void about(View view) {
        Intent intent=new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void reports(View view) {
        Intent intent=new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    public void transactions(View view) {
        Intent intent=new Intent(this, ViewTransactions.class);
        startActivity(intent);
    }
}