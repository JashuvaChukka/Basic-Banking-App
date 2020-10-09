package com.lenovo.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private EditText enter1;
    private EditText enter2;
    private EditText enter3;
    private Button enter4;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        enter1=findViewById(R.id.enter1);
        enter2=findViewById(R.id.enter2);
        enter3=findViewById(R.id.enter3);
        enter4=findViewById(R.id.enter4);

        enter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=enter1.getText().toString();
                String email=enter2.getText().toString();
                String balance=enter3.getText().toString();

                if(name.equals(""))
                {
                    enter1.setError("Enter name");
                }
                if(email.equals(""))
                {
                    enter2.setError("Enter email");
                }
                if(balance.equals(""))
                {
                    enter3.setError("Enter balance");
                }
                if(!name.equals("") && !email.equals("") && !balance.equals(""))
                {
                    final ProgressDialog pd=new ProgressDialog(AddActivity.this);
                    pd.setMessage("Adding data...");
                    pd.show();

                    Map<String,Object> user1=new HashMap<>();
                    user1.put("name",name);
                    user1.put("email",email);
                    user1.put("balance",balance);

                    db.collection("users").document(email).set(user1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent i =new Intent(getApplicationContext(),HomeActivity.class);
                                        startActivity(i);
                                        pd.dismiss();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to Add data",Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                        }
                    });

                }
            }
        });
    }
}
