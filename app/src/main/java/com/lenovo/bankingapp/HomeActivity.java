package com.lenovo.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton bt;
    private RecyclerView recyclerView;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private List<UserModel> modelList=new ArrayList<>();
    private long backPressedTime;
    private Toast backToast;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bt=findViewById(R.id.floating);
        recyclerView=findViewById(R.id.recycler_view);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),AddActivity.class);
                startActivity(i);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        collectionReference=db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    String name=snapshot.getString("name");
                    String email=snapshot.getString("email");
                    String balance=snapshot.getString("balance");

                    modelList.add(new UserModel(name,email,balance));
                }

                MyAdapter adapter=new MyAdapter(HomeActivity.this,modelList);
                recyclerView.setAdapter(adapter);
                pd.dismiss();
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to load data",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(backPressedTime+2000>System.currentTimeMillis())
        {
            backToast.cancel();
            finishAffinity();
//            super.onBackPressed();
//            return;
        }else
        {
            backToast=Toast.makeText(getApplicationContext(),"Press back again to Exit",Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime=System.currentTimeMillis();
    }
}
