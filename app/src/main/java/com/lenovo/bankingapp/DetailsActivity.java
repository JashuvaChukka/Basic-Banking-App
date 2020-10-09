package com.lenovo.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity {
    private TextView name;
    private TextView mail;
    private TextView balance;
    private EditText amount;
    private Button next;

    public String id;
    public int n;

    ProgressDialog pd;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentSnapshot documentSnapshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name=findViewById(R.id.name3);
        mail=findViewById(R.id.mail3);
        balance=findViewById(R.id.balance3);
        amount=findViewById(R.id.Amount);
        next=findViewById(R.id.next);

        id=getIntent().getStringExtra("email");

        pd=new ProgressDialog(this);
        pd.setMessage("Loading data....");
        pd.show();

        db.collection("users").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            pd.dismiss();

                            documentSnapshot=task.getResult();
                            name.setText(":  "+documentSnapshot.get("name").toString());
                            mail.setText(":   "+documentSnapshot.get("email").toString());
                            balance.setText(":  Rs."+documentSnapshot.get("balance").toString()+"/-");

                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!"".equals(amount.getText().toString())) {
                                        String n = amount.getText().toString();
                                        Intent i = new Intent(getApplicationContext(), TransferActivity.class);
                                        i.putExtra("amount",n);
                                        i.putExtra("email1",id);
//                                        Toast.makeText(getApplicationContext(),n+":"+id,Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Enter Amount to Transfer",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to load Data!!",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });


    }
}
