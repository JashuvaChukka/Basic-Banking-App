package com.lenovo.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button tr;
    private List<UserModel> modelList=new ArrayList<>();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    ProgressDialog pd;
    String id;
    int amount,bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        recyclerView=findViewById(R.id.recyclerView3);
        tr=findViewById(R.id.transfer);

        amount=Integer.parseInt(getIntent().getStringExtra("amount"));
        id=getIntent().getStringExtra("email1");


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.currentItem==null)
                {
                    Toast.makeText(getApplicationContext(),"Select user to transfer",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String id2=Common.currentItem.getEmail();
                    int balance2=Integer.parseInt(Common.currentItem.getBalance());

                    Map<String,Object> user1=new HashMap<>();
                    user1.put("balance",""+(balance2+amount));
                    db.collection("users").document(id2).update(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Amount Transfered",Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(i);                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Error in data uploading",Toast.LENGTH_SHORT).show();
                        }
                    });


                    Map<String,Object> user2=new HashMap<>();
                    user2.put("balance",""+(bd-amount));
                    db.collection("users").document(id).update(user2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
            }
        });

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

                    if(!email.equals(id))
                    modelList.add(new UserModel(name,email,balance));
                    else
                        bd=Integer.parseInt(balance);
                }
//                Toast.makeText(getApplicationContext(),(bd-amount)+"",Toast.LENGTH_SHORT).show();

                TransferAdapter adapter=new TransferAdapter(modelList,TransferActivity.this);
                recyclerView.setAdapter(adapter);
                pd.dismiss();
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Check your Internet Connection!!",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

    }
}
